package com.xinte.mailnotifier.ui.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.xinte.mailnotifier.MailNotifier;
import com.xinte.mailnotifier.R;
import com.xinte.mailnotifier.db.AppDatabase;
import com.xinte.mailnotifier.receivers.ScheduledEmailCheck;
import com.xinte.mailnotifier.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Koti on 18-02-2018.
 */

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.username)
    TextView username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        toolBar.setTitle(R.string.title_home);
        setSupportActionBar(toolBar);
        Utils.scheduleAlarm(this);
        username.setText(getIntent().getStringExtra(getString(R.string.extra_username)));
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
                new Logout().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class Logout extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            AppDatabase db = ((MailNotifier) getApplication()).getDatabaseInstance();
            int count = db.accountDao().deleteAccount(db.accountDao().getAccount());
            return count;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer > 0) {
                Utils.cancelAlarm(HomeActivity.this);
                startActivity(new Intent(HomeActivity.this, EmailSetupActivity.class));
                finish();
            }
        }
    }


}
