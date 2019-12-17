package com.dianaramirez.rima.ui.personal_incidents;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.dianaramirez.rima.Incident;
import com.dianaramirez.rima.IncidentAdapter;
import com.dianaramirez.rima.IncidentPersonalRequest;
import com.dianaramirez.rima.IncidentRequest;
import com.dianaramirez.rima.Login;
import com.dianaramirez.rima.R;
import com.dianaramirez.rima.ui.total_report.TotalReportViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PersonalIncidentsFragment extends Fragment {

    private PersonalIncidentsViewModel personalIncidentsViewModel;
    private TotalReportViewModel totalReportViewModel;
    private List<Incident> incidentList;
    private IncidentAdapter adapter;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        personalIncidentsViewModel =
                ViewModelProviders.of(this).get(PersonalIncidentsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_personal_incidents, container, false);
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
                            incidentList.add(
                                new Incident(
                                    Integer.valueOf(incident.getString("id")),
                                    incident.getString("name"),
                                    incident.getString("description"),
                                    "Incidente a "+ distanceKms +" Km",
                                    incident.getString("date"),
                                    Integer.valueOf(incident.getString("type")),
                                    R.drawable.alerta,
                                    true));
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
        SharedPreferences prefs = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        JSONObject user;
        String dataUser = prefs.getString("user", "test");
        try {
            JSONObject jsondatUser = new JSONObject(dataUser);
            user = new JSONObject(jsondatUser.getString("user"));
            IncidentPersonalRequest r = new IncidentPersonalRequest(user.getString("id"), respuesta);
            RequestQueue cola = Volley.newRequestQueue(getContext());
            cola.add(r);
        } catch (JSONException e) {
            Toast.makeText(getContext(), e.getMessage() , Toast.LENGTH_LONG).show();
        }
    }

    public static boolean checkLocationPermission(Context context) {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
}