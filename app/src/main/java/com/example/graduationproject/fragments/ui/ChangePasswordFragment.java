package com.example.graduationproject.fragments.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.graduationproject.InputFilterMinMax;
import com.example.graduationproject.R;
import com.example.graduationproject.activities.SignInActivity;
import com.example.graduationproject.databinding.FragmentChangePasswordBinding;
import com.example.graduationproject.fragments.BaseFragment;
import com.example.graduationproject.fragments.MyTitleEventBus;
import com.example.graduationproject.fragments.PagesFragment;
import com.example.graduationproject.retrofit.Creator;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.retrofit.change.password.ChangePassword;
import com.example.graduationproject.retrofit.register.RegisterResponse;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Context context;
    FragmentChangePasswordBinding binding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public int getFragmentTitle() {
        return R.string.changePassword;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getActivity();
        EventBus.getDefault().post(new MyTitleEventBus(PagesFragment.CHANGE_PASSWORD, "change password"));

        serviceApi = Creator.getClient().create(ServiceApi.class);
        sharedPreferences = new AppSharedPreferences(getActivity().getApplicationContext());
        token = sharedPreferences.readString(AppSharedPreferences.AUTHENTICATION);
        binding.changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.progressBar.setVisibility(View.VISIBLE);
                String oldPassword = binding.oldPassword.getText().toString();
                String newPassword = binding.newPassword.getText().toString();
                String confirmationPassword = binding.confirmationPassword.getText().toString();
                binding.textErrorNewPassword.setVisibility(View.GONE);
                binding.textErrorOldPassword.setVisibility(View.GONE);
                binding.textErrorConfirmationPassword.setVisibility(View.GONE);
                changePasswordRequest(oldPassword, newPassword, confirmationPassword);

            }
        });

        binding.lottieImg.setAnimation(R.raw.change_password);
        binding.lottieImg.loop(true);
        binding.lottieImg.playAnimation();

        return view;
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
                    parseError(response, binding);
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e("onFailure", call.toString());
            }
        });

    }

    public static void parseError(Response<?> response, FragmentChangePasswordBinding binding) {
        String errorMsg = null;
        try {
            Log.e("start", "ffff");
            assert response.errorBody() != null;
            JSONObject jsonObject = new JSONObject(response.errorBody().string());
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            JSONObject jsonObject2 = jsonArray.getJSONObject(0);
            Log.e("start", jsonObject2.getString("password_confirmation"));

            if (jsonObject2.has("old_password")) {
                JSONArray jsonArrayOldPassword = jsonObject2.getJSONArray("old_password");
                binding.textErrorOldPassword.setText(jsonArrayOldPassword.getString(0));
                binding.textErrorOldPassword.setVisibility(View.VISIBLE);
//                binding.oldPassword.setError(jsonArrayOldPassword.getString(0));
            }
            if (jsonObject2.has("password_confirmation")) {
                JSONArray jsonArrayPasswordConfirmation = jsonObject2.getJSONArray("password_confirmation");
                binding.textErrorConfirmationPassword.setText(jsonArrayPasswordConfirmation.getString(0));
                binding.textErrorConfirmationPassword.setVisibility(View.VISIBLE);
//                binding.confirmationPassword.setError(jsonArrayPasswordConfirmation.getString(0));
            }

            if (jsonObject2.has("password")) {
                JSONArray jsonArrayPassword = jsonObject2.getJSONArray("password");
                binding.textErrorNewPassword.setText(jsonArrayPassword.getString(0));
                binding.textErrorNewPassword.setVisibility(View.VISIBLE);
//                binding.newPassword.setError(jsonArrayPassword.getString(0));
            }

        } catch (Exception e) {
        }

    }
}