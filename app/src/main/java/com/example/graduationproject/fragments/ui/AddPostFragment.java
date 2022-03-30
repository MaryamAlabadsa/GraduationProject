package com.example.graduationproject.fragments.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.databinding.FragmentAddPostBinding;
import com.example.graduationproject.fragments.BaseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddPostFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "ADD_POSTS";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Animation fab_open, fab_close, rotate_forward, rotate_back;
    boolean isOpen = false; // by default is false
    static final int TAKE_IMAGE_ACTIVITY = 101;
    static final int CAMERA_FROM_CODE = 102;
    static final int CAMERA_REQUEST_CODE = 103;
    FragmentAddPostBinding binding;
    Context context;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddPostFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AddPostFragment newInstance(String param1, String param2) {
        AddPostFragment fragment = new AddPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddPostBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getActivity();

        // Spinner code
        ArrayAdapter adapter = ArrayAdapter.createFromResource(context, R.array.Donations, R.layout.color_spinner);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(this);


        // Animation
        fab_open = AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim);
        fab_close = AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim);
        rotate_forward = AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim);
        rotate_back = AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim);

        // set the click listener on the main fab
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationFab();
            }
        });
        binding.fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationFab();
                //Add photo from gallery
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, TAKE_IMAGE_ACTIVITY);
            }
        });

        binding.fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationFab();
                askCameraPermission();
//                Toast.makeText(AddPost.this, "Click To Write", Toast.LENGTH_SHORT).show();
            }
        });
        return view;

    }


    private void AnimationFab() {
        if (isOpen) {
            binding.fab.startAnimation(rotate_forward);
            binding.fab1.startAnimation(fab_close);
            binding.fab2.startAnimation(fab_close);
            binding.fab1.setClickable(false);
            binding.fab2.setClickable(false);
            isOpen = false;
        } else {
            binding.fab.startAnimation(rotate_back);
            binding.fab1.startAnimation(fab_open);
            binding.fab2.startAnimation(fab_open);
            binding.fab1.setClickable(true);
            binding.fab2.setClickable(true);
            isOpen = true;
        }
    }

    //camera
    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_FROM_CODE);
        } else {
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
                Toast.makeText(context, "Camera", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_IMAGE_ACTIVITY || requestCode == CAMERA_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {

            binding.imagePost.setImageBitmap((Bitmap) data.getExtras().get("data"));
            Uri uri = data.getData();
            binding.imagePost.setImageURI(uri);

        } else if (resultCode == getActivity().RESULT_CANCELED) {
            Toast.makeText(context, "THE USER CANCELLED", Toast.LENGTH_LONG).show();
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

    public void checkButton(View view) {
        int radioId = binding.radioGroup.getCheckedRadioButtonId();
        Toast.makeText(context, radioId+"", Toast.LENGTH_SHORT).show();
//        radioButton = findViewById(radioId);
    }




    @Override
    public int getFragmentTitle() {
        return R.string.add_post;
    }
}