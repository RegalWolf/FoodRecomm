package com.example.calories.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.calories.R;

import static com.example.calories.Config.Config.SHARED_PREFS;
import static com.example.calories.Config.Config.TOKEN;

public class SplashScreenActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        final String token = sharedPreferences.getString(TOKEN, "token");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (token.equals("token")) {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();
                }
            }
        }, 2000);
    }
}
