package com.gaojun.hasee.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import widget.TimeView;

public class SplashActivity extends Activity {
    private TimeView mTimeView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initElements();
    }

    private void initElements() {
        mTimeView = (TimeView) findViewById(R.id.time_view);
        mTimeView.setStatusChangeListener(new TimeView.StatusChangeListener() {
            @Override
            public void change(int currentStatus) {
                if (currentStatus == 1) {
                    Message message = Message.obtain();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }
        });
        mTimeView.startAnimation();
    }
}
