package com.dianaramirez.rima.ui.total_report;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TotalReportViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TotalReportViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}