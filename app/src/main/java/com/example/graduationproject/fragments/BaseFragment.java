package com.example.graduationproject.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.graduationproject.activities.MainActivity;
import com.example.graduationproject.retrofit.Creator;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.utils.AppSharedPreferences;

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
    public void showDialog(){

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
}



//package com.example.graduationproject.fragments;
//
//
//import androidx.fragment.app.Fragment;
//
//import com.example.graduationproject.activities.MainActivity;
//import com.example.graduationproject.retrofit.Creator;
//import com.example.graduationproject.retrofit.ServiceApi;
//import com.example.graduationproject.utils.AppSharedPreferences;
//
//public abstract class BaseFragment extends Fragment {
//
//    public AppSharedPreferences sharedPreferences;
//    public String token;
//    public ServiceApi serviceApi;
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        setTitle();
//        serviceApi = Creator.getClient().create(ServiceApi.class);
//        sharedPreferences = new AppSharedPreferences(getActivity().getApplicationContext());
//        token = sharedPreferences.readString(AppSharedPreferences.AUTHENTICATION);
//
//    }
//
//    public abstract int getFragmentTitle();
//
//    public void setTitle() {
//        if (getActivity() instanceof MainActivity) {
//            ((MainActivity) getActivity()).setTitle(getFragmentTitle());
//        }
//    }
//
//}
//
