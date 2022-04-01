package com.example.graduationproject.fragments;


import androidx.fragment.app.Fragment;

import com.example.graduationproject.activities.MainActivity;
import com.example.graduationproject.retrofit.Creator;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.utils.AppSharedPreferences;

public abstract class BaseFragment extends Fragment {

    AppSharedPreferences sharedPreferences;
    String token;
    ServiceApi serviceApi;

    @Override
    public void onResume() {
        super.onResume();
        setTitle();
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

}

