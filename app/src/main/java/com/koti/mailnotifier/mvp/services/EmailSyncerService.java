package com.koti.mailnotifier.mvp.services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.sun.mail.pop3.POP3Store;
import com.koti.mailnotifier.R;
import com.koti.mailnotifier.mvp.db.Account;
import com.koti.mailnotifier.mvp.db.Mail;
import com.koti.mailnotifier.mvp.interfaces.AccountListener;
import com.koti.mailnotifier.mvp.receivers.EmailWidget;
import com.koti.mailnotifier.mvp.utils.Utils;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;

/**
 * Created by Koti on 18-02-2018.
 */

public class EmailSyncerService extends IntentService implements AccountListener {
    private AccountService accountService;
    private Account account;

    public EmailSyncerService() {
        super("Email Syncer");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        accountService = new AccountService(getApplicationContext(), this);
        accountService.getAccount();
    }

    private void fetchMails() {
        if (account != null) {
            Properties properties = new Properties();
            properties.put("mail.pop3.host", account.getHost());
            properties.put("mail.pop3.ssl.enable", "true");
            properties.put("mail.pop3.port", account.getPort());
            Session emailSession = Session.getDefaultInstance(properties);
            if (emailSession != null) {
                try {
                    POP3Store emailStore = (POP3Store) emailSession.getStore(account.getStoreType());
                    emailStore.connect(account.getUsername(), account.getPassword());
                    Folder emailFolder = emailStore.getFolder("INBOX");
                    emailFolder.open(Folder.READ_ONLY);
                    Message[] messages = emailFolder.getMessages();
                    Mail mail = account.getMail();
                    if (mail.getMailCount() > 0 && messages.length > mail.getMailCount())
                        sendNewMailBroadcast();
                    mail.setLastEmailFrom(messages[messages.length - 1].getFrom()[0].toString());
                    mail.setMailCount(messages.length);
                    account.setMail(mail);
                    updateWidget(mail.getLastEmailFrom());
                    emailFolder.close(false);
                    emailStore.close();
                    sendBroadcast(true);
                } catch (NoSuchProviderException exception1) {
                    exception1.printStackTrace();
                    sendBroadcast(false);
                } catch (MessagingException exception2) {
                    exception2.printStackTrace();
                    sendBroadcast(false);
                }
            } else
                sendBroadcast(false);
        }
    }

    private void updateWidget(String email) {
        Context context = this;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.email_widget);
        ComponentName thisWidget = new ComponentName(context, EmailWidget.class);
        remoteViews.setTextViewText(R.id.widget_email_from, email);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }

    private void sendBroadcast(boolean status) {
        if (!status) {
            accountService.deleteAccount(account);
            Utils.cancelAlarm(this);
        } else
            accountService.updateAccount(account);
        Intent intent = new Intent(getString(R.string.receiver_accountsetup));
        intent.putExtra(getString(R.string.extra_status), status);
        sendBroadcast(intent);
    }

    private void sendNewMailBroadcast() {
        Intent intent = new Intent(getString(R.string.action_new_mail_receiver));
        sendBroadcast(intent);
    }

    @Override
    public void onAccountCreated(Account account) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onAccountDeleted() {

    }

    @Override
    public void onGetAccount(Account account) {
        this.account = account;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                fetchMails();
            }
        });

    }

    @Override
    public void onError(int errorCode, String error) {

    }

    @Override
    public void onUpdateAccount(Account account) {

    }
}
