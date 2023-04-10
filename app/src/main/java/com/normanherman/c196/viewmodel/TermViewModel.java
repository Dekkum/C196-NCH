package com.normanherman.c196.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.normanherman.c196.database.AppRepository;
import com.normanherman.c196.models.Term;

import java.util.List;

public class TermViewModel extends AndroidViewModel {

    public LiveData<List<Term>> mTerms;
    private AppRepository mRepository;

    public TermViewModel(@NonNull Application application) {
        super(application);

        mRepository = AppRepository.getInstance(application.getApplicationContext());
        mTerms = mRepository.mTerms;
    }
}
