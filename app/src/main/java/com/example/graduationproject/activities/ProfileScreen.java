package com.example.graduationproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.models.ChangePassResponse;
import com.example.graduationproject.requests.ChangePassBody;
import com.example.graduationproject.retrofit.Service;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

<<<<<<< HEAD:app/src/main/java/com/example/graduationproject/activities/ConfirmPasswordActivity.java
public class ConfirmPasswordActivity extends AppCompatActivity {
    private EditText editTextCurrentPassword, editTextNewPassword, editTextConfirmPassword;
    private Button changePassBtn;
=======
public class ProfileScreen extends AppCompatActivity {
>>>>>>> origin/razan2:app/src/main/java/com/example/graduationproject/activities/ProfileScreen.java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD:app/src/main/java/com/example/graduationproject/activities/ConfirmPasswordActivity.java
        setContentView(R.layout.activity_confirm_password);
        editTextCurrentPassword = findViewById(R.id.editTextCurrentPassword);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        changePassBtn = findViewById(R.id.confirmBtn);

        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });

    }

    private void updatePassword() {
        String currentPassword = editTextCurrentPassword.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (currentPassword.isEmpty()) {
            editTextCurrentPassword.setError("Password required");
            editTextCurrentPassword.requestFocus();
            return;
        }

        if (newPassword.isEmpty()) {
            editTextNewPassword.setError("Enter new password");
            editTextNewPassword.requestFocus();
            return;
        }
        if (confirmPassword.isEmpty()) {
            editTextConfirmPassword.setError("Enter new password");
            editTextConfirmPassword.requestFocus();
            return;
        }

        ChangePassBody changePassBody = new ChangePassBody(currentPassword, newPassword, confirmPassword);
        Service.RetrofitClient.getRetrofitInstance().changePassword(changePassBody)
                .enqueue(new Callback<ChangePassResponse>() {
                    @Override
                    public void onResponse(Call<ChangePassResponse> call, Response<ChangePassResponse> response) {
                        Toast.makeText(ConfirmPasswordActivity.this, "Password changed ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ChangePassResponse> call, Throwable t) {
                        Log.e("onFailure", call.toString());
                        t.printStackTrace();
                    }
                });
=======
        setContentView(R.layout.activity_profile_screen);
>>>>>>> origin/razan2:app/src/main/java/com/example/graduationproject/activities/ProfileScreen.java
    }
}
