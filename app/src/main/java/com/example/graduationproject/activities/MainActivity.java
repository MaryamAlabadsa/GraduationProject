package com.example.graduationproject.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.graduationproject.R;
import com.example.graduationproject.databinding.ActivityMainBinding;
import com.example.graduationproject.databinding.LayoutToolbarBinding;
import com.example.graduationproject.fragments.ui.AddPostFragment;
import com.example.graduationproject.fragments.ui.AllPostsFragment;
import com.example.graduationproject.fragments.BaseFragment;
import com.example.graduationproject.fragments.FragmentSwitcher;
import com.example.graduationproject.fragments.ui.NotificationFragment;
import com.example.graduationproject.fragments.PagesFragment;
import com.example.graduationproject.fragments.ui.ProfileFragment;
import com.google.android.material.navigation.NavigationView;
import static com.example.graduationproject.fragments.PagesFragment.ALL_POSTS;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentSwitcher {
    ActivityMainBinding binding;
    Context context = MainActivity.this;
    int category_id;
    DrawerLayout drawer;
    boolean isOpen = false; // by default is false

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LayoutToolbarBinding toolbarBinding = binding.mainToolbar;
//        NavLayoutBinding navLayoutBinding = binding.navLayout;
//     //   NavHeaderBinding navHeaderBinding = NavHeaderBinding.inflate(getLayoutInflater());
        drawer = binding.mainDrawer;

        toolbarBinding.toolbar.setTitle("");
        setSupportActionBar(toolbarBinding.toolbar);

//
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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            switchFragment(PagesFragment.getValue(bundle.getInt("type", 0)), null);
        } else
            switchFragment(ALL_POSTS, null);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.profile:
                switchFragment(PagesFragment.PROFILE, null);
                break;

            case R.id.notifcation:
                switchFragment(PagesFragment.NOTIFICATION, null);
                break;

            case R.id.allPosts:
                switchFragment(ALL_POSTS, null);
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


        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    String tag;

    @Override
    public void switchFragment(PagesFragment pagesFragment, Object object) {
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
                fragment = new ProfileFragment();
                break;
        }
        setTitle(fragment.getFragmentTitle());

        if (ALL_POSTS != pagesFragment) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment, tag).addToBackStack(null).commit();
            } else{
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment, tag).commit();
        }} else{
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment, tag).commit();

        }

    }


    private void requestLogout() {
        Toast.makeText(context, "log out", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



}