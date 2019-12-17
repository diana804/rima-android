package com.dianaramirez.rima;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IncidentPersonalRequest extends StringRequest {
    private static final String ruta = "https://rima-proyect.herokuapp.com/api/personalIncident";
    private Map<String, String> parametros;

    public IncidentPersonalRequest(String user_id, Response.Listener<String> listener){
        super(Method.POST, ruta, listener, null);
        parametros = new HashMap<>();
        parametros.put("user_id", user_id);
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
