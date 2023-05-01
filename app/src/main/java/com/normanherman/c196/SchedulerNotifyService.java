package com.normanherman.c196;

import static com.normanherman.c196.MainActivity.CHANNEL_ID;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.normanherman.c196.database.AppRepository;
import com.normanherman.c196.models.Assessment;
import com.normanherman.c196.models.Course;

import java.util.ArrayList;
import java.util.List;

import com.normanherman.c196.models.Assessment;
import com.normanherman.c196.models.Course;
import com.normanherman.c196.ui.AssessmentAdapter;
import com.normanherman.c196.ui.RecyclerContext;

public class SchedulerNotifyService extends Service{



    @Override
    public void onCreate() {
        Log.v("DEBUG", "Executed onCreate() for Notification Service");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
