package com.dianaramirez.rima.ui.create_incident;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateIncidentViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CreateIncidentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is share fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}