package com.dianaramirez.rima;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegistroRequest extends StringRequest {
    private static final String ruta = "https://rima-proyect.herokuapp.com/api/user";
    private Map<String, String> parametros;

    public RegistroRequest(String nombre, String apellido, String correo, String contraseña, int edad, Response.Listener<String> listener){
        super(Request.Method.POST, ruta, listener, null);
        parametros = new HashMap<>();
        parametros.put("name", nombre+"");
        parametros.put("lastname", apellido+"");
        parametros.put("email", correo+"");
        parametros.put("password", contraseña+"");
        parametros.put("age", edad+"");
    }

    @Override
    public Map getHeaders() {
        HashMap headers = new HashMap();
        headers.put("Accept", "application/json");
        return headers;
    }

    @Override
    protected Map<String, String> getParams(){
        return parametros;
    }
}
