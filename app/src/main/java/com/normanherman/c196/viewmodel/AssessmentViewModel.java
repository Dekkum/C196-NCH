package com.normanherman.c196.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.normanherman.c196.database.AppRepository;
import com.normanherman.c196.models.Assessment;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {

    public LiveData<List<Assessment>> mAssessments;
    private AppRepository mRepository;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);

        mRepository = AppRepository.getInstance(application.getApplicationContext());
        mAssessments = mRepository.mAssessments;
    }
}
