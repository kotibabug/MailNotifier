package com.xinthe.mailnotifier.receivers;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.xinthe.mailnotifier.R;
import com.xinthe.mailnotifier.db.Account;
import com.xinthe.mailnotifier.interfaces.AccountListener;
import com.xinthe.mailnotifier.services.AccountService;

/**
 * Implementation of App Widget functionality.
 */
public class EmailWidget extends AppWidgetProvider implements AccountListener {

    private AppWidgetManager appWidgetManager;
    private int appWidgetId;
    private Context context;

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {

        this.context = context;
        this.appWidgetManager = appWidgetManager;
        this.appWidgetId = appWidgetId;
        AccountService accountService = new AccountService(context, this);
        accountService.getAccount();

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onAccountCreated(Account account) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onAccountDeleted() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onGetAccount(Account account) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.email_widget);
        views.setTextViewText(R.id.widget_email_from, account.getMail().getLastEmailFrom());
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdateAccount(Account account) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onError(int errorCode, String error) {

    }


}

