package com.example.graduationproject.fragments.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.adapters.CategoryAdapter;
import com.example.graduationproject.adapters.NotificationAdapter;
import com.example.graduationproject.adapters.PostOrdersAdapter;
import com.example.graduationproject.databinding.FragmentAllPostsBinding;
import com.example.graduationproject.databinding.FragmentNotificationBinding;
import com.example.graduationproject.fragments.BaseFragment;
import com.example.graduationproject.fragments.MyTitleEventBus;
import com.example.graduationproject.fragments.PagesFragment;
import com.example.graduationproject.listener.CategoryInterface;
import com.example.graduationproject.listener.NotificationInterface;
import com.example.graduationproject.listener.PostOrderRequestInterface;
import com.example.graduationproject.model.PostOrdersInfo;
import com.example.graduationproject.retrofit.notifiction.Datum;
import com.example.graduationproject.retrofit.notifiction.Notification;
import com.example.graduationproject.retrofit.post.AllPosts;
import com.example.graduationproject.retrofit.post.PostDetails;
import com.example.graduationproject.utils.UtilMethods;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends BaseFragment {

    public static final String TAG = "NOTIFICATION";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FragmentNotificationBinding binding;
    Context context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getActivity();
        swipeToRefresh();
        //event bus
        EventBus.getDefault().post(new MyTitleEventBus(PagesFragment.ALL_POSTS, TAG));
        showWaitingImage();
        getNotification();
        return view;
    }

    private void swipeToRefresh() {
        SwipeRefreshLayout swipeRefreshLayout = binding.scroll;
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
                showWaitingImage();
                getNotification();  
            }, 1000);
        });
    }

    private void getNotification() {

        Call<Notification> call = serviceApi.getNotification(
                "Bearer " + token);
        call.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                Log.d("response code", response.code() + "");

                if (response.isSuccessful()) {
                    dismissWaitingImage();

                    Log.d("Success", new Gson().toJson(response.body()));
                    if (response.body().getData().isEmpty()) {
                        showNotificationImage();
                    } else {

                        binding.rvNotification.setVisibility(View.VISIBLE);
                        setNotificationRv(response.body().getData());
                    }

                    //progressDialog.dismiss();


                } else {
                    //progressDialog.dismiss();


                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(context, errorMessage + "", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                Toast.makeText(context, t.getMessage() + "3", Toast.LENGTH_SHORT).show();

                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }


    public static String parseError(Response<?> response) {
        //function that parse error from api in case response code!=200
        String errorMsg = null;
        try {
            JSONObject jObjError = new JSONObject(response.errorBody().string());
            errorMsg = jObjError.getString("message");
            return errorMsg;
        } catch (Exception e) {
        }
        return errorMsg;
    }


    private void setNotificationRv(List<Datum> list) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                context, RecyclerView.VERTICAL, false);
        binding.rvNotification.setLayoutManager(layoutManager);
        NotificationAdapter adapter = new NotificationAdapter(context, new NotificationInterface() {
            @Override
            public void layout(String postId, String userId) {
                if (postId != null) {
                    UtilMethods.launchLoadingLottieDialog(context);
                    getPostDetails(postId);
                }
            }
        });
        adapter.setList(list);
        binding.rvNotification.setAdapter(adapter);
    }

    @Override
    public int getFragmentTitle() {
        return R.string.notification;
    }

    private void getPostDetails(String post_id) {
        Call<PostDetails> call = serviceApi.getPostDetails(
                "Bearer " + token, Integer.parseInt(post_id));
        call.enqueue(new Callback<PostDetails>() {
            @Override
            public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                Log.d("response code", response.code() + "");
                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    PostDetails postDetails = response.body();
                    UtilMethods.launchLoadingLottieDialogDismiss(context);
                    UtilMethods.showBottomSheetDialog(postDetails, context, serviceApi, postDetails.getPost().getId(), token, null);
                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<PostDetails> call, Throwable t) {
                Log.d("onFailure", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    private void showWaitingImage() {
        binding.lottieImg.setAnimation(R.raw.loading_pink);
        binding.lottieImg.loop(true);
        binding.lottieImg.playAnimation();
        binding.lottieImg.setVisibility(View.VISIBLE);
        binding.rvNotification.setVisibility(View.GONE);
    }

    private void showNotificationImage() {
        binding.lottieImg.setAnimation(R.raw.no_notifaiction);
        binding.lottieImg.loop(true);
        binding.lottieImg.playAnimation();
        binding.lottieImg.setVisibility(View.VISIBLE);
        binding.rvNotification.setVisibility(View.GONE);
    }

    private void dismissWaitingImage() {
        binding.lottieImg.setVisibility(View.GONE);
        binding.rvNotification.setVisibility(View.VISIBLE);
    }
}