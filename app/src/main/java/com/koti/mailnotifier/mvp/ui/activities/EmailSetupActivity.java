package com.koti.mailnotifier.mvp.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.koti.mailnotifier.R;
import com.koti.mailnotifier.mvp.db.Account;
import com.koti.mailnotifier.mvp.db.Mail;
import com.koti.mailnotifier.mvp.interfaces.AccountListener;
import com.koti.mailnotifier.mvp.services.AccountService;
import com.koti.mailnotifier.mvp.services.EmailSyncerJobService;
import com.koti.mailnotifier.mvp.services.EmailSyncerService;
import com.koti.mailnotifier.mvp.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmailSetupActivity extends AppCompatActivity implements AccountListener {

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
    private AccountService accountService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolBar.setTitle(getString(R.string.title_email_setup));
        setSupportActionBar(toolBar);
        accountService = new AccountService(getBaseContext(), this);
        accountService.getAccount();
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
        accountService.createAccount(account);
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
                EmailSyncerJobService.callPeriodicEmailCheckerJobService(EmailSetupActivity.this);
                accountService.getAccount();
            }
            else
                Utils.showErrorAlert(EmailSetupActivity.this, getString(R.string.error_create_account));
        }
    };

    private void startHomeActivity(Account account) {
        Intent intent = new Intent(EmailSetupActivity.this, HomeActivity.class);
        intent.putExtra(getString(R.string.extra_account), account);
        startActivity(intent);
        finish();
    }


    @Override
    public void onAccountCreated(Account account) {
        Intent intent = new Intent(EmailSetupActivity.this, EmailSyncerService.class);
        startService(intent);
    }

    @Override
    public void onAccountDeleted() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onUpdateAccount(Account account) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onGetAccount(Account account) {
        startHomeActivity(account);
    }

    @Override
    public void onError(int code, String error) {
        //if (code == Constants.CREATE_ACCOUNT_ERROR)
        // Utils.showErrorAlert(this, error);
    }


}
