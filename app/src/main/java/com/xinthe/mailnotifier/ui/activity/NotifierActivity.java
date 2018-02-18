package com.xinthe.mailnotifier.ui.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.skyfishjy.library.RippleBackground;
import com.xinthe.mailnotifier.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Koti on 18-02-2018.
 */

public class NotifierActivity extends AppCompatActivity {

    @BindView(R.id.content)
    RippleBackground reppleBackground;
    MediaPlayer player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        setContentView(R.layout.activity_notifier);
        ButterKnife.bind(this);
        reppleBackground.startRippleAnimation();
        player = MediaPlayer.create(this,
                Settings.System.DEFAULT_RINGTONE_URI);
        player.start();
    }

    @OnClick(R.id.btn_cancel)
    public void cancel() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null)
            player.stop();
    }

    
}
