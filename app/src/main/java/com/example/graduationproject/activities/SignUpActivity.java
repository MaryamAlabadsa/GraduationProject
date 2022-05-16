package com.example.graduationproject.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.graduationproject.databinding.ActivitySignUpBinding;
import com.example.graduationproject.model.RegisterRequest;
import com.example.graduationproject.retrofit.Creator;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.retrofit.register.RegisterResponse;
import com.example.graduationproject.retrofit.register.User;
import com.example.graduationproject.retrofit.token.MessageResponse;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.example.graduationproject.utils.FileUtil;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;

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

    AppSharedPreferences sharedPreferences;
    String token;
    ServiceApi serviceApi;
    ActivitySignUpBinding binding;
    Context context = SignUpActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        getSupportActionBar().hide();
        serviceApi = Creator.getClient().create(ServiceApi.class);
        sharedPreferences = new AppSharedPreferences(getApplicationContext());

        binding.pickImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                boolean pick = true;
                if (pick == true) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else PickImage();
                } else {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else PickImage();
                }
            }
        });

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                String location = binding.location.getText().toString();
                String phone = binding.phone.getText().toString();
                String userName = binding.username.getText().toString();
                RegisterRequest request = new RegisterRequest(email, password, location, phone, userName);
                if (validation(request) != null) {
                    register(file, validation(request));
                    showDialog();
                }

            }
        });
    }

    private void register(File resourceFile, RegisterRequest request) {
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
        Call<RegisterResponse> call = serviceApi.register("application/json"
                , name
                , email
                , phoneNumber
                , address
                , password
                , passwordConfirmation
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
                    sendDeviceToken();
                } else {
                    String errorMessage = parseError2(response);
                    Toast.makeText(context, errorMessage + "", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.d("onFailure", t.getMessage() + "");
                call.cancel();
            }
        });
//
    }

    public String parseError2(Response<?> response) {
        String errorMsg = null;
        try {
            assert response.errorBody() != null;
            JSONObject jsonObject = new JSONObject(response.errorBody().string());
            JSONObject jsonObject2 = jsonObject.getJSONObject("errors");
            JSONArray jsonArray = jsonObject2.getJSONArray("email");
            return jsonArray.getString(0);
        } catch (Exception ignored) {
            Log.e(errorMsg, ignored.getMessage() + "");
            return ignored.getMessage();
        }
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

    Uri userImg;
    File file;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri userImg = result.getUri();
                try {
                    InputStream stream = getContentResolver().openInputStream(userImg);
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    binding.pickImage.setImageBitmap(bitmap);
                   file= FileUtil.from(context, userImg);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }
    }

    private void sendDeviceToken() {
        String token = sharedPreferences.readString(AppSharedPreferences.AUTHENTICATION);
        String deviceToken = sharedPreferences.readString(AppSharedPreferences.DEVICE_TOKEN);
        Call<MessageResponse> call = serviceApi.sendDeviceToken(deviceToken, "Bearer " + token);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.d("response1 code", response.code() + "");
                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    startActivity(new Intent(context, MainActivity.class));
                    progressDialog.dismiss();
                } else {
//                    String errorMessage = parseError(response);
//                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    private RegisterRequest validation(RegisterRequest request) {
        if (request.getEmail().trim().isEmpty()) {
            binding.email.setError("can not be empty");
            return null;
        } else if (request.getPassword().trim().isEmpty()) {
            binding.password.setError("can not be empty");
            return null;
        } else if (request.getLocation().trim().isEmpty()) {
            binding.location.setError("can not be empty");
            return null;
        } else if (request.getUser_name().trim().isEmpty()) {
            binding.username.setError("can not be empty");
            return null;
        } else if (request.getPhone().trim().isEmpty()) {
            binding.phone.setError("can not be empty");
            return null;
        } else
            return request;

    }
}