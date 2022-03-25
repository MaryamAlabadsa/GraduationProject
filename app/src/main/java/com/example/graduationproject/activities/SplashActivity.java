package com.example.graduationproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Bundle;
import android.os.Bundle;
import android.os.Handler;


import com.example.graduationproject.R;

public class SplashActivity extends AppCompatActivity {
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }

            }
        };

        @Override
        protected void onResume() {
            super.onResume();
            handler.postDelayed(runnable, 1000);
        }

        @Override
        protected void onPause(){
            super.onPause();
            handler.removeCallbacks(runnable);
        }
    }
