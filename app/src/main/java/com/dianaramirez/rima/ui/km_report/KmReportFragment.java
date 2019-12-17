package com.dianaramirez.rima.ui.km_report;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.dianaramirez.rima.Incident;
import com.dianaramirez.rima.IncidentAdapter;
import com.dianaramirez.rima.IncidentRequest;
import com.dianaramirez.rima.Login;
import com.dianaramirez.rima.R;
import com.dianaramirez.rima.ui.total_report.TotalReportViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KmReportFragment extends Fragment {

    private KmReportViewModel kmReportViewModel;
    private List<Incident> incidentList;
    private IncidentAdapter adapter;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        kmReportViewModel =
                ViewModelProviders.of(this).get(KmReportViewModel.class);
        View root = inflater.inflate(R.layout.fragment_km_report, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context= getActivity();
        getIncidents();
    }

    private void configRecycler(){
        final RecyclerView recyclerView= getActivity().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new IncidentAdapter(getContext(), incidentList);
        recyclerView.setAdapter(adapter);
    }

    private void getIncidents(){
        Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    incidentList = new ArrayList<>();
                    JSONObject jsonRespuesta = new JSONObject(response);
                    Boolean no_error = jsonRespuesta.isNull("error");
                    LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Location location = new Location("");
                    if (checkLocationPermission(context)) {
                        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }else {
                        Toast.makeText(context, "No hay permisos para ubicación!" , Toast.LENGTH_LONG).show();
                        Intent i = new Intent(context, Login.class);
                        context.startActivity(i);
                        getActivity().finish();
                    }
                    if(no_error) {
                        JSONArray data = jsonRespuesta.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject incident = data.getJSONObject(i);
                            Location loc = new Location("");
                            loc.setLatitude(Double.valueOf(incident.getString("latitude")));
                            loc.setLongitude(Double.valueOf(incident.getString("longitude")));
                            float distanceInMeters = location.distanceTo(loc);
                            int distanceKms = Math.round(distanceInMeters/1000);
                            if(distanceKms < 1) {
                                incidentList.add(
                                    new Incident(
                                        Integer.valueOf(incident.getString("id")),
                                        incident.getString("name"),
                                        incident.getString("description"),
                                        "Incidente a " + distanceKms + " Km",
                                        incident.getString("date"),
                                        Integer.valueOf(incident.getString("type")),
                                        R.drawable.alerta,
                                        false
                                    )
                                );
                            }
                        }
                        configRecycler();
                    }else {
                        Toast.makeText(getContext(), jsonRespuesta.getString("error"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        };
        IncidentRequest r = new IncidentRequest(respuesta);
        RequestQueue cola = Volley.newRequestQueue(getContext());
        cola.add(r);
    }

    public static boolean checkLocationPermission(Context context) {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
}