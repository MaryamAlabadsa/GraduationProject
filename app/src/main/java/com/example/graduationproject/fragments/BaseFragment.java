package com.example.graduationproject.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.graduationproject.R;
import com.example.graduationproject.activities.MainActivity;
import com.example.graduationproject.retrofit.Creator;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.retrofit.request.Order;
import com.example.graduationproject.retrofit.token.MessageResponse;
import com.example.graduationproject.utils.AppSharedPreferences;
 import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseFragment extends Fragment {

    public AppSharedPreferences sharedPreferences;
    public String token;
    public ServiceApi serviceApi;
    public ProgressDialog progressDialog;



    @Override
    public void onResume() {
        super.onResume();
        setTitle();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceApi = Creator.getClient().create(ServiceApi.class);
        sharedPreferences = new AppSharedPreferences(getActivity().getApplicationContext());
        token = sharedPreferences.readString(AppSharedPreferences.AUTHENTICATION);

    }

    public abstract int getFragmentTitle();

    public void setTitle() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setTitle(getFragmentTitle());
        }
    }

//    public void //showDialog() {
//
//
//
//
//        progressDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
//        progressDialog.setMessage("Please Wait...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//
//
//    }




}


