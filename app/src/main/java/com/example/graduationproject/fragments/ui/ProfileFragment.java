package com.example.graduationproject.fragments.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.databinding.FragmentProfileBinding;
import com.example.graduationproject.fragments.BaseFragment;
import com.example.graduationproject.retrofit.Creator;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.retrofit.post.AllPosts;
import com.example.graduationproject.retrofit.register.User;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends BaseFragment {
    Context context;
    FragmentProfileBinding binding;
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private int userId;

    public ProfileFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(int param1) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt(ARG_PARAM1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getActivity();
        serviceApi = Creator.getClient().create(ServiceApi.class);
        sharedPreferences = new AppSharedPreferences(getActivity().getApplicationContext());
        token = sharedPreferences.readString(AppSharedPreferences.AUTHENTICATION);

        String user = sharedPreferences.readUser(AppSharedPreferences.USER);
        Gson gson = new Gson();
        if (!user.isEmpty()) {
            User user1 = gson.fromJson(user, User.class);
            Glide.with(context).load(user1.getImageLink()).circleCrop()
                    .placeholder(R.drawable.ic_launcher_foreground).into(binding.profileImage);
            binding.fullName.setText(user1.getName());

        }

        return view;
    }

    private void getUserProfileRequest() {
        if (userId==0){

        }
    }
    private void getMyProfileRequest() {
        Call<AllPosts> call = serviceApi.getMyProfileRequestsPosts("Bearer " + token);
        call.enqueue(new Callback<AllPosts>() {
            @Override
            public void onResponse(Call<AllPosts> call, Response<AllPosts> response) {
                Log.d("response1 code", response.code() + "");

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                
                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<AllPosts> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    @Override
    public int getFragmentTitle() {
        return R.string.see_your_profile;
    }
}