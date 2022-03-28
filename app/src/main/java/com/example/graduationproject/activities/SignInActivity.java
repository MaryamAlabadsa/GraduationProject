package com.example.graduationproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.databinding.ActivityMainBinding;
import com.example.graduationproject.databinding.ActivitySignInBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

public class SignInActivity extends AppCompatActivity {
//    ActivitySignInBinding binding;

    ImageView icon_image;
    TextInputLayout username,password;
    TextView forget;
    RelativeLayout login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
//        binding = ActivitySignInBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        icon_image = findViewById(R.id.icon_img);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        forget = findViewById(R.id.forget);
        login = findViewById(R.id.login);


   }

}