package com.example.graduationproject.fragments.ui;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.databinding.FragmentAddPostBinding;
import com.example.graduationproject.fragments.BaseFragment;
import com.example.graduationproject.retrofit.Creator;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.retrofit.post.AllPosts;
import com.example.graduationproject.retrofit.post.Post;
import com.example.graduationproject.retrofit.register.User;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private ProgressDialog progressDialog;
    Uri postImg;

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
        serviceApi = Creator.getClient().create(ServiceApi.class);
        sharedPreferences = new AppSharedPreferences(getActivity().getApplicationContext());
        token = sharedPreferences.readString(AppSharedPreferences.AUTHENTICATION);

        String user = sharedPreferences.readUser(AppSharedPreferences.USER);
        Gson gson = new Gson();
        if (!user.isEmpty()) {
            User user1 = gson.fromJson(user, User.class);
            Glide.with(context).load(user1.getImageLink()).circleCrop()
                    .placeholder(R.drawable.ic_launcher_foreground).into(binding.imageProfile);
            binding.userName.setText(user1.getName());
        }

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

        binding.postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please Wait");
                progressDialog.setCancelable(false);
                progressDialog.show();
                String uTitle = binding.titlePost.getText().toString();
                String uDescription = binding.titlePost.getText().toString();

//                addPostRequest(postImg,uTitle,uDescription,);
            }
        });
        return view;

    }

    private void addPostRequest(File resourceFile, String uTitle, String uDescription, int uIsDonation, int uCategoryId) {
        MultipartBody.Part body = null;
        if (resourceFile != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), resourceFile);
            body = MultipartBody.Part.createFormData("assets", resourceFile.getName(), requestFile);
        }
        RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), uTitle);
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), uDescription);
        RequestBody is_donation = RequestBody.create(MediaType.parse("multipart/form-data"), uIsDonation + "");
        RequestBody category_id = RequestBody.create(MediaType.parse("multipart/form-data"), uCategoryId + "");
        Call<AllPosts> call = serviceApi.addPost("Bearer " + token
//                "application/json"
                , title
                , description
                , is_donation
                , category_id
                , body);
        call.enqueue(new Callback<AllPosts>() {
            @Override
            public void onResponse(Call<AllPosts> call, Response<AllPosts> response) {
                Log.d("response code", response.code() + "");
                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    progressDialog.dismiss();
                } else {
                    String errorMessage = parseError(response);
                    Toast.makeText(context, errorMessage + "o", Toast.LENGTH_SHORT).show();
                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<AllPosts> call, Throwable t) {
                Log.d("onFailure", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    public static String parseError(Response<?> response) {
        String errorMsg = null;
        try {
            JSONObject jObjError = new JSONObject(response.errorBody().string());
            errorMsg = jObjError.getString("message");
            return errorMsg;
        } catch (Exception e) {
        }
        return errorMsg;
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
            postImg = data.getData();
            binding.imagePost.setImageURI(postImg);

        } else if (resultCode == getActivity().RESULT_CANCELED) {
            Toast.makeText(context, "THE USER CANCELLED", Toast.LENGTH_LONG).show();
        }
    }


    //spinner code
    String category;

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String category = adapterView.getSelectedItem().toString();
        Toast.makeText(adapterView.getContext(), category, Toast.LENGTH_SHORT).show();
    }

    int getCategoryName() {
        switch (category) {
//            case R.array.Donations.:

        }
        return 4;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
RadioButton radioButton;
    public void checkButton(View view) {
        int radioId = binding.radioGroup.getCheckedRadioButtonId();
        Toast.makeText(context, radioId + "", Toast.LENGTH_SHORT).show();
        radioButton = radioButton.findViewById(radioId);
    }


    @Override
    public int getFragmentTitle() {
        return R.string.add_post;
    }
}