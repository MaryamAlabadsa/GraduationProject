package com.example.graduationproject.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.graduationproject.R;
import com.example.graduationproject.retrofit.Creator;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.utils.AppSharedPreferences;

import org.json.JSONObject;

import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {
    AppSharedPreferences sharedPreferences;
    String token,lang;
    static ServiceApi serviceApi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        serviceApi = Creator.getClient().create(ServiceApi.class);
        sharedPreferences = new AppSharedPreferences(getApplicationContext());
        token = sharedPreferences.readString(AppSharedPreferences.AUTHENTICATION);
        lang = sharedPreferences.readString(AppSharedPreferences.LANG);

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
