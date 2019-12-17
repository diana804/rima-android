package com.dianaramirez.rima.ui.create_incident;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.dianaramirez.rima.IncidentRequest;
import com.dianaramirez.rima.Login;
import com.dianaramirez.rima.ProfileRequest;
import com.dianaramirez.rima.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class CreateIncidentFragment extends Fragment {

    private CreateIncidentViewModel createIncidentViewModel;
    private final static String[] types_incidents = { "Agua", "Alcantarillado", "Electricidad",
            "Telefonía", "Vías", "Ríos", "Laderas" };
    CircularProgressButton btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        createIncidentViewModel =
                ViewModelProviders.of(this).get(CreateIncidentViewModel.class);
        View root = inflater.inflate(R.layout.fragment_create_incident, container, false);
        btn = (CircularProgressButton) root.findViewById(R.id.btnCrearAlerta);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, types_incidents);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) root.findViewById(R.id.type_incidents_spinner);
        sItems.setAdapter(adapter);

        final EditText tituloT = (EditText) root.findViewById(R.id.title);
        final EditText descripcionT = (EditText) root.findViewById(R.id.description);

        crearIncidente(tituloT, descripcionT, getActivity(),getContext(), root);

        return root;
    }

    private void crearIncidente(final EditText tituloT, final EditText descripcionT, final Activity activity, final Context context, final View root) {
        Button btnCreate = (Button) root.findViewById(R.id.btnCrearAlerta);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid(root)) {
                    btn.startAnimation();
                    Spinner mySpinner = (Spinner) root.findViewById(R.id.type_incidents_spinner);
                    String tipoT = mySpinner.getSelectedItem().toString();
                    int tipo = 0;
                    switch (tipoT) {
                        case ("Agua"):
                            tipo = 1;
                            break;
                        case ("Alcantarillado"):
                            tipo = 2;
                            break;
                        case ("Electricidad"):
                            tipo = 3;
                            break;
                        case ("Telefonía"):
                            tipo = 4;
                            break;
                        case ("Vías"):
                            tipo = 5;
                            break;
                        case ("Ríos"):
                            tipo = 6;
                            break;
                        case ("Laderas"):
                            tipo = 7;
                            break;
                    }
                    String titulo = tituloT.getText().toString();
                    String descripcion = descripcionT.getText().toString();

                    Response.Listener<String> respuesta = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            btn.revertAnimation();
                            Toast.makeText(context, "Alerta Creada!", Toast.LENGTH_LONG).show();
                            tituloT.setText("");
                            descripcionT.setText("");
                        }
                    };

                    LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                    if (checkLocationPermission(context)) {
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        double lng = location.getLongitude();
                        double lat = location.getLatitude();
                        SharedPreferences prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                        JSONObject user;
                        String dataUser = prefs.getString("user", "test");
                        String user_id = "";
                        try {
                            JSONObject jsondatUser = new JSONObject(dataUser);
                            user = new JSONObject(jsondatUser.getString("user"));
                            user_id = user.getString("id");
                        } catch (JSONException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        IncidentRequest r = new IncidentRequest(user_id, tipo, titulo, descripcion, lat + "", lng + "", respuesta);
                        RequestQueue cola = Volley.newRequestQueue(context);
                        cola.add(r);
                    } else {
                        btn.revertAnimation();
                        Toast.makeText(context, "No hay permisos para ubicación!", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(context, Login.class);
                        context.startActivity(i);
                        activity.finish();
                    }
                }
            }
        });
    }

    public static boolean checkLocationPermission(Context context) {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private boolean isValid(View root) {
        final EditText tituloT = (EditText) root.findViewById(R.id.title);
        final EditText descripcionT = (EditText) root.findViewById(R.id.description);
        if(tituloT.getText().toString().length() < 3) {
            tituloT.setError("El título debe contener mínimo 3 caracteres");
            return  false;
        }
        if(descripcionT.getText().toString().length() < 5) {
            descripcionT.setError("La descripción debe contener mínimo 5 caracteres");
            return  false;
        }
        return true;
    }
}