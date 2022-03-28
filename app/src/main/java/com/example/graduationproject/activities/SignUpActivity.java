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
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

public class SignUpActivity extends AppCompatActivity {

    public ImageView image;
    TextInputLayout email,username,location,phone,password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        image = findViewById(R.id.pick_image);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        location = findViewById(R.id.location);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);

        image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                boolean pick = true;
                if(pick==true){
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }else PickImage();
                }
                else {
                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }else PickImage();
                }
            }
        });



    }

    private void PickImage() {
        CropImage.activity().start(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private boolean checkStoragePermission() {
        boolean res2 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==PackageManager.PERMISSION_GRANTED;
        return res2;
    }

    private boolean checkCameraPermission(){
        boolean res1 = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
                ==PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==PackageManager.PERMISSION_GRANTED;
        return res1 && res2;


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                Picasso.with(this).load(resultUri).into(image);
//                try {
//                    InputStream stream = getContentResolver().openInputStream(resultUri);
//                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
//                    image.setImageBitmap(bitmap);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }

        }
    }
}