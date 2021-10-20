package com.tbi.coronavirus.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tbi.coronavirus.Activity.MainActivity;
import com.tbi.coronavirus.Pref.sharedprefrance;
import com.tbi.coronavirus.R;
import com.tbi.coronavirus.Service.MyService;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startService(new Intent(getApplicationContext(), MyService.class));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedprefrance.getIsLogin(SplashActivity.this)) {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SplashActivity.this, Splash2Activity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 3000);

    }
}
