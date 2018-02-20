package com.xinthe.mailnotifier.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.sun.mail.pop3.POP3Store;
import com.xinthe.mailnotifier.MailNotifier;
import com.xinthe.mailnotifier.R;
import com.xinthe.mailnotifier.db.Account;
import com.xinthe.mailnotifier.db.AppDatabase;
import com.xinthe.mailnotifier.db.Mail;
import com.xinthe.mailnotifier.interfaces.AccountListener;
import com.xinthe.mailnotifier.utils.Utils;

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
                    mail.setMailCount(messages.length);
                    account.setMail(mail);
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
