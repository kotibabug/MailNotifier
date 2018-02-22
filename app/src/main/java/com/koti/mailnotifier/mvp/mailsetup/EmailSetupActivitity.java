package com.koti.mailnotifier.mvp.mailsetup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.koti.mailnotifier.R;
import com.koti.mailnotifier.mvp.utils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Koti on 2/22/2018.
 */

public class EmailSetupActivitity extends AppCompatActivity implements EmailSetupFragment.OnCancelClick {
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    private EmailPresenter emailPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailsetup);
        ButterKnife.bind(this);
        toolBar.setTitle(getString(R.string.title_email_setup));
        setSupportActionBar(toolBar);
        EmailSetupFragment emailSetupFragment = (EmailSetupFragment) getSupportFragmentManager().findFragmentById(R.id.content_emailsetup);
        if (emailSetupFragment == null) {
            emailSetupFragment = EmailSetupFragment.getInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), emailSetupFragment, R.id.content_emailsetup);
        }
        emailPresenter = new EmailPresenter(emailSetupFragment);

    }

    @Override
    public void onCancel() {
        finish();
    }
}
