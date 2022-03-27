package com.example.graduationproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddPost extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    //ActivityAddPostBinding binding;
    // private SpinnerCategoriesAdapter adapter;
    //Switch req,don;
    ImageView img_post;
    FloatingActionButton fab_0, fab_1, fab_2;
    Animation fab_open, fab_close, rotate_forward,rotate_back ;
    Spinner sp_cate;
    RadioGroup radioGroup;
    RadioButton radioButton;
    boolean isOpen = false; // by default is false
    static final int TAKE_IMAGE_ACTIVITY =101;
    static final int CAMERA_FROM_CODE =102;
    static final int CAMERA_REQUEST_CODE =103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
//        req = findViewById(R.id.request_post_switch);
//        don = findViewById(R.id.donation_post_switch);
        img_post = findViewById(R.id.image_post);
        fab_0 = findViewById(R.id.fab);
        fab_1 = findViewById(R.id.fab1);
        fab_2 = findViewById(R.id.fab2);
        radioGroup = findViewById(R.id.radio_group);
        sp_cate = findViewById(R.id.spinner);


        // Spinner code
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.Donations,R.layout.color_spinner);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        sp_cate.setAdapter(adapter);
        sp_cate.setOnItemSelectedListener(this);


        // Animation
        fab_open =AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim);
        fab_close = AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim);
        rotate_forward = AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim);
        rotate_back = AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim);

        // set the click listener on the main fab
        fab_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationFab();
            }
        });
        fab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationFab();
                //Add photo from gallery
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery,TAKE_IMAGE_ACTIVITY);
            }
        });

        fab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationFab();
                askCameraPermission();
//                Toast.makeText(AddPost.this, "Click To Write", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void AnimationFab(){
        if (isOpen){
            fab_0.startAnimation(rotate_forward);
            fab_1.startAnimation(fab_close);
            fab_2.startAnimation(fab_close);
            fab_1.setClickable(false);
            fab_2.setClickable(false);
            isOpen=false;
        }else {
            fab_0.startAnimation(rotate_back);
            fab_1.startAnimation(fab_open);
            fab_2.startAnimation(fab_open);
            fab_1.setClickable(true);
            fab_2.setClickable(true);
            isOpen=true;
        }
    }
    //camera
    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_FROM_CODE);
        }
        else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_FROM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,CAMERA_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_IMAGE_ACTIVITY || requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK ) {

            img_post.setImageBitmap((Bitmap) data.getExtras().get("data"));
            Uri uri = data.getData();
            img_post.setImageURI(uri);

        }

        else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getBaseContext(), "THE USER CANCELLED", Toast.LENGTH_LONG).show();
        }
    }



    //spinner code
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getSelectedItem().toString();
//        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
    public void checkButton(View view){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }
}
////2////
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
//                (this,R.array.Donations, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sp_cate.setAdapter(adapter);
//        sp_cate.setOnItemSelectedListener(this);
///////1//////
//        adapter = new SpinnerCategoriesAdapter(AddPost.this, DataSpinner.getCategoriesSpinner());
//        sp_cate.setAdapter(adapter);




//        //Switch code
//        req.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if (isChecked){
//                    Toast.makeText(AddPost.this, "You Are Request This", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(AddPost.this, "You Are Cancelled Request This", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//
//        don.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if (isChecked){
//                    Toast.makeText(AddPost.this, "You Are Donations ", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(AddPost.this, "You Are Cancelled Donations ", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });