package com.dianaramirez.rima;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ProfileRequest extends StringRequest {
    private static final String ruta = "https://rima-proyect.herokuapp.com/api/user";
    private Map<String, String> parametros;

    public ProfileRequest(int id, String nombre, String apellido, String correo, String contraseña, int edad, Response.Listener<String> listener){
        super(Method.PATCH, ruta+"/"+id, listener, null);
        parametros = new HashMap<>();
        parametros.put("name", nombre+"");
        parametros.put("lastname", apellido+"");
        parametros.put("email", correo+"");
        if(!contraseña.isEmpty()){
            parametros.put("password", contraseña+"");
        }
        parametros.put("age", edad+"");
    }

    public ProfileRequest(int id, Response.Listener<String> listener){
        super(Method.DELETE, ruta+"/"+id, listener, null);
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
