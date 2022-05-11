package com.example.graduationproject.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.databinding.ActivityMainBinding;
import com.example.graduationproject.databinding.ButtonDialogBinding;
import com.example.graduationproject.databinding.LayoutToolbarBinding;
import com.example.graduationproject.fragments.ui.AddPostFragment;
import com.example.graduationproject.fragments.ui.AllPostsFragment;
import com.example.graduationproject.fragments.BaseFragment;
import com.example.graduationproject.fragments.FragmentSwitcher;
import com.example.graduationproject.fragments.ui.NotificationFragment;
import com.example.graduationproject.fragments.PagesFragment;
import com.example.graduationproject.fragments.ui.PostOrdersFragment;
import com.example.graduationproject.fragments.ui.ProfileFragment;
import com.example.graduationproject.model.PostOrdersInfo;
import com.example.graduationproject.retrofit.Creator;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.retrofit.logout.LogOut;
import com.example.graduationproject.retrofit.register.User;
import com.example.graduationproject.retrofit.token.MessageResponse;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import static com.example.graduationproject.fragments.PagesFragment.ALL_POSTS;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = new AppSharedPreferences(getApplicationContext());
        serviceApi = Creator.getClient().create(ServiceApi.class);
        token = sharedPreferences.readString(AppSharedPreferences.AUTHENTICATION);


        LayoutToolbarBinding toolbarBinding = binding.mainToolbar;
        drawer = binding.mainDrawer;

        toolbarBinding.toolbar.setTitle("");
        setSupportActionBar(toolbarBinding.toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.mainDrawer, toolbarBinding.toolbar, 0, 0);

        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);
//        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

//
        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header);

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

//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            switchFragment(PagesFragment.getValue(bundle.getInt("type", 0)), null);
//        } else
        switchFragment(ALL_POSTS, null);
        Toast.makeText(context, token + "token ", Toast.LENGTH_SHORT).show();


    }


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
//                        SettingsActivity.class);
//                startActivity(intent);
                break;

            case R.id.nav_change_password:
                Intent intent = new Intent(this,
                        ChangePasswordActivity.class);
                startActivity(intent);
                break;


        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    String tag;

    @Override
    public void switchFragment(PagesFragment pagesFragment, PostOrdersInfo object) {
        BaseFragment fragment = null;
//         tag = AllPostsFragment.TAG;
        switch (pagesFragment) {
            case ADD_POSTS:
                fragment = new AddPostFragment();
                break;
            case ALL_POSTS:
//                tag = AllPostsFragment.TAG;
                fragment = new AllPostsFragment();
                break;
            case NOTIFICATION:
                fragment = new NotificationFragment();
                break;
            case PROFILE:
                fragment = ProfileFragment.newInstance(object.getUserId());
                break;
            case POST_ORDERS:
                fragment = PostOrdersFragment.newInstance(object);
                break;
        }
        setTitle(fragment.getFragmentTitle());

        if (ALL_POSTS == pagesFragment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment, tag).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment, tag).commit();
        }
    }

    private void requestLogout() {

        Call<LogOut> call = serviceApi.logout("Bearer " + token);
        call.enqueue(new Callback<LogOut>() {

            @Override
            public void onResponse(Call<LogOut> call, Response<LogOut> response) {
                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    LogOut logOut = response.body();
                    sharedPreferences.writeString(AppSharedPreferences.AUTHENTICATION, "");
                    sharedPreferences.clear();
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

    @Override
    public void onBackPressed() {
        switch (fragmentSelcted) {
            case ADD_POSTS:
                binding.navView.getMenu().getItem(1).setChecked(true);
                break;
            case ALL_POSTS:
//                tag = AllPostsFragment.TAG;
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
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new AllPostsFragment(), tag).commit();
            isAllPost = true;
        } else {
            super.onBackPressed();
        }
    }


}