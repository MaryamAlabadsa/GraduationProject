package com.example.graduationproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Bundle;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;


import com.appgozar.fadeoutparticle.FadeOutParticleFrameLayout;
import com.example.graduationproject.R;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.example.graduationproject.utils.UtilMethods;
import com.kodmap.app.library.loader.core.display.FadeInBitmapDisplayer;

import java.util.Locale;

public class SplashActivity extends BaseActivity {
    Handler handler = new Handler();
    Context context = SplashActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FadeOutParticleFrameLayout fade1 = findViewById(R.id.fade_out_particle);
        FadeOutParticleFrameLayout fade2 = findViewById(R.id.fade_out_particle2);
        fade1.startAnimation();
        fade2.startAnimation();
        setLang();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                run_activity();
                if (getIntent().getExtras() != null && (getIntent().getStringExtra("post_id") != null || getIntent().getStringExtra("user_id") != null)) {
                    if (getIntent().getStringExtra("post_id") != null) {
                        Toast.makeText(SplashActivity.this, getIntent().getStringExtra("post_id") + "", Toast.LENGTH_SHORT).show();
                        Intent notificationIntent = new Intent(SplashActivity.this, MainActivity.class);
                        notificationIntent.putExtra("post_id", getIntent().getStringExtra("post_id"));
                        startActivity(notificationIntent);
                        finish();
                    } else if (getIntent().getStringExtra("user_id") != null) {
                        Toast.makeText(SplashActivity.this, getIntent().getStringExtra("user_id") + "", Toast.LENGTH_SHORT).show();
                        Intent notificationIntent = new Intent(SplashActivity.this, MainActivity.class);
                        notificationIntent.putExtra("user_id", getIntent().getStringExtra("user_id"));
                        startActivity(notificationIntent);
                        finish();
                    }

                } else if (UtilMethods.isLogin(context)) {
                    // new WebService().startRequest(WebService.RequestAPI.PROFILE, SplashScreen.this);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    Intent i = new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        handler.postDelayed(runnable, (long) (1.9 * 1000));
    }

    private void setLang() {
        String lang = UtilMethods.getLang(context);
        if (!lang.isEmpty()) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Resources resources = getResources();
            Configuration config = resources.getConfiguration();
            config.setLocale(locale);
            resources.updateConfiguration(config, resources.getDisplayMetrics());
        }

    }
}
