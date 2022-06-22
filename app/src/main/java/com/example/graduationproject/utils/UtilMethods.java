package com.example.graduationproject.utils;

import static com.example.graduationproject.activities.BaseActivity.parseError;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.lottiedialog.LottieDialog;
import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.adapters.PostOrdersAdapter;
import com.example.graduationproject.listener.PostOrderRequestInterface;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.retrofit.post.PostDetails;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UtilMethods {
    private static LottieDialog dialog;
//    LottieDialog dialog;
    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    public static   void showBottomSheetDialog(PostDetails postDetails, Context context) {

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
            pendingBtn.setVisibility(View.GONE);
        } else {
            completeBtn.setVisibility(View.GONE);
            pendingBtn.setBackground(context.getDrawable(R.drawable.button_pending2));
            pendingBtn.setTextColor(context.getColor(R.color.white));
        }
        if (postDetails.getPost().getIsDonation()) {
            donationBtn.setBackground(context.getDrawable(R.drawable.button_profile1));
            donationBtn.setTextColor(context.getColor(R.color.white));
            requestBtn.setVisibility(View.GONE);
        } else {
            donationBtn.setVisibility(View.GONE);
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
        bottomSheetDialog.show();
    }

    public static void getPostDetails(int post_id, Context context, ServiceApi serviceApi,String token) {
        Call<PostDetails> call = serviceApi.getPostDetails("Bearer " + token, post_id);
        call.enqueue(new Callback<PostDetails>() {
            @Override
            public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                Log.d("response code", response.code() + "");
                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    PostDetails postDetails = response.body();
                    showBottomSheetDialog(postDetails,context);
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

    public static boolean isLogin(Context context) {
        AppSharedPreferences sharedPreferences = new AppSharedPreferences(context);

        String userToken = sharedPreferences.readString(AppSharedPreferences.AUTHENTICATION);

        return !userToken.equals("");
    }
    public static void launchLoadingLottieDialog(Context context) {
         dialog = new LottieDialog(context)
                .setAnimation(R.raw.heart_loading).setAnimationViewHeight(1000).setAnimationViewWidth(1000)
                .setAutoPlayAnimation(true)
                .setDialogHeight(500)
                .setAnimationRepeatCount(LottieDialog.INFINITE)
                .setMessage("Loading...").setCancelable(false);
        dialog.show();
    }
    public static void launchLoadingLottieDialogDismiss(Context context) {
        dialog.dismiss();
    }
    public static void launchInternetLottieDialog(Context context) {
        Button button = new Button(context);
        button.setText("Retry");
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        int purpleColor = ContextCompat.getColor(context, R.color.green);
        button.setBackgroundTintList(ColorStateList.valueOf(purpleColor));

        LottieDialog dialog = new LottieDialog(context)
                .setAnimation(R.raw.no_internet)
                .setAutoPlayAnimation(true)
                .setAnimationRepeatCount(LottieDialog.INFINITE)
                .setMessage("You have no internet connection")
                .addActionButton(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                checkNetwork(context);
            }
        });

        dialog.show();
    }
    public static void checkNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//            Toast.makeText(context, "connected", Toast.LENGTH_SHORT).show();
//            connected = true;
        } else {
//            Toast.makeText(context, "connected", Toast.LENGTH_SHORT).show();
            launchInternetLottieDialog(context);
//            connected = false;

        }
    }

}
