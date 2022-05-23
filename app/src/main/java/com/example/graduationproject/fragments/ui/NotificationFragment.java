package com.example.graduationproject.fragments.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.adapters.CategoryAdapter;
import com.example.graduationproject.adapters.NotificationAdapter;
import com.example.graduationproject.adapters.PostOrdersAdapter;
import com.example.graduationproject.databinding.FragmentAllPostsBinding;
import com.example.graduationproject.databinding.FragmentNotificationBinding;
import com.example.graduationproject.fragments.BaseFragment;
import com.example.graduationproject.fragments.PagesFragment;
import com.example.graduationproject.listener.CategoryInterface;
import com.example.graduationproject.listener.NotificationInterface;
import com.example.graduationproject.listener.PostOrderRequestInterface;
import com.example.graduationproject.model.PostOrdersInfo;
import com.example.graduationproject.retrofit.notifiction.Datum;
import com.example.graduationproject.retrofit.notifiction.Notification;
import com.example.graduationproject.retrofit.post.AllPosts;
import com.example.graduationproject.retrofit.post.PostDetails;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        showDialog();
        getNotification();
        return view;
    }


    private void getNotification() {

        Call<Notification> call = serviceApi.getNotification(
                "Bearer " + token);
        call.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                Log.d("response code", response.code() + "");

                if (response.isSuccessful()) {
                    Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                    Log.d("Success", new Gson().toJson(response.body()));
                    binding.tv.setVisibility(View.GONE);
                    binding.rvNotification.setVisibility(View.VISIBLE);
                    setNotificationRv(response.body().getData());
                    progressDialog.dismiss();

                } else {
                    progressDialog.dismiss();
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
                    showDialog();
                    getPostDetails(postId);
                } else if (userId != null) {
//                    switchFragment(PagesFragment.PROFILE, new PostOrdersInfo(Integer.parseInt(user_id_notifaction)));
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

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    private void showBottomSheetDialog(PostDetails postDetails) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.MyTransparentBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.layout_post_detials);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //
        ImageView userImage = bottomSheetDialog.findViewById(R.id.user_image);
        TextView userName = bottomSheetDialog.findViewById(R.id.user_name_text);
        TextView postDate = bottomSheetDialog.findViewById(R.id.post_date_text);
        TextView postRequestText = bottomSheetDialog.findViewById(R.id.post_request_text);
        TextView postTitleText = bottomSheetDialog.findViewById(R.id.post_title_text);
        TextView postDescriptionText = bottomSheetDialog.findViewById(R.id.post_description_text);
        TextView orderText = bottomSheetDialog.findViewById(R.id.order_text);
        Button completeBtn = bottomSheetDialog.findViewById(R.id.complete_btn);
        Button pendingBtn = bottomSheetDialog.findViewById(R.id.pending_btn);
        Button donationBtn = bottomSheetDialog.findViewById(R.id.donation_btn);
        Button requestBtn = bottomSheetDialog.findViewById(R.id.request_btn);
        RecyclerView orderRv = bottomSheetDialog.findViewById(R.id.order_rv);
        //set data
        Glide.with(context).load(postDetails.getPost().getFirstUserImageLink()).circleCrop()
                .placeholder(R.drawable.ic_launcher_foreground).into(userImage);
        userName.setText(postDetails.getPost().getFirstUserName());
        postDate.setText(postDetails.getPost().getPublishedAt());
        postRequestText.setText(postDetails.getPost().getNumberOfRequests() + " requests");
        postTitleText.setText(postDetails.getPost().getTitle());
        postDescriptionText.setText(postDetails.getPost().getDescription() + "");
        if (postDetails.getPost().getIsCompleted()) {
            completeBtn.setBackground(context.getDrawable(R.drawable.button_complete2));
            completeBtn.setTextColor(context.getColor(R.color.white));
            pendingBtn.setBackground(context.getDrawable(R.drawable.button_pending));
            pendingBtn.setTextColor(context.getColor(R.color.red));
        } else {
            completeBtn.setBackground(context.getDrawable(R.drawable.button_complete));
            completeBtn.setTextColor(context.getColor(R.color.green));
            pendingBtn.setBackground(context.getDrawable(R.drawable.button_pending2));
            pendingBtn.setTextColor(context.getColor(R.color.white));
        }
        if (postDetails.getPost().getIsDonation()) {
            donationBtn.setBackground(context.getDrawable(R.drawable.button_profile1));
            donationBtn.setTextColor(context.getColor(R.color.white));
            requestBtn.setBackground(context.getDrawable(R.drawable.button_profile2));
            requestBtn.setTextColor(context.getColor(R.color.blue));
        } else {
            donationBtn.setBackground(context.getDrawable(R.drawable.button_profile2));
            donationBtn.setTextColor(context.getColor(R.color.blue));
            requestBtn.setBackground(context.getDrawable(R.drawable.button_profile1));
            requestBtn.setTextColor(context.getColor(R.color.white));
        }
        if (postDetails.getPost().getIsHeTheOwnerOfThePost()) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                    context, RecyclerView.VERTICAL, false);
            orderRv.setLayoutManager(layoutManager);

            PostOrdersAdapter adapter = new PostOrdersAdapter(context, new PostOrderRequestInterface() {
                @Override
                public void layout(int userId) {

                }
            }, postDetails.getPost().getIsCompleted(), postDetails.getPost().getIsDonation(), postDetails.getPost().getSecondUserId());
            adapter.setList(postDetails.getOrders());
            orderRv.setAdapter(adapter);
            orderText.setVisibility(View.GONE);

        }
        progressDialog.dismiss();
        bottomSheetDialog.show();
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
                    showBottomSheetDialog(postDetails);
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


}