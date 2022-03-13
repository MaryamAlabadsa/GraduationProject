package com.example.graduationproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.Spinner.DataSpinner;
import com.example.graduationproject.adapters.SpinnerCategoriesAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddPost extends AppCompatActivity {
    FloatingActionButton fab, fab1, fab2;
    Animation fab_open, fab_close, fab_from, fab_to;
    Switch req,don;
    Spinner sp_cate;
    private SpinnerCategoriesAdapter adapter;

    boolean isOpen = false; // by default is false


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        fab = findViewById(R.id.add);
        fab1 = findViewById(R.id.btn_edit);
        fab2 = findViewById(R.id.btn_image);
        req = findViewById(R.id.request_post_switch);
        don = findViewById(R.id.donation_post_switch);
        sp_cate = findViewById(R.id.spinner);


        // Spinner code
        adapter = new SpinnerCategoriesAdapter(AddPost.this, DataSpinner.getCategoriesSpinner());
        sp_cate.setAdapter(adapter);


        //Switch code
        req.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(AddPost.this, "You Are Request This", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AddPost.this, "You Are Cancelled Request This", Toast.LENGTH_SHORT).show();

                }
            }
        });

        don.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(AddPost.this, "You Are Donations ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AddPost.this, "You Are Cancelled Donations ", Toast.LENGTH_SHORT).show();

                }
            }
        });



        // Animation
        fab_open = AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim);
        fab_close = AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim);
        fab_from = AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim);
        fab_to = AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim);

        // set the click listener on the main fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationFab();
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationFab();
                Toast.makeText(AddPost.this, "Gallery Click", Toast.LENGTH_SHORT).show();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationFab();
                Toast.makeText(AddPost.this, "Edit Click", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void AnimationFab(){
        if (isOpen){
            fab.startAnimation(fab_from);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isOpen=false;
        }else {
            fab.startAnimation(fab_to);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isOpen=true;
        }
    }
}