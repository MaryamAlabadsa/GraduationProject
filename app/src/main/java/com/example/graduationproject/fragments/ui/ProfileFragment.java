package com.example.graduationproject.fragments.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.adapters.CategoryAdapter;
import com.example.graduationproject.adapters.ProfilePostsAdapter;
import com.example.graduationproject.databinding.FragmentProfileBinding;
import com.example.graduationproject.fragments.BaseFragment;
import com.example.graduationproject.listener.CategoryInterface;
import com.example.graduationproject.listener.PostRequestInterface;
import com.example.graduationproject.listener.UserIdtRequestInterface;
import com.example.graduationproject.retrofit.Creator;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.retrofit.post.Post;
import com.example.graduationproject.retrofit.profile.donation.posts.PostsList;
import com.example.graduationproject.retrofit.profile.donation.posts.ProfilePosts;
import com.example.graduationproject.retrofit.profile.user.info.UserProfileInfo;
import com.example.graduationproject.retrofit.register.User;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener {
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
        getProfileData();
        binding.btnDonationPost.setOnClickListener(this::onClick);
        binding.btnRequestPost.setOnClickListener(this::onClick);

        return view;
    }

    private void getProfileData() {
        showDialog();
        if (userId == 0) {
            setMyProfileInfo();
            getMyRequestPosts();
        } else {
            setUserProfileInfo();
            getMyRequestPosts();
        }
    }


    private void setMyProfileInfo() {

        Call<UserProfileInfo> call = serviceApi.getMyProfileInfo("Bearer " + token);
        call.enqueue(new Callback<UserProfileInfo>() {
            @Override
            public void onResponse(Call<UserProfileInfo> call, Response<UserProfileInfo> response) {
                Log.d("response1 code", response.code() + "");

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    Glide.with(context).load(response.body().getData().getUser().getImageLink()).circleCrop()
                            .placeholder(R.drawable.ic_launcher_foreground).into(binding.profileImage);
                    binding.fullName.setText(response.body().getData().getUser().getName());
                    binding.tvDonationPostsNum.setText(response.body().getData().getNumDonationPost() + "");
                    binding.tvRequestPostsNum.setText(response.body().getData().getNumRequestPost() + "");


                } else {
//                    String errorMessage
//                    = parseError(response);
//                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<UserProfileInfo> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    private void setUserProfileInfo() {

        Call<UserProfileInfo> call = serviceApi.getUserProfileInfo(userId, "Bearer " + token);
        call.enqueue(new Callback<UserProfileInfo>() {
            @Override
            public void onResponse(Call<UserProfileInfo> call, Response<UserProfileInfo> response) {
                Log.d("response1 code", response.code() + "");

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    Glide.with(context).load(response.body().getData().getUser().getImageLink()).circleCrop()
                            .placeholder(R.drawable.ic_launcher_foreground).into(binding.profileImage);
                    binding.fullName.setText(response.body().getData().getUser().getName());
                    binding.tvDonationPostsNum.setText(response.body().getData().getNumDonationPost() + "");
                    binding.tvRequestPostsNum.setText(response.body().getData().getNumRequestPost() + "");


                } else {
//                    String errorMessage
//                    = parseError(response);
//                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<UserProfileInfo> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    private void getMyRequestPosts() {
        Call<ProfilePosts> call = serviceApi.getMyRequestsPosts("Bearer " + token);
        call.enqueue(new Callback<ProfilePosts>() {
            @Override
            public void onResponse(Call<ProfilePosts> call, Response<ProfilePosts> response) {
                Log.d("response1 code", response.code() + "");

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    setRvData(response.body().getData().getPostsList());
                    progressDialog.dismiss();
                    binding.getRoot().setVisibility(View.VISIBLE);

                } else {
//                    String errorMessage
//                    = parseError(response);
//                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<ProfilePosts> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    private void getMyDonationPosts() {
        Call<ProfilePosts> call = serviceApi.getMyDonationPosts("Bearer " + token);
        call.enqueue(new Callback<ProfilePosts>() {
            @Override
            public void onResponse(Call<ProfilePosts> call, Response<ProfilePosts> response) {
                Log.d("response1 code", response.code() + "");

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    setRvData(response.body().getData().getPostsList());
                    progressDialog.dismiss();
                    binding.getRoot().setVisibility(View.VISIBLE);

                } else {
//                    String errorMessage
//                    = parseError(response);
//                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<ProfilePosts> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    private void getUserRequestPosts(int userId) {
        Call<ProfilePosts> call = serviceApi.getUserRequestPosts(userId, "Bearer " + token);
        call.enqueue(new Callback<ProfilePosts>() {
            @Override
            public void onResponse(Call<ProfilePosts> call, Response<ProfilePosts> response) {
                Log.d("response1 code", response.code() + "");

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    setRvData(response.body().getData().getPostsList());
                    progressDialog.dismiss();
                    binding.getRoot().setVisibility(View.VISIBLE);

                } else {
//                    String errorMessage
//                    = parseError(response);
//                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<ProfilePosts> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    private void getUserDonationPosts(int userId) {
        Call<ProfilePosts> call = serviceApi.getUserDonationPosts(userId, "Bearer " + token);
        call.enqueue(new Callback<ProfilePosts>() {
            @Override
            public void onResponse(Call<ProfilePosts> call, Response<ProfilePosts> response) {
                Log.d("response1 code", response.code() + "");

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    setRvData(response.body().getData().getPostsList());
                    progressDialog.dismiss();
                    binding.getRoot().setVisibility(View.VISIBLE);

                } else {
//                    String errorMessage
//                    = parseError(response);
//                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<ProfilePosts> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    private void setRvData(List<PostsList> data) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                context, RecyclerView.VERTICAL, false);
        binding.rvPosts.setLayoutManager(layoutManager);
        ProfilePostsAdapter adapter = new ProfilePostsAdapter(context, new PostRequestInterface() {
            @Override
            public void layout(Post post) {

            }
        }, new UserIdtRequestInterface() {
            @Override
            public void layout(int userId) {

            }
        });
        adapter.setList(data);
        binding.rvPosts.setAdapter(adapter);
    }

    @Override
    public int getFragmentTitle() {
        return R.string.see_your_profile;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //when user click rating button
            case R.id.btn_donation_post:
                if (userId == 0)
                    getMyDonationPosts();
                else
                    getUserDonationPosts(userId);
                break;

            case R.id.btn_request_post:
                if (userId == 0)
                    getMyRequestPosts();
                else
                    getUserRequestPosts(userId);
                break;
        }
    }
}