package com.normanherman.c196.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.normanherman.c196.database.AppRepository;
import com.normanherman.c196.models.Assessment;
import com.normanherman.c196.models.AssessmentType;
import com.normanherman.c196.models.Course;
import com.normanherman.c196.models.CourseStatus;
import com.normanherman.c196.models.Mentor;
import com.normanherman.c196.models.Term;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditorViewModel extends AndroidViewModel {
    public MutableLiveData<Term> mLiveTerm = new MutableLiveData<>();
    public MutableLiveData<Course> mLiveCourse = new MutableLiveData<>();
    public MutableLiveData<Assessment> mLiveAssessment = new MutableLiveData<>();
    public MutableLiveData<Mentor> mLiveMentor = new MutableLiveData<>();
    public LiveData<List<Term>> mTerms;
    public LiveData<List<Course>> mCourses;
    public LiveData<List<Assessment>> mAssessments;
    public LiveData<List<Mentor>> mMentors;
    private AppRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public EditorViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(getApplication());
        mTerms = mRepository.mTerms;
        mCourses = mRepository.mCourses;
        mAssessments = mRepository.mAssessments;
        mMentors = mRepository.mMentors;
    }

    public void loadTermData(final int termId) {
        executor.execute(() -> {
            Term term = mRepository.getTermById(termId);
            mLiveTerm.postValue(term);
        });
    }

    public void loadCourseData(final int courseId) {
        executor.execute(() -> {
            Course course = mRepository.getCourseById(courseId);
            mLiveCourse.postValue(course);
        });
    }

    public void loadAssessmentData(final int assessmentId) {
        executor.execute(() -> {
            Assessment assessment = mRepository.getAssessmentById(assessmentId);
            mLiveAssessment.postValue(assessment);
        });
    }

    public void loadMentorData(final int mentorId) {
        executor.execute(() -> {
            Mentor mentor = mRepository.getMentorById(mentorId);
            mLiveMentor.postValue(mentor);
        });
    }

    public void saveTerm(String termTitle, Date startDate, Date endDate) {
        Term term = mLiveTerm.getValue();

        if (term == null) {
            if (TextUtils.isEmpty(termTitle.trim())) {
                return;
            }
            term = new Term(termTitle.trim(), startDate, endDate);
        } else {
            term.setTitle(termTitle.trim());
            term.setStartDate(startDate);
            term.setEndDate(endDate);
        }
        mRepository.insertTerm(term);
    }

    public void saveCourse(String courseTitle, Date startDate, Date endDate, CourseStatus courseStatus, int termId, String note, boolean enableNotifications) {
        Course course = mLiveCourse.getValue();

        if(course == null) {
            if (TextUtils.isEmpty(courseTitle.trim())) {
                return;
            }
            course = new Course(courseTitle.trim(), startDate, endDate, courseStatus, termId);
        } else {
            course.setTitle(courseTitle.trim());
            course.setStartDate(startDate);
            course.setAnticipatedEndDate(endDate);
            course.setCourseStatus(courseStatus);
            course.setTermId(termId);
            course.setNote(note);
            course.setEnabledNotifications(enableNotifications);
        }
        mRepository.insertCourse(course);
    }

    public void overwriteCourse(Course course, int termId) {
        course.setTermId(termId);
        mRepository.insertCourse(course);
    }

    public void overwriteAssessment(Assessment assessment, int courseId) {
        assessment.setCourseId(courseId);
        mRepository.insertAssessment(assessment);
    }

    public void overwriteMentor(Mentor mentor, int courseId) {
        mentor.setCourseId(courseId);
        mRepository.insertMentor(mentor);
    }

    public void saveAssessment(String assessmentTitle, Date date, AssessmentType assessmentType, int courseId, boolean checked) {
        Assessment assessment = mLiveAssessment.getValue();

        if(assessment == null) {
            if(TextUtils.isEmpty(assessmentTitle.trim())) {
                return;
            }
            assessment = new Assessment(assessmentTitle.trim(), date, assessmentType, courseId);
        } else {
            assessment.setTitle(assessmentTitle.trim());
            assessment.setDate(date);
            assessment.setAssessmentType(assessmentType);
            assessment.setCourseId(courseId);
            assessment.setEnabledNotifications(checked);
        }
        mRepository.insertAssessment(assessment);
    }

    public void saveMentor(String name, String email, String phone, int courseId) {
        Mentor mentor = mLiveMentor.getValue();

        if(mentor == null) {
            if(TextUtils.isEmpty(name.trim())) {
                return;
            }
            mentor = new Mentor(name, email, phone, courseId);
        } else {
            mentor.setName(name);
            mentor.setEmail(email);
            mentor.setPhone(phone);
            mentor.setCourseId(courseId);
        }
        mRepository.insertMentor(mentor);
    }

    public void deleteTerm() {
        mRepository.deleteTerm(mLiveTerm.getValue());
    }

    public void deleteCourse() {
        mRepository.deleteCourse(mLiveCourse.getValue());
    }

    public void deleteAssessment() {
        mRepository.deleteAssessment(mLiveAssessment.getValue());
    }

    public LiveData<List<Course>> getCoursesInTerm(int termId) {
        return (mRepository.getCoursesByTerm(termId));
    }

    public LiveData<List<Assessment>> getAssessmentsInCourse(int courseId) {
        return (mRepository.getAssessmentsByCourse(courseId));
    }

    public LiveData<List<Mentor>> getMentorsInCourse(int courseId) {
        return (mRepository.getMentorsByCourse(courseId));
    }

    public LiveData<List<Course>> getUnassignedCourses() {
        return (mRepository.getCoursesByTerm(-1));
    }

    public LiveData<List<Assessment>> getUnassignedAssessments() {
        return (mRepository.getAssessmentsByCourse(-1));
    }

    public LiveData<List<Mentor>> getUnassignedMentors() {
        return (mRepository.getMentorsByCourse(-1));
    }

    public Term getTermById(int termId) {
        return mRepository.getTermById(termId);
    }
}
