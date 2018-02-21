package com.xinthe.mailnotifier.services;


import android.content.Context;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.xinthe.mailnotifier.utils.Constants;

/**
 * Created by Koti on 2/21/2018.
 */

public class EmailSyncerJobService extends JobService {

    public static long currenttime = 0;

    @Override
    public boolean onStartJob(JobParameters params) {
        if (currenttime > 0)
            Log.e("time", "" + (System.currentTimeMillis() - currenttime));
        else
            Log.e("time", "" + System.currentTimeMillis());
        currenttime = System.currentTimeMillis();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    public static void callPeriodicEmailCheckerJobService(Context context) {
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job jobInfo = firebaseJobDispatcher.newJobBuilder().setService(EmailSyncerJobService.class)
                .setTag(String.valueOf(Constants.CHECK_EMAIL_ASYNC_JOB_ID))
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setRecurring(true)
                .setReplaceCurrent(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(0, 30))
                .build();
        firebaseJobDispatcher.mustSchedule(jobInfo);
    }

    public static void cancelJobService(Context context) {
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        firebaseJobDispatcher.cancelAll();
    }
}
