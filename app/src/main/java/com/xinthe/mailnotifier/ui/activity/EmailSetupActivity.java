package com.xinthe.mailnotifier.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.xinthe.mailnotifier.MailNotifier;
import com.xinthe.mailnotifier.R;
import com.xinthe.mailnotifier.db.Account;
import com.xinthe.mailnotifier.db.AppDatabase;
import com.xinthe.mailnotifier.db.Mail;
import com.xinthe.mailnotifier.services.EmailSyncerService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmailSetupActivity extends AppCompatActivity {

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.inputHost)
    EditText host;
    @BindView(R.id.inputStoreType)
    EditText storeType;
    @BindView(R.id.inputPort)
    EditText port;
    @BindView(R.id.inputUsername)
    EditText username;
    @BindView(R.id.inputPassword)
    EditText password;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolBar.setTitle(getString(R.string.title_email_setup));
        setSupportActionBar(toolBar);
        new GetAccount().execute();
    }

    @OnClick(R.id.btn_cancel)
    public void onCancle() {
        finish();
    }

    @OnClick(R.id.btn_submit)
    public void onSubmit() {
        if (progressBar.getVisibility() == View.VISIBLE)
            return;
        progressBar.setVisibility(View.VISIBLE);
        Account account = new Account();
        account.setHost(host.getText().toString());
        account.setStoreType(storeType.getText().toString());
        account.setPort(Integer.parseInt(port.getText().toString()));
        account.setUsername(username.getText().toString());
        account.setPassword(password.getText().toString());
        Mail mail = new Mail();
        mail.setMailCount(0);
        account.setMail(mail);
        new CreateAccount().execute(account);
    }

    class CreateAccount extends AsyncTask<Account, Void, Long> {
        @Override
        protected Long doInBackground(Account... accounts) {
            AppDatabase appDatabase = ((MailNotifier) getApplication()).getDatabaseInstance();
            long insertCount = appDatabase.accountDao().addAccount(accounts[0]);
            return insertCount;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            if (aLong > 0) {
                Intent intent = new Intent(EmailSetupActivity.this, EmailSyncerService.class);
                startService(intent);
            }
        }
    }

    class GetAccount extends AsyncTask<Void, Void, Account> {
        @Override
        protected Account doInBackground(Void... voids) {
            AppDatabase appDatabase = ((MailNotifier) getApplication()).getDatabaseInstance();
            return appDatabase.accountDao().getAccount();
        }

        @Override
        protected void onPostExecute(Account account) {
            super.onPostExecute(account);
            if (account != null) {
                startHomeActivity(account);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(accountSetupReceiver, new IntentFilter(getString(R.string.receiver_accountsetup)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(accountSetupReceiver);
    }

    BroadcastReceiver accountSetupReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean status = intent.getBooleanExtra(getString(R.string.extra_status), false);
            progressBar.setVisibility(View.INVISIBLE);
            if (status) {
                new GetAccount().execute();
            } else
                showErrorAlert();
        }
    };

    private void startHomeActivity(Account account) {
        Intent intent = new Intent(EmailSetupActivity.this, HomeActivity.class);
        intent.putExtra(getString(R.string.extra_username), account.getUsername());
        startActivity(intent);
        finish();
    }

    public void showErrorAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Unable to connect with server, please check credentials.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
}
