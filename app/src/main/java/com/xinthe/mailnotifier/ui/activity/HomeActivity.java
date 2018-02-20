package com.xinthe.mailnotifier.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.xinthe.mailnotifier.MailNotifier;
import com.xinthe.mailnotifier.R;
import com.xinthe.mailnotifier.db.Account;
import com.xinthe.mailnotifier.db.AppDatabase;
import com.xinthe.mailnotifier.interfaces.AccountListener;
import com.xinthe.mailnotifier.services.AccountService;
import com.xinthe.mailnotifier.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Koti on 18-02-2018.
 */

public class HomeActivity extends AppCompatActivity implements AccountListener {

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.username)
    TextView username;
    private AccountService accountService;
    private Account account;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        toolBar.setTitle(R.string.title_home);
        setSupportActionBar(toolBar);
        Utils.scheduleAlarm(this);
        account = getIntent().getParcelableExtra(getString(R.string.extra_account));
        username.setText(account.getUsername());
        accountService = new AccountService(getBaseContext(), this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                accountService.deleteAccount(account);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAccountCreated(Account account) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onUpdateAccount(Account account) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onAccountDeleted() {
        Utils.cancelAlarm(HomeActivity.this);
        startActivity(new Intent(HomeActivity.this, EmailSetupActivity.class));
        finish();
    }

    @Override
    public void onGetAccount(Account account) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onError(int errorCode, String error) {
        Utils.showErrorAlert(this, error);
    }
}
