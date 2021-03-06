package com.koti.mailnotifier.mvp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.koti.mailnotifier.mvp.ui.activities.NotifierActivity;

/**
 * Created by Koti on 18-02-2018.
 */

public class MailReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mailIntent = new Intent(context, NotifierActivity.class);
        mailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mailIntent);
    }
}
