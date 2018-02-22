package com.koti.mailnotifier.mvp.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.koti.mailnotifier.mvp.receivers.ScheduledEmailCheck;

/**
 * Created by Koti on 18-02-2018.
 */

public class Utils {
    public static void cancelAlarm(Context context) {
        Intent intent = new Intent(context, ScheduledEmailCheck.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, 1000,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }

    public static void scheduleAlarm(Context context) {
        Intent intent = new Intent(context, ScheduledEmailCheck.class);
        boolean isWorking = (PendingIntent.getBroadcast(context, 1000, intent, PendingIntent.FLAG_NO_CREATE) != null);
        if (isWorking)
            return;
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, 1000,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                30000, pIntent);
    }

    public static void showErrorAlert(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
}
