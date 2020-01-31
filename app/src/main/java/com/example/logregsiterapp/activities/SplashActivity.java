package com.example.logregsiterapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logregsiterapp.R;
import com.example.logregsiterapp.utils.Constants;

public class SplashActivity extends AppCompatActivity {

    private Handler mHandler;
    private Intent intent;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_USER, MODE_PRIVATE);
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (checkUserLoggedIn()) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },Constants.SPLASH_TIMEOUT);
    }

    public boolean checkUserLoggedIn() {
        return !sharedPreferences.getString(Constants.SHARED_PREFERENCE_USER_NAME,"").isEmpty();
    }
}
