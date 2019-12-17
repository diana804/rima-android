package com.dianaramirez.rima;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class Registro extends AppCompatActivity {

    CircularProgressButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        btn = (CircularProgressButton) findViewById(R.id.btnSaveRegistro);
        final EditText nombreT = (EditText) findViewById(R.id.name);
        final EditText apellidoT = (EditText) findViewById(R.id.lastname);
        final EditText correoT = (EditText) findViewById(R.id.email);
        final EditText contraseñaT = (EditText) findViewById(R.id.password);
        final EditText edadT = (EditText) findViewById(R.id.age);
        TextView goToLogin = (TextView) findViewById(R.id.btnReturnLogin);
        Button btnsave = (Button) findViewById(R.id.btnSaveRegistro);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid()) {
                    btn.startAnimation();
                    String nombre = nombreT.getText().toString();
                    String apellido = apellidoT.getText().toString();
                    String correo = correoT.getText().toString();
                    String contraseña = contraseñaT.getText().toString();
                    int edad = Integer.parseInt(edadT.getText().toString());
                    Response.Listener<String> respuesta = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonRespuesta = new JSONObject(response);
                                Boolean no_error = jsonRespuesta.isNull("error");
                                if (no_error) {
                                    Toast.makeText(getApplicationContext(), "Registrado!", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(Registro.this, Login.class);
                                    Registro.this.startActivity(i);
                                    Registro.this.finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), jsonRespuesta.getString("error"), Toast.LENGTH_LONG).show();
                                }
                                btn.revertAnimation();
                            } catch (JSONException e) {
                                btn.revertAnimation();
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                e.getMessage();
                            }
                        }
                    };
                    RegistroRequest r = new RegistroRequest(nombre, apellido, correo, contraseña, edad, respuesta);
                    RequestQueue cola = Volley.newRequestQueue(Registro.this);
                    cola.add(r);
                }
            }
        });
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(Registro.this, Login.class);
                Registro.this.startActivity(login);
                finish();
            }
        });
    }

    private boolean isValid() {
        final EditText nombreT = (EditText) findViewById(R.id.name);
        final EditText apellidoT = (EditText) findViewById(R.id.lastname);
        final EditText correoT = (EditText) findViewById(R.id.email);
        final EditText contraseñaT = (EditText) findViewById(R.id.password);
        final EditText edadT = (EditText) findViewById(R.id.age);
        String nombre = nombreT.getText().toString();
        String apellido = apellidoT.getText().toString();
        String correo = correoT.getText().toString();
        String contraseña = contraseñaT.getText().toString();
        int edad=0;
        if(!edadT.getText().toString().isEmpty()) {
            edad = Integer.parseInt(edadT.getText().toString());
        }

        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo);
        if(!matcher.matches()) {
            correoT.setError("Por favor, ingrese in correo válido");
            return false;
        }

        if(contraseña.length() < 8) {
            contraseñaT.setError("La contraseña debe contener mínimo 8 caracteres");
            return false;
        }

        if(edad<10 || edad>110) {
            edadT.setError("La edad debe estar entre 10 y 110 años");
            return false;
        }

        if(nombre.length()<3) {
            nombreT.setError("El nombre debe contener más de 3 caracteres");
            return  false;
        }

        if(apellido.length()<3) {
            apellidoT.setError("El apellido debe contener más de 3 caracteres");
            return  false;
        }

        return true;
    }
}
