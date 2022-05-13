package com.example.graduationproject.fragments.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.graduationproject.databinding.ButtonDialogBinding;
import com.example.graduationproject.databinding.FragmentProfileBinding;
import com.example.graduationproject.fragments.BaseFragment;
import com.example.graduationproject.fragments.FragmentSwitcher;
import com.example.graduationproject.fragments.PagesFragment;
import com.example.graduationproject.listener.CategoryInterface;
import com.example.graduationproject.listener.PostAddOrderInterface;
import com.example.graduationproject.listener.PostRemoveOrderInterface;
import com.example.graduationproject.listener.UserIdtRequestInterface;
import com.example.graduationproject.model.PostOrdersInfo;
import com.example.graduationproject.retrofit.Creator;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.retrofit.post.Post;
import com.example.graduationproject.retrofit.profile.donation.posts.PostsList;
import com.example.graduationproject.retrofit.profile.donation.posts.ProfilePosts;
import com.example.graduationproject.retrofit.profile.user.info.UserProfileInfo;
import com.example.graduationproject.retrofit.register.User;
import com.example.graduationproject.retrofit.request.Order;
import com.example.graduationproject.retrofit.token.MessageResponse;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
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
    private FragmentSwitcher fragmentSwitcher;
    BottomSheetDialog dialog;


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
        dialogBinding = ButtonDialogBinding.inflate(inflater, container, false);
        context = getActivity();
        dialog = new BottomSheetDialog(context);
//        showDialog();
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
                    progressDialog.dismiss();


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
        ProfilePostsAdapter adapter = new ProfilePostsAdapter(context, new PostAddOrderInterface() {
            @Override
            public void layout(Post post) {
                if (post.getIsHeTheOwnerOfThePost()) {
                    PostOrdersInfo info = new PostOrdersInfo(post.getId(),
                            post.getIsCompleted(),
                            post.getIsDonation(),
                            post.getSecondUserId());
                    fragmentSwitcher.switchFragment(PagesFragment.POST_ORDERS, info);
                } else{
                    createDialog(post.getId());
                    dialog.show();
                }

            }
        }, new PostRemoveOrderInterface() {
            @Override
            public void layout(Post post) {
                RemoveRequest(post.getOrderId());
            }
        }, new UserIdtRequestInterface() {
            @Override
            public void layout(int userId) {
                fragmentSwitcher.switchFragment(PagesFragment.PROFILE, new PostOrdersInfo(userId));
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
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentSwitcher = (FragmentSwitcher) context;

    }

    private void AddRequest(int id_post, String massage, Dialog dialog) {

        RequestBody post_id = RequestBody.create(MediaType.parse("multipart/form-data"), id_post + "");
        RequestBody mPost = RequestBody.create(MediaType.parse("multipart/form-data"), massage);

        Call<Order> call = serviceApi.addRequest(
                "Bearer " + token
                , post_id
                , mPost);

        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Log.d("response5 code", response.code() + "");
                dialog.dismiss();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {

            }
        });
    }

    private void RemoveRequest(int order_id) {

        Call<MessageResponse> call = serviceApi.deleteOrder(order_id,
                "Bearer " + token
        );

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.d("response5 code", response.code() + "");
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {

            }
        });
    }

    private void createDialog(int id) {
        View view = dialogBinding.getRoot();
        dialogBinding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String massage = dialogBinding.editComment.getText().toString();
                AddRequest(id, massage, dialog);
            }
        });
        dialog.setContentView(dialogBinding.getRoot());
    }
}