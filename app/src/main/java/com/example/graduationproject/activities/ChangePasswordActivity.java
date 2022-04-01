package com.example.graduationproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.databinding.ActivityChangePasswordBinding;
import com.example.graduationproject.databinding.ActivityMainBinding;
import com.example.graduationproject.retrofit.Creator;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.retrofit.change.password.ChangePassword;
import com.example.graduationproject.retrofit.register.RegisterResponse;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {
    ActivityChangePasswordBinding binding;
    Context context = ChangePasswordActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        serviceApi = Creator.getClient().create(ServiceApi.class);
        sharedPreferences = new AppSharedPreferences(getApplicationContext());
        token = sharedPreferences.readString(AppSharedPreferences.AUTHENTICATION);

        binding.changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.progressBar.setVisibility(View.VISIBLE);
                String oldPassword = binding.oldPassword.getText().toString();
                String newPassword = binding.newPassword.getText().toString();
                String confirmationPassword = binding.confirmationPassword.getText().toString();
                if (oldPassword != null) {
                    if (newPassword != null) {
                        if (confirmationPassword != null) {
                            changePasswordRequest(oldPassword, newPassword, confirmationPassword);
                        } else
                            binding.confirmationPassword.setError("this field is required.");
                    } else
                        binding.newPassword.setError("this field is required.");
                } else
                    binding.oldPassword.setError("this field is required.");

            }
        });
    }

    private void changePasswordRequest(String oldPassword, String password, String passwordConfirmation) {
        ChangePassword changePassword = new ChangePassword();
        changePassword.setOldPassword(oldPassword);
        changePassword.setPassword(password);
        changePassword.setPasswordConfirmation(passwordConfirmation);
        Call<RegisterResponse> call = serviceApi.changePassword(
                "Bearer " + token, changePassword);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    Log.e("Success", new Gson().toJson(response.body()));
                    startActivity(new Intent(context, SignInActivity.class));
                    binding.progressBar.setVisibility(View.GONE);

                } else {
                    String errorMessage = parseError(response);
                    Toast.makeText(context, errorMessage + "", Toast.LENGTH_SHORT).show();
                    if (errorMessage == "old password do not matched")
                        binding.oldPassword.setError(errorMessage);
                    else if (errorMessage == "The password confirmation and password must match.") {
                        binding.newPassword.setError(errorMessage);
                        binding.confirmationPassword.setError(errorMessage);

                    }
                    binding.progressBar.setVisibility(View.GONE);

                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e("onFailure", call.toString());
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


}