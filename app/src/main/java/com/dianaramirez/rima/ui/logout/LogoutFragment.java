package com.dianaramirez.rima.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.dianaramirez.rima.Login;
import com.dianaramirez.rima.R;

public class LogoutFragment extends Fragment {

    private LogoutViewModel logoutViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        logoutViewModel =
                ViewModelProviders.of(this).get(LogoutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_km_report, container, false);
        Intent i = new Intent(getContext(), Login.class);
        getContext().startActivity(i);
        getActivity().finish();
        return root;
    }
}
