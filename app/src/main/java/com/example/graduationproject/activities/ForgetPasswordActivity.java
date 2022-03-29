package com.example.graduationproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.graduationproject.R;
import com.example.graduationproject.databinding.ActivityMainBinding;
import com.google.android.material.textfield.TextInputLayout;

public class ForgetPasswordActivity extends AppCompatActivity {
    TextInputLayout pass1,pass2,pass3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        pass1 = findViewById(R.id.pass1);
        pass2 = findViewById(R.id.re_password);
        pass3 = findViewById(R.id.re_password2);
        

    }
}