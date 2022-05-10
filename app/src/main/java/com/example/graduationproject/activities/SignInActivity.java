package com.example.graduationproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.databinding.ActivityMainBinding;
import com.example.graduationproject.databinding.ActivitySignInBinding;
import com.example.graduationproject.retrofit.Creator;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.retrofit.login.SendLogin;
import com.example.graduationproject.retrofit.post.AllPosts;
import com.example.graduationproject.retrofit.register.RegisterResponse;
import com.example.graduationproject.retrofit.register.User;
import com.example.graduationproject.retrofit.token.SendDeviceTokenResponse;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BaseActivity {
    ActivitySignInBinding binding;

    ServiceApi serviceApi;
    Context context = SignInActivity.this;
    private AppSharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        serviceApi = Creator.getClient().create(ServiceApi.class);
        sharedPreferences = new AppSharedPreferences(getApplicationContext());

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                if (email != null && password != null) {
                    login(email, password);
                    Toast.makeText(context, "login", Toast.LENGTH_SHORT).show();
//                    finish();
                }
            }
        });

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,SignUpActivity.class ));
            }
        });
    }

    private void login(String email, String password) {
        SendLogin sendLogin = new SendLogin();
        sendLogin.setEmail(email);
        sendLogin.setPassword(password); /** GET List Resources **/
        Call<RegisterResponse> call = serviceApi.login("application/json", sendLogin);
        call.enqueue(new Callback<RegisterResponse>() {

            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                Log.d("response code", response.code() + "");
                if (response.isSuccessful()) {
                    Log.e("Success", new Gson().toJson(response.body()));
                    assert response.body() != null;
                    User user = response.body().getData().getUser();
                    Gson gson = new Gson();
                    String userString = gson.toJson(user);
                    sharedPreferences.writeString(AppSharedPreferences.USER, userString);
                    sharedPreferences.writeString(AppSharedPreferences.AUTHENTICATION, response.body().getData().getToken());
                    sendDeviceToken();

                } else {
                    String errorMessage = parseError2(response);
                    Toast.makeText(SignInActivity.this, errorMessage + "", Toast.LENGTH_SHORT).show();
                    binding.email.setError(errorMessage);
                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                Log.e("onFailure", t.getMessage() + "");
                call.cancel();
            }
        });
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

    private void sendDeviceToken(){
        String token = sharedPreferences.readString(AppSharedPreferences.AUTHENTICATION);
        String deviceToken = sharedPreferences.readString(AppSharedPreferences.DEVICE_TOKEN);
        Call<SendDeviceTokenResponse> call = serviceApi.sendDeviceToken(deviceToken,"Bearer " + token);
        call.enqueue(new Callback<SendDeviceTokenResponse>() {
            @Override
            public void onResponse(Call<SendDeviceTokenResponse> call, Response<SendDeviceTokenResponse> response) {
                Log.d("response1 code", response.code() + "");
                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    startActivity(new Intent(context, MainActivity.class));
                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                }
            }
            @Override
            public void onFailure(Call<SendDeviceTokenResponse> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }
}