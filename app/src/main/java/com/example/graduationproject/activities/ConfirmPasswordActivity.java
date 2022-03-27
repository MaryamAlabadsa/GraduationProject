package com.example.graduationproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.graduationproject.R;
import com.example.graduationproject.databinding.ActivityConfirmPasswordBinding;
import com.example.graduationproject.databinding.ActivityMainBinding;

public class ConfirmPasswordActivity extends AppCompatActivity {
    ActivityConfirmPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}