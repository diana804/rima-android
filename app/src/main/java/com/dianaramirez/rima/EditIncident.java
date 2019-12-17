package com.dianaramirez.rima;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class EditIncident extends AppCompatActivity {

    Context context;
    private final static String[] types_incidents = { "Agua", "Alcantarillado", "Electricidad",
            "Telefonía", "Vías", "Ríos", "Laderas" };

    CircularProgressButton btnEditInc;
    CircularProgressButton btnDeleteInc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_edit_incident);

        btnEditInc = (CircularProgressButton) findViewById(R.id.btnEditIncidente);
        btnDeleteInc = (CircularProgressButton) findViewById(R.id.btnEliminarIncidente);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, types_incidents);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.type_incidents_spinner);
        sItems.setAdapter(adapter);

        final TextView idT = (TextView) findViewById(R.id.id);
        getData();
        eliminarIncidente(Integer.parseInt(idT.getText().toString()));
        editarIncidente(Integer.parseInt(idT.getText().toString()));

        setToolbar();
    }

    private void editarIncidente(final int idUser) {
        Button btnEdit = (Button) findViewById(R.id.btnEditIncidente);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid()) {
                    btnEditInc.startAnimation();
                    Spinner mySpinner = (Spinner) findViewById(R.id.type_incidents_spinner);
                    String tipoString = mySpinner.getSelectedItem().toString();

                    int tipo = 0;
                    switch (tipoString) {
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

                    final EditText tituloT = (EditText) findViewById(R.id.title);
                    final EditText descripcionT = (EditText) findViewById(R.id.description);

                    String title = tituloT.getText().toString();
                    String description = descripcionT.getText().toString();

                    Response.Listener<String> respuesta = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonRespuesta = new JSONObject(response);
                                Boolean no_error = jsonRespuesta.isNull("error");
                                if (no_error) {
                                    Toast.makeText(context, "Editado!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, jsonRespuesta.getString("error"), Toast.LENGTH_LONG).show();
                                }
                                btnEditInc.revertAnimation();
                            } catch (JSONException e) {
                                btnEditInc.revertAnimation();
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    };
                    IncidentRequest r = new IncidentRequest(idUser, tipo, title, description, respuesta);
                    RequestQueue cola = Volley.newRequestQueue(context);
                    cola.add(r);
                }
            }
        });
    }

    private void getData(){
        Intent intent=getIntent();
        try{
            String title= intent.getStringExtra("title");
            String description= intent.getStringExtra("description");
            String id= intent.getStringExtra("id");
            String type = intent.getStringExtra("type_incident");
            if(!title.isEmpty()){
                final EditText tituloT = (EditText) findViewById(R.id.title);
                final EditText descripcionT = (EditText) findViewById(R.id.description);
                final TextView idT = (TextView) findViewById(R.id.id);
                final Spinner spinner = (Spinner) findViewById(R.id.type_incidents_spinner);
                spinner.setSelection(Integer.parseInt(type)-1);
                tituloT.setText(title);
                descripcionT.setText(description);
                idT.setText(id);
            }
        }catch (Exception e){}
    }

    private void eliminarIncidente(final int id) {
        Button btnDelete = (Button) findViewById(R.id.btnEliminarIncidente);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            btnDeleteInc.startAnimation();
            Response.Listener<String> respuesta = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                btnDeleteInc.revertAnimation();
                Toast.makeText(context, "Eliminado!" , Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, Home.class);
                context.startActivity(i);
                }
            };
            IncidentRequest r = new IncidentRequest(id,respuesta);
            RequestQueue cola = Volley.newRequestQueue(context);
            cola.add(r);
            }
        });
    }

    private boolean isValid() {
        final EditText tituloT = (EditText) findViewById(R.id.title);
        final EditText descripcionT = (EditText) findViewById(R.id.description);
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


    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
