package com.dianaramirez.rima.ui.km_report;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class KmReportViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public KmReportViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}