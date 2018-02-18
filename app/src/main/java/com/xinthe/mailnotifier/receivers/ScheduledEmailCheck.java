package com.xinthe.mailnotifier.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xinthe.mailnotifier.services.EmailSyncerService;
import com.xinthe.mailnotifier.utils.Utils;

/**
 * Created by Koti on 18-02-2018.
 */

public class ScheduledEmailCheck extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context, EmailSyncerService.class);
        context.startService(intentService);
        Utils.scheduleAlarm(context);
    }
}
