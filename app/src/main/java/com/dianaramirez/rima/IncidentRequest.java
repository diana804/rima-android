package com.dianaramirez.rima;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IncidentRequest extends StringRequest {
    private static final String ruta = "https://rima-proyect.herokuapp.com/api/incident";
    private Map<String, String> parametros;

    public IncidentRequest(String user_id, int tipo, String titulo, String descripcion, String lat, String lng, Response.Listener<String> listener){
        super(Method.POST, ruta, listener, null);
        parametros = new HashMap<>();
        parametros.put("type", tipo+"");
        parametros.put("name", titulo+"");
        parametros.put("description", descripcion+"");
        parametros.put("latitude", lat+"");
        parametros.put("longitude", lng+"");
        parametros.put("user_id", user_id);
        parametros.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    public IncidentRequest(Response.Listener<String> listener){
        super(Method.GET, ruta, listener, null);
    }

    public IncidentRequest(int id, Response.Listener<String> listener){
        super(Method.DELETE, ruta+"/"+id, listener, null);
    }

    public IncidentRequest(int id, int tipo, String titulo, String descripcion, Response.Listener<String> listener){
        super(Method.PATCH, ruta+"/"+id, listener, null);
        parametros = new HashMap<>();
        parametros.put("type", tipo+"");
        parametros.put("name", titulo+"");
        parametros.put("description", descripcion+"");
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
