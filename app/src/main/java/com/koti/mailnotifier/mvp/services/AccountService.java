package com.koti.mailnotifier.mvp.services;

import android.content.Context;
import android.os.AsyncTask;

import com.koti.mailnotifier.mvp.MailNotifier;
import com.koti.mailnotifier.R;
import com.koti.mailnotifier.mvp.db.Account;
import com.koti.mailnotifier.mvp.db.AppDatabase;
import com.koti.mailnotifier.mvp.interfaces.AccountListener;
import com.koti.mailnotifier.mvp.utils.Constants;

/**
 * Created by Koti on 2/20/2018.
 */

public class AccountService {
    private AccountListener accountListener;
    private Context context;

    public AccountService(Context context, AccountListener accountListener) {
        this.context = context;
        this.accountListener = accountListener;
    }

    public void createAccount(Account account) {
        new AsyncTask<Account, Void, Account>() {
            @Override
            protected Account doInBackground(Account... accounts) {
                AppDatabase appDatabase = ((MailNotifier) context.getApplicationContext()).getDatabaseInstance();
                long insert = appDatabase.accountDao().addAccount(accounts[0]);
                if (insert > 0)
                    return accounts[0];
                return null;
            }

            @Override
            protected void onPostExecute(Account account) {
                super.onPostExecute(account);
                if (account != null)
                    accountListener.onAccountCreated(account);
                else
                    accountListener.onError(Constants.CREATE_ACCOUNT_ERROR, context.getString(R.string.error_create_account));
            }
        }.execute(account);
    }

    public void getAccount() {
        new AsyncTask<Void, Void, Account>() {
            @Override
            protected Account doInBackground(Void... voids) {
                AppDatabase appDatabase = ((MailNotifier) context.getApplicationContext()).getDatabaseInstance();
                return appDatabase.accountDao().getAccount();
            }

            @Override
            protected void onPostExecute(Account account) {
                super.onPostExecute(account);
                if (account != null)
                    accountListener.onGetAccount(account);
                else
                    accountListener.onError(Constants.GET_ACCOUNT_ERROR, null);
            }

        }.execute();
    }

    public void deleteAccount(Account account) {
        new AsyncTask<Account, Void, Integer>() {
            @Override
            protected Integer doInBackground(Account... accounts) {
                AppDatabase db = ((MailNotifier) context.getApplicationContext()).getDatabaseInstance();
                int count = db.accountDao().deleteAccount(accounts[0]);
                return count;
            }

            @Override
            protected void onPostExecute(Integer count) {
                super.onPostExecute(count);
                if (count > 0)
                    accountListener.onAccountDeleted();
                else
                    accountListener.onError(Constants.DELETE_ACCOUNT_ERROR, null);
            }
        }.execute(account);
    }

    public void updateAccount(Account account) {
        new AsyncTask<Account, Void, Integer>() {
            @Override
            protected Integer doInBackground(Account... accounts) {
                AppDatabase db = ((MailNotifier) context.getApplicationContext()).getDatabaseInstance();
                return db.accountDao().updateAccount(accounts[0]);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
            }
        }.execute(account);
    }
}
