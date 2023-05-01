package com.normanherman.c196;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.normanherman.c196.models.Assessment;
import com.normanherman.c196.models.Course;
import com.normanherman.c196.models.Mentor;
import com.normanherman.c196.models.Term;
import com.normanherman.c196.ui.RecyclerContext;
import com.normanherman.c196.ui.TermAdapter;
import com.normanherman.c196.utilities.Alerting;
import com.normanherman.c196.viewmodel.MainViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawer;
    NavigationView mNavigationView;

    private List<Term> termData = new ArrayList<>();
    private List<Course> courseData = new ArrayList<>();
    private List<Assessment> assessmentData = new ArrayList<>();
    private TermAdapter mAdapter;
    private MainViewModel mViewModel;
    private TextView termStatus, courseStatus, assessmentStatus, mentorStatus;

    public static final String CHANNEL_ID = "NCH_Scheduler";
    public static final String CHANNEL_NAME = "NCH Scheduler";
    public static final String CHANNEL_DESC = "Notifications from the NCH Scheduler app";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.nav_open, R.string.nav_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        // Tells icons to use full color
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(this);

        ButterKnife.bind(this);

        initViewModel();
        // Initialize status text views
        termStatus = findViewById(R.id.status_terms_count);
        courseStatus = findViewById(R.id.status_courses_count);
        assessmentStatus = findViewById(R.id.status_assessments_count);
        mentorStatus = findViewById(R.id.status_mentors_count);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notifyChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notifyChannel.setDescription(CHANNEL_DESC);
            NotificationManager notifyManager = getSystemService(NotificationManager.class);
            notifyManager.createNotificationChannel(notifyChannel);
        }

    }

    private void handleAlerts() {
        Log.v("DEBUG", "We are handling alerts");
        ArrayList<String> alerts = new ArrayList<>();

        Log.v("DEBUG", "Courses: " + courseData.size() + "\nAssessments: " + assessmentData.size());

        NotificationManager notifyManager = getSystemService(NotificationManager.class);

        // Loop through Courses to find start and end dates.
        for(Course course: courseData) {
            Log.v("DEBUG", "We are looping through courses to find due dates");
            if(course.getEnabledNotifications() && DateUtils.isToday(course.getStartDate().getTime())) {
                Log.v("DEBUG", "Start date is today.");

                Intent notificationIntent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

                Notification newNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Course Starting")
                        .setContentText(course.getTitle() + " is starting today!")
                        .setSmallIcon(R.drawable.rounded_corners)
                        .setContentIntent(pendingIntent)
                        .addAction(R.drawable.rounded_corners, "Course Starting",pendingIntent)
                        .build();
                notifyManager.notify(course.getId(),newNotification);
            } else if(course.getEnabledNotifications() && DateUtils.isToday(course.getAnticipatedEndDate().getTime())) {
                Log.v("DEBUG", "End date is today!");
                Intent notificationIntent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

                Notification newNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Course Ending")
                        .setContentText(course.getTitle() + " is ending today!")
                        .setSmallIcon(R.drawable.rounded_corners)
                        .setContentIntent(pendingIntent)
                        .addAction(R.drawable.rounded_corners, "Course Starting",pendingIntent)
                        .build();
                notifyManager.notify(course.getId(),newNotification);
            }
        }

        // Loop through assessments to find start dates
        for(Assessment assessment: assessmentData) {
            Log.v("DEBUG", "We are looping through assessments to find due dates");
            if(assessment.getEnabledNotifications() && DateUtils.isToday(assessment.getDate().getTime())) {
                Log.v("DEBUG", "Assessment due date is today");

                Intent notificationIntent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

                Notification newNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Assessment Due")
                        .setContentText(assessment.getTitle() + "is DUE TODAY!")
                        .setSmallIcon(R.drawable.rounded_corners)
                        .setContentIntent(pendingIntent)
                        .addAction(R.drawable.rounded_corners, "Assessment Due",pendingIntent)
                        .build();
                notifyManager.notify(assessment.getId(),newNotification);
            }
        }
        // Toast the alerts one at a time
        if(alerts.size() > 0) {
            for(String alert: alerts) {
                AlarmManager alarm = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
                Alerting alerting = new Alerting();
                IntentFilter filter = new IntentFilter("ALARM_ACTION");
                registerReceiver(alerting, filter);

                Intent intent = new Intent("ALARM_ACTION");
                intent.putExtra("param", alert);
                PendingIntent operation = PendingIntent.getBroadcast(this, 0, intent, 0);
                alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ Toast.LENGTH_SHORT, operation);
            }
        }
    }

    private void setStatusNumbers(int count, TextView textView) {
        textView.setText(Integer.toString(count));
    }

    private void initViewModel() {
        // Term observer
        final Observer<List<Term>> termObserver =
            termEntities -> {
                termData.clear();
                termData.addAll(termEntities);
                // Updates term status number
                setStatusNumbers(termEntities.size(), termStatus);
                if (mAdapter == null) {
                    mAdapter = new TermAdapter(termData, MainActivity.this, RecyclerContext.MAIN);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            };
        // Course observer
        final Observer<List<Course>> courseObserver =
            courseEntities -> {
                courseData.clear();
                courseData.addAll(courseEntities);
                // Updates course status number
                setStatusNumbers(courseEntities.size(), courseStatus);
                handleAlerts();
            };

        // Assessment observer
        final Observer<List<Assessment>> assessmentObserver =
            assessmentEntities -> {
                assessmentData.clear();
                assessmentData.addAll(assessmentEntities);
                // Updates assessment status number
                setStatusNumbers(assessmentEntities.size(), assessmentStatus);
                handleAlerts();
            };

        // Mentor observer
        final Observer<List<Mentor>> mentorObserver =
            mentorEntities -> {
                setStatusNumbers(mentorEntities.size(), mentorStatus);
            };
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.mTerms.observe(this, termObserver);
        mViewModel.mCourses.observe(this, courseObserver);
        mViewModel.mAssessments.observe(this, assessmentObserver);
        mViewModel.mMentors.observe(this, mentorObserver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_sample_data) {
            addSampleData();
            return true;
        } else if (id == R.id.action_delete_all) {
            deleteAllData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllData() {
        mViewModel.deleteAllData();
    }

    private void addSampleData() {
        mViewModel.addSampleData();
    }

    public void startService(View v){
        Intent serviceIntent = new Intent(this, SchedulerNotifyService.class);
        startService(serviceIntent);
    }

    public void stopService(View v){
        Intent serviceIntent = new Intent(this, SchedulerNotifyService.class);
        stopService(serviceIntent);
    }

    public void showTerms(View view) {
        Intent intent = new Intent(this, TermActivity.class);
        startActivity(intent);
    }

    public void showCourses(View view) {
        Intent intent = new Intent(this, CourseActivity.class);
        startActivity(intent);
    }

    public void showAssessments(View view) {
        Intent intent = new Intent(this, AssessmentActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_mentors)
    public void showMentors() {
        Intent intent = new Intent(this, MentorActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        int duration = Toast.LENGTH_SHORT;
        Toast toast;

        switch (id) {
            case R.id.nav_terms:
                toast = Toast.makeText(this, "Terms pressed", duration);
                toast.show();
                break;
            case R.id.nav_courses:
                toast = Toast.makeText(this, "Courses pressed", duration);
                toast.show();
                break;
            case R.id.nav_assessments:
                toast = Toast.makeText(this, "Assessments pressed", duration);
                toast.show();
                break;
            case R.id.nav_mentors:
                toast = Toast.makeText(this, "Mentors pressed", duration);
                toast.show();
                showMentors();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
