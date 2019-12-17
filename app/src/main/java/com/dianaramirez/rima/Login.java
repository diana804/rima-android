package com.dianaramirez.rima;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
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

public class Login extends AppCompatActivity {

    private Context context;
    private CircularProgressButton btn;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context=this;
        checkLocationPermission();
        TextView registro = (TextView) findViewById(R.id.btnRegistro);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        final EditText emailT = (EditText) findViewById(R.id.loginEmail);
        final EditText contraseñaT = (EditText) findViewById(R.id.loginContraseña);
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registro = new Intent(Login.this, Registro.class);
                Login.this.startActivity(registro);
                finish();
            }
        });
        btn = (CircularProgressButton) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid()) {
                    btn.startAnimation();
                    final String correo = emailT.getText().toString();
                    final String contraseña = contraseñaT.getText().toString();

                    Response.Listener<String> respuesta = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonRespuesta = new JSONObject(response);
                                Boolean no_error = jsonRespuesta.isNull("error");
                                if (no_error) {
                                    SharedPreferences prefs =
                                            getSharedPreferences("user", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("user", response);
                                    editor.commit();
                                    String dataUser = prefs.getString("user", "");
                                    JSONObject jsondatUser = new JSONObject(dataUser);
                                    JSONObject user = new JSONObject(jsondatUser.getString("user"));
                                    String nameUser = user.getString("name");
                                    Toast.makeText(getApplicationContext(), "Bienvenido " + nameUser + "!", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(Login.this, Home.class);
                                    i.putExtra("name", nameUser);
                                    i.putExtra("email", correo);
                                    Login.this.startActivity(i);
                                    Login.this.finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), jsonRespuesta.getString("error"), Toast.LENGTH_LONG).show();
                                }
                                btn.revertAnimation();
                            } catch (JSONException e) {
                                btn.revertAnimation();
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    };
                    LoginRequest r = new LoginRequest(correo, contraseña, respuesta);
                    RequestQueue cola = Volley.newRequestQueue(Login.this);
                    cola.add(r);
                }
            }
        });
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Location")
                        .setMessage("Necesitamos acceder a este permiso")
                        .setPositiveButton("Acceder", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Login.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    private boolean isValid() {
        final EditText emailT = (EditText) findViewById(R.id.loginEmail);
        final EditText contraseñaT = (EditText) findViewById(R.id.loginContraseña);
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailT.getText().toString());
        if(!matcher.matches()) {
            emailT.setError("Por favor, ingrese in correo válido");
            return false;
        }

        if(contraseñaT.getText().toString().length() < 8) {
            contraseñaT.setError("La contraseña debe contener mínimo 8 caracteres");
            return false;
        }

        return true;
    }
}
