package com.example.graduationproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddPost extends AppCompatActivity {
    FloatingActionButton fab, fab1, fab2;
    Animation fab_open, fab_close, fab_from, fab_to;

    boolean isOpen = false; // by default is false


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post2);

        fab = findViewById(R.id.add);
        fab1 = findViewById(R.id.btn_edit);
        fab2 = findViewById(R.id.btn_image);

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