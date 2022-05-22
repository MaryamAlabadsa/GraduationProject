package com.example.graduationproject.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.adapters.PostOrdersAdapter;
import com.example.graduationproject.databinding.ActivityMainBinding;
import com.example.graduationproject.databinding.ButtonDialogBinding;
import com.example.graduationproject.databinding.LayoutToolbarBinding;
import com.example.graduationproject.fragments.ChangePasswordFragment;
import com.example.graduationproject.fragments.ui.AddPostFragment;
import com.example.graduationproject.fragments.ui.AllPostsFragment;
import com.example.graduationproject.fragments.BaseFragment;
import com.example.graduationproject.fragments.FragmentSwitcher;
import com.example.graduationproject.fragments.ui.NotificationFragment;
import com.example.graduationproject.fragments.PagesFragment;
import com.example.graduationproject.fragments.ui.PostOrdersFragment;
import com.example.graduationproject.fragments.ui.ProfileFragment;
import com.example.graduationproject.listener.PostOrderRequestInterface;
import com.example.graduationproject.model.PostOrdersInfo;
import com.example.graduationproject.retrofit.Creator;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.retrofit.logout.LogOut;
import com.example.graduationproject.retrofit.post.PostDetails;
import com.example.graduationproject.retrofit.register.RegisterResponse;
import com.example.graduationproject.retrofit.register.User;
import com.example.graduationproject.retrofit.token.MessageResponse;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.example.graduationproject.utils.FileUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;

import static com.example.graduationproject.fragments.PagesFragment.ALL_POSTS;
import static com.example.graduationproject.fragments.PagesFragment.POST_ORDERS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentSwitcher {
    ActivityMainBinding binding;
    ButtonDialogBinding dialogBinding;
    Context context = MainActivity.this;
    int category_id;
    DrawerLayout drawer;
    boolean isOpen = false; // by default is false
    boolean isAllPost = true;
    PagesFragment fragmentSelcted = ALL_POSTS;
    AppSharedPreferences sharedPreferences;
    View headerView;
    LayoutToolbarBinding toolbarBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = new AppSharedPreferences(getApplicationContext());
        serviceApi = Creator.getClient().create(ServiceApi.class);
        token = sharedPreferences.readString(AppSharedPreferences.AUTHENTICATION);
        String post_id_notifaction = getIntent().getStringExtra("post_id");
        if (post_id_notifaction != null) {
            Toast.makeText(context, post_id_notifaction+"", Toast.LENGTH_SHORT).show();
            showDialog();
            getPostDetails(Integer.parseInt(post_id_notifaction));
        }

        toolbarBinding = binding.mainToolbar;
        drawer = binding.mainDrawer;

        toolbarBinding.toolbar.setTitle("");
        setSupportActionBar(toolbarBinding.toolbar);

        binding.toolbarBack.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                toolbarBinding.getRoot().setVisibility(View.VISIBLE);
                binding.toolbarBack.setVisibility(View.INVISIBLE);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                binding.mainDrawer,
                toolbarBinding.toolbar,
                0,
                0);

        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.inflateHeaderView(R.layout.nav_header);

        ImageView userImage = headerView.findViewById(R.id.user_image);
        TextView username = headerView.findViewById(R.id.user_profile);
        String user = sharedPreferences.readUser(AppSharedPreferences.USER);
        Gson gson = new Gson();
        if (!user.isEmpty()) {
            User user1 = gson.fromJson(user, User.class);
            Glide.with(context).load(user1.getImageLink()).circleCrop()
                    .placeholder(R.drawable.ic_launcher_foreground).into(userImage);
            username.setText(user1.getName());
        }
        changeUserImage();

        switchFragment(ALL_POSTS, null);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        isAllPost = false;
        switch (id) {
            case R.id.profile:
                switchFragment(PagesFragment.PROFILE, new PostOrdersInfo(0));
                break;

            case R.id.notifcation:
                switchFragment(PagesFragment.NOTIFICATION, null);
                break;

            case R.id.allPosts:
                switchFragment(ALL_POSTS, null);
                isAllPost = true;
                break;

            case R.id.add_post:
                switchFragment(PagesFragment.ADD_POSTS, null);
                break;

            case R.id.nav_logout:
                requestLogout();
                break;

            case R.id.nav_settings:
//                Intent intent = new Intent(this,
//                        RequestsMassages.class);
//                startActivity(intent);
                break;

            case R.id.nav_change_password:
                switchFragment(PagesFragment.CHANGE_PASSWORD, null);
                break;


        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    String tag;

    @Override
    public void switchFragment(PagesFragment pagesFragment, PostOrdersInfo object) {
        BaseFragment fragment = null;
        switch (pagesFragment) {
            case ADD_POSTS:
                toolbarBinding.getRoot().setVisibility(View.VISIBLE);
                fragment = new AddPostFragment();
                break;
            case ALL_POSTS:
                toolbarBinding.getRoot().setVisibility(View.VISIBLE);
                fragment = new AllPostsFragment();
                break;
            case NOTIFICATION:
                toolbarBinding.getRoot().setVisibility(View.VISIBLE);
                fragment = new NotificationFragment();
                break;
            case PROFILE:
                toolbarBinding.getRoot().setVisibility(View.GONE);
                binding.toolbarBack.setVisibility(View.VISIBLE);
                fragment = ProfileFragment.newInstance(object.getUserId());
                break;
            case POST_ORDERS:
                toolbarBinding.getRoot().setVisibility(View.VISIBLE);
                fragment = PostOrdersFragment.newInstance(object);
                break;
            case CHANGE_PASSWORD:
                toolbarBinding.getRoot().setVisibility(View.VISIBLE);
                fragment = new ChangePasswordFragment();
                break;
        }
        toolbarBinding.tvTitle.setText(fragment.getFragmentTitle());


        if (ALL_POSTS == pagesFragment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment, tag).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment, tag).commit();
        }
    }

    @Override
    public void onBackPressed() {
        switch (fragmentSelcted) {
            case ADD_POSTS:
                binding.navView.getMenu().getItem(1).setChecked(true);
                break;
            case ALL_POSTS:
                binding.navView.getMenu().getItem(0).setChecked(true);
                break;
            case NOTIFICATION:
                binding.navView.getMenu().getItem(2).setChecked(true);
                break;
            case PROFILE:
                binding.navView.getMenu().getItem(3).setChecked(true);
                break;

        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!isAllPost) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                    new AllPostsFragment(),
                    tag).commit();
            isAllPost = true;
        } else {
            super.onBackPressed();
        }
    }

    // change user image
    private void PickImage() {
        CropImage.activity().start(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private boolean checkStoragePermission() {
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        return res2;
    }

    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }

    File file;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri userImg = result.getUri();
                try {
                    InputStream stream = getContentResolver().openInputStream(userImg);
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    ImageView userImage = headerView.findViewById(R.id.user_image);
                    userImage.setImageBitmap(bitmap);
                    file = FileUtil.from(context, userImg);
                    changeUserImageRequest();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void changeUserImage() {
        headerView.findViewById(R.id.edit_user_image).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                boolean pick = true;
                if (pick) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else PickImage();
                } else {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        PickImage();
                        showDialog();
                    }
                }
            }
        });
    }

    private void changeUserImageRequest() {
        MultipartBody.Part body = null;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }
        Call<RegisterResponse> call = serviceApi.updateUserImage("application/json", body, "Bearer " + token);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                Log.d("response code", response.code() + "");
                if (response.isSuccessful() || response.code() == 200) {
                    assert response.body() != null;
                    User user = response.body().getData().getUser();
                    Gson gson = new Gson();
                    String userString = gson.toJson(user);

                    sharedPreferences.writeString(AppSharedPreferences.USER, userString);
                    Log.e("image link", user.getImageLink() + "");
                    Toast.makeText(MainActivity.this, response.message() + "", Toast.LENGTH_SHORT).show();
                } else {
                    String errorMessage = parseError2(response);
                    Toast.makeText(context, errorMessage + "", Toast.LENGTH_SHORT).show();
                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.d("onFailure", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    public String parseError2(Response<?> response) {
        String errorMsg = null;
        try {
            assert response.errorBody() != null;
            JSONObject jsonObject = new JSONObject(response.errorBody().string());
            JSONObject jsonObject2 = jsonObject.getJSONObject("errors");
            JSONArray jsonArray = jsonObject2.getJSONArray("email");
            return jsonArray.getString(0);
        } catch (Exception ignored) {
            Log.e(errorMsg, ignored.getMessage() + "");
            return ignored.getMessage();
        }
    }

    private void requestLogout() {

        Call<LogOut> call = serviceApi.logout("Bearer " + token);
        call.enqueue(new Callback<LogOut>() {

            @Override
            public void onResponse(Call<LogOut> call, Response<LogOut> response) {
                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    sharedPreferences.writeString(AppSharedPreferences.AUTHENTICATION, "");
                    Intent i = new Intent(context, SplashActivity.class);
                    Toast.makeText(context, R.string.session_was_expired_logout_processing, Toast.LENGTH_SHORT).show();
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    deleteDeviceToken();

                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<LogOut> call, Throwable t) {

            }
        });
    }

    private void deleteDeviceToken() {
        Call<MessageResponse> call = serviceApi.sendDeviceToken("", "Bearer " + token);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.d("response1 code", response.code() + "");
                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    private void showBottomSheetDialog(PostDetails postDetails) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context,R.style.MyTransparentBottomSheetDialogTheme);
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

    private void getPostDetails(int post_id) {
        Call<PostDetails> call = serviceApi.getPostDetails(
                "Bearer " + token, post_id);
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