package com.dianaramirez.rima;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private static final String ruta = "https://rima-proyect.herokuapp.com/api/login";
    private Map<String, String> parametros;

    public LoginRequest(String correo, String contraseña, Response.Listener<String> listener){
        super(Request.Method.POST, ruta, listener, null);
        parametros = new HashMap<>();
        parametros.put("email", correo+"");
        parametros.put("password", contraseña+"");
    }

    @Override
    public Map getHeaders() {
        HashMap headers = new HashMap();
        //headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        return headers;
    }


    @Override
    protected Map<String, String> getParams(){
        return parametros;
    }
}
