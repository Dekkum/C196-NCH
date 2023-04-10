package com.normanherman.c196.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.normanherman.c196.database.AppRepository;
import com.normanherman.c196.models.Course;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    public LiveData<List<Course>> mCourses;
    private AppRepository mRepository;

    public CourseViewModel(@NonNull Application application) {
        super(application);

        mRepository = AppRepository.getInstance(application.getApplicationContext());
        mCourses = mRepository.mCourses;
    }
}
