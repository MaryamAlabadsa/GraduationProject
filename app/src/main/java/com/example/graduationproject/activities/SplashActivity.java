package com.example.graduationproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Bundle;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;


import com.example.graduationproject.R;
import com.example.graduationproject.utils.AppSharedPreferences;

public class SplashActivity extends BaseActivity {
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = new AppSharedPreferences(getApplicationContext());
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(500);
        rotateAnimation.setRepeatCount(Animation.RESTART);

        findViewById(R.id.image).startAnimation(rotateAnimation);
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2000);
//                   if (isLogin()) {
//                      // new WebService().startRequest(WebService.RequestAPI.PROFILE, SplashScreen.this);
//                       startActivity(new Intent(SplashActivity.this,MainActivity.class));
//                    } else {
//                        Intent i = new Intent(SplashActivity.this, SignInActivity.class);
//                        startActivity(i);
//                        finish();
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        t.run();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                run_activity();
                if (isLogin()) {
                    // new WebService().startRequest(WebService.RequestAPI.PROFILE, SplashScreen.this);
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                } else {
                    Intent i = new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        handler.postDelayed(runnable, (long) (1.5 * 1000));
    }
    public void run_activity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
