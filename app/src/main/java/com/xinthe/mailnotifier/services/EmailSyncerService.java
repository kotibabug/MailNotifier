package com.xinthe.mailnotifier.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.sun.mail.pop3.POP3Store;
import com.xinthe.mailnotifier.MailNotifier;
import com.xinthe.mailnotifier.R;
import com.xinthe.mailnotifier.db.Account;
import com.xinthe.mailnotifier.db.AppDatabase;
import com.xinthe.mailnotifier.db.Mail;
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

public class EmailSyncerService extends IntentService {

    public EmailSyncerService() {
        super("Email Syncer");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // accountModel = intent.getParcelableExtra(getString(R.string.extra_account));
        AppDatabase appDatabase = ((MailNotifier) getApplication()).getDatabaseInstance();
        Account account = appDatabase.accountDao().getAccount();
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
                    if (mail.getMailCount() > 0 && messages.length > mail.getMailCount()) {
                        sendNewMailBroadcast();
                    }
                    mail.setMailCount(messages.length);
                    account.setMail(mail);
                    appDatabase.accountDao().updateAccount(account);
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

    private void deleteAccount() {
        AppDatabase appDatabase = ((MailNotifier) getApplication()).getDatabaseInstance();
        Account account = appDatabase.accountDao().getAccount();
        if (account != null)
            appDatabase.accountDao().deleteAccount(account);
    }

    private void sendBroadcast(boolean status) {
        if (!status) {
            deleteAccount();
            Utils.cancelAlarm(this);
        }
        Intent intent = new Intent(getString(R.string.receiver_accountsetup));
        intent.putExtra(getString(R.string.extra_status), status);
        sendBroadcast(intent);
    }

    private void sendNewMailBroadcast() {
        Intent intent = new Intent("New-Mail-Receiver");
        sendBroadcast(intent);
    }
}
