package com.dianaramirez.rima.ui.personal_incidents;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PersonalIncidentsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PersonalIncidentsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is send fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}