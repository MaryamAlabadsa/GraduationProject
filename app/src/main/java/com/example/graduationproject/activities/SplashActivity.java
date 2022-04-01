package com.example.graduationproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Bundle;
import android.os.Bundle;
import android.os.Handler;


import com.example.graduationproject.R;
import com.example.graduationproject.utils.AppSharedPreferences;

public class SplashActivity extends BaseActivity {
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = new AppSharedPreferences(getApplicationContext());
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                   if (isLogin()) {
                      // new WebService().startRequest(WebService.RequestAPI.PROFILE, SplashScreen.this);
                       startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    } else {
                        Intent i = new Intent(SplashActivity.this, SignInActivity.class);
                        startActivity(i);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.run();

    }


}
