package com.example.graduationproject.activities;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.graduationproject.databinding.ActivitySignInBinding;
import com.example.graduationproject.fragments.ui.ChangePasswordFragment;
import com.example.graduationproject.retrofit.Creator;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.retrofit.login.SendLogin;
import com.example.graduationproject.retrofit.register.RegisterResponse;
import com.example.graduationproject.retrofit.register.User;
import com.example.graduationproject.retrofit.token.MessageResponse;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BaseActivity {
    com.example.graduationproject.databinding.ActivitySignInBinding binding;

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
                String deviceToken = sharedPreferences.readString(AppSharedPreferences.DEVICE_TOKEN);
                String email = binding.email.getText().toString().trim();
                String password = binding.password.getText().toString();
                SendLogin sendLogin = new SendLogin();
                sendLogin.setEmail(email);
                sendLogin.setPassword(password);
                sendLogin.setFcm_token(deviceToken);/** GET List Resources **/
                if (email != null && password != null) {
                    showDialog();
                    login(sendLogin);
                }
            }
        });



        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,SignUpActivity.class ));
            }
        });


//        binding.forgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(SignInActivity.this, ChangePasswordFragment.class);
//                startActivity(intent);            }
//        });
    }

    private void login(SendLogin sendLogin) {

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
                    startActivity(new Intent(context, MainActivity.class));
                } else {
                    String errorMessage = parseError2(response);
                    Toast.makeText(SignInActivity.this, errorMessage + "", Toast.LENGTH_SHORT).show();
                    binding.email.setError(errorMessage);
                    progressDialog.dismiss();
                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                Log.e("onFailure", t.getMessage() + "");
                progressDialog.dismiss();
                Toast.makeText(SignInActivity.this, t.getMessage() + "", Toast.LENGTH_SHORT).show();
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


}