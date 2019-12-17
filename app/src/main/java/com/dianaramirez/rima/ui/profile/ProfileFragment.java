package com.dianaramirez.rima.ui.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.dianaramirez.rima.Login;
import com.dianaramirez.rima.ProfileRequest;
import com.dianaramirez.rima.R;
import com.dianaramirez.rima.Registro;
import com.dianaramirez.rima.RegistroRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    CircularProgressButton btnEditReg;
    CircularProgressButton btnDeleteReg;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_profile, container, false);
        btnEditReg = (CircularProgressButton) root.findViewById(R.id.btnEditRegistro);
        btnDeleteReg=(CircularProgressButton) root.findViewById(R.id.btnEliminarCuenta);

        final EditText nombreT = (EditText) root.findViewById(R.id.name);
        final EditText apellidoT = (EditText) root.findViewById(R.id.lastname);
        final EditText correoT = (EditText) root.findViewById(R.id.email);
        final EditText contraseñaT = (EditText) root.findViewById(R.id.password);
        final EditText edadT = (EditText) root.findViewById(R.id.age);
        int idUser = 0;

        SharedPreferences prefs = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        JSONObject user;
        String dataUser = prefs.getString("user", "test");
        try {
            JSONObject jsondatUser = new JSONObject(dataUser);
            user = new JSONObject(jsondatUser.getString("user"));
            nombreT.setText(user.getString("name"));
            apellidoT.setText(user.getString("lastname"));
            correoT.setText(user.getString("email"));
            edadT.setText(user.getString("age"));
            idUser = Integer.parseInt(user.getString("id"));
            eliminarCuenta(idUser, getActivity(), getContext(), root);
        } catch (JSONException e) {
            Toast.makeText(getContext(), e.getMessage() , Toast.LENGTH_LONG).show();
        }

        Button btnEdit = (Button) root.findViewById(R.id.btnEditRegistro);
        final int finalIdUser = idUser;
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid(root)) {
                    btnEditReg.startAnimation();
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
                                    Toast.makeText(getActivity(), "Editado!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(), jsonRespuesta.getString("error"), Toast.LENGTH_LONG).show();
                                }
                                btnEditReg.revertAnimation();
                            } catch (JSONException e) {
                                btnEditReg.revertAnimation();
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    };
                    ProfileRequest r = new ProfileRequest(finalIdUser, nombre, apellido, correo, contraseña, edad, respuesta);
                    RequestQueue cola = Volley.newRequestQueue(getContext());
                    cola.add(r);
                }
            }
        });
        return root;
    }

    private void eliminarCuenta(final int id, final Activity activity, final Context context, View root) {
        Button btnDelete = (Button) root.findViewById(R.id.btnEliminarCuenta);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDeleteReg.startAnimation();
                Response.Listener<String> respuesta = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        btnDeleteReg.revertAnimation();
                        Toast.makeText(context, "Eliminado!" , Toast.LENGTH_LONG).show();
                        Intent i = new Intent(context, Login.class);
                        context.startActivity(i);
                        activity.finish();
                    }
                };
                ProfileRequest r = new ProfileRequest(id,respuesta);
                RequestQueue cola = Volley.newRequestQueue(context);
                cola.add(r);
            }
        });
    }

    private boolean isValid(View root) {
        final EditText nombreT = (EditText) root.findViewById(R.id.name);
        final EditText apellidoT = (EditText) root.findViewById(R.id.lastname);
        final EditText correoT = (EditText) root.findViewById(R.id.email);
        final EditText contraseñaT = (EditText) root.findViewById(R.id.password);
        final EditText edadT = (EditText) root.findViewById(R.id.age);
        String nombre = nombreT.getText().toString();
        String apellido = apellidoT.getText().toString();
        String correo = correoT.getText().toString();
        String contraseña = contraseñaT.getText().toString();
        int edad = Integer.parseInt(edadT.getText().toString());

        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo);
        if(!matcher.matches()) {
            correoT.setError("Por favor, ingrese in correo válido");
            return false;
        }

        if(contraseña.length() > 0 && contraseña.length() < 8) {
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