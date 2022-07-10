package com.example.graduationproject.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.databinding.ActivitySignUpBinding;
import com.example.graduationproject.model.RegisterRequest;
import com.example.graduationproject.retrofit.Creator;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.retrofit.register.RegisterResponse;
import com.example.graduationproject.retrofit.register.User;
import com.example.graduationproject.retrofit.token.MessageResponse;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.example.graduationproject.utils.FileUtil;
import com.example.graduationproject.utils.UtilMethods;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity {

    private static final String TAG = "SignUpActivity";
    AppSharedPreferences sharedPreferences;
    String token;
    ServiceApi serviceApi;
    ActivitySignUpBinding binding;
    Context context = SignUpActivity.this;
    File file;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        getSupportActionBar().hide();
        serviceApi = Creator.getClient().create(ServiceApi.class);
        sharedPreferences = new AppSharedPreferences(getApplicationContext());
        changeUserImage();

        binding.lottieImg.setAnimation(R.raw.add_user_image);
        binding.lottieImg.loop(true);
        binding.lottieImg.playAnimation();

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textErrorAddress.setVisibility(View.GONE);
                binding.textErrorPhone.setVisibility(View.GONE);
                binding.textErrorEmail.setVisibility(View.GONE);
                binding.textErrorUserName.setVisibility(View.GONE);
                binding.textErrorPassword.setVisibility(View.GONE);

                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                String location = binding.location.getText().toString();
                String phone = binding.phone.getText().toString();
                String userName = binding.username.getText().toString();
                RegisterRequest request = new RegisterRequest(email, password, location, phone, userName);
                    UtilMethods.launchLoadingLottieDialog(context);
                    register(file,request);
            }
        });
    }

    private void register(File resourceFile, RegisterRequest request) {
        String deviceToken = sharedPreferences.readString(AppSharedPreferences.DEVICE_TOKEN);
        lang = sharedPreferences.readString(AppSharedPreferences.LANG);

        MultipartBody.Part body = null;
        if (resourceFile != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), resourceFile);
            body = MultipartBody.Part.createFormData("image", resourceFile.getName(), requestFile);
        }

        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), request.getUser_name());
        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), request.getEmail());
        RequestBody phoneNumber = RequestBody.create(MediaType.parse("multipart/form-data"), request.getPhone());
        RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), request.getLocation());
        RequestBody password = RequestBody.create(MediaType.parse("multipart/form-data"), request.getPassword());
        RequestBody passwordConfirmation = RequestBody.create(MediaType.parse("multipart/form-data"), request.getPassword());
        RequestBody fcm_token = RequestBody.create(MediaType.parse("multipart/form-data"), deviceToken);
        Call<RegisterResponse> call = serviceApi.register("application/json",lang
                , name
                , email
                , phoneNumber
                , address
                , password
                , passwordConfirmation
                , fcm_token
                , body);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                Log.d("response code", response.code() + "");
                if (response.isSuccessful() || response.code() == 200) {
                    assert response.body() != null;
                    User user = response.body().getData().getUser();
                    Gson gson = new Gson();
                    String userString = gson.toJson(user);

                    sharedPreferences.writeString(AppSharedPreferences.USER, userString);
                    sharedPreferences.writeString(AppSharedPreferences.AUTHENTICATION, response.body().getData().getToken());
                    startActivity(new Intent(context, MainActivity.class));
//                    finish();
                    finishAffinity();
                } else {
                    parseError2(response);
//                    Toast.makeText(context, errorMessage + "", Toast.LENGTH_SHORT).show();
                    UtilMethods.launchLoadingLottieDialogDismiss(context);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.d("onFailure", t.getMessage() + "");
                //progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this, t.getMessage() + "", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
//
    }

    @SuppressLint("SetTextI18n")
    public void parseError2(Response<?> response) {
        String errorMsg = null;
        try {
            assert response.errorBody() != null;
            JSONObject jsonObject = new JSONObject(response.errorBody().string());
            JSONObject jsonObject2 = jsonObject.getJSONObject("errors");

            if (jsonObject2.has("email")) {
                JSONArray jsonArrayEmail = jsonObject2.getJSONArray("email");
                binding.textErrorEmail.setText(jsonArrayEmail.getString(0));
                binding.textErrorEmail.setVisibility(View.VISIBLE);
            }
            if (jsonObject2.has("name")) {
                JSONArray jsonArrayName = jsonObject2.getJSONArray("name");
                binding.textErrorUserName.setText(jsonArrayName.getString(0)+" *");
                binding.textErrorUserName.setVisibility(View.VISIBLE);
            }
            if (jsonObject2.has("phone_number")) {
                JSONArray jsonArrayPhone = jsonObject2.getJSONArray("phone_number");
                binding.textErrorPhone.setText(jsonArrayPhone.getString(0)+" *");
                binding.textErrorPhone.setVisibility(View.VISIBLE);
            }
            if (jsonObject2.has("address")) {
                JSONArray jsonArrayAddress = jsonObject2.getJSONArray("address");
                binding.textErrorAddress.setText(jsonArrayAddress.getString(0)+" *");
                binding.textErrorAddress.setVisibility(View.VISIBLE);
            }if (jsonObject2.has("password")) {
                JSONArray jsonArrayAddress = jsonObject2.getJSONArray("password");
                binding.textErrorPassword.setText(jsonArrayAddress.getString(0)+" *");
                binding.textErrorPassword.setVisibility(View.VISIBLE);
            }
        } catch (Exception ignored) {
            Log.e(errorMsg, ignored.getMessage() + "");
            Toast.makeText(context, ignored.getMessage() + "", Toast.LENGTH_SHORT).show();
//            return ignored.getMessage();
        }
    }

    // camera cooooode
    // change user image

    private void changeUserImage() {
        binding.pickImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                boolean pick = true;
                if (pick == true) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else
                        new ImagePicker.Builder(SignUpActivity.this)
                                .crop(83, 100)                    //Crop image(Optional), Check Customization for more option
                                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(233, 280)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start();

                } else {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else new ImagePicker.Builder(SignUpActivity.this)
                            .crop(83, 100)                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(233, 280)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start();

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        new ImagePicker.Builder(this)
                .crop(83, 100)                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(233, 280)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();

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
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        return res2;
    }

    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }

    private void uploadImage() {
        binding.profileImage.setVisibility(View.VISIBLE);
        binding.lottieImg.setVisibility(View.INVISIBLE);
        binding.profileImage.setImageBitmap(bitmap);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                file = FileUtil.from(context, uri);
                uploadImage();
            } catch (IOException e) {
                Log.w(TAG, "onActivityResult: CROP_IMAGE_ACTIVITY_REQUEST_CODE => ", e);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }


        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
            Uri uri = data.getData();
            //this is written from a fragment.
            CropImage.activity(uri).setAspectRatio(83, 100)
                    .setRequestedSize(233, 280)
                    .setGuidelines(CropImageView.Guidelines.ON).start(this);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
                    uploadImage();
                } catch (IOException e) {
                    Log.w(TAG, "onActivityResult: CROP_IMAGE_ACTIVITY_REQUEST_CODE => ", e);
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.w(TAG, "onActivityResult: ", error);
            }
        }
    }


}