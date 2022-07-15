package com.example.graduationproject.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.amrdeveloper.lottiedialog.LottieDialog;
import com.bumptech.glide.Glide;
import com.claudiodegio.msv.BaseMaterialSearchView;
import com.claudiodegio.msv.OnSearchViewListener;
import com.example.graduationproject.R;
import com.example.graduationproject.databinding.ActivityMainBinding;
import com.example.graduationproject.databinding.LayoutToolbarBinding;
import com.example.graduationproject.fragments.ui.ChangePasswordFragment;
import com.example.graduationproject.fragments.MyTitleEventBus;
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
import com.example.graduationproject.utils.AppSharedPreferences;
import com.example.graduationproject.utils.UtilMethods;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import static com.example.graduationproject.fragments.PagesFragment.ADD_POSTS;
import static com.example.graduationproject.fragments.PagesFragment.ALL_POSTS;
import static com.example.graduationproject.fragments.PagesFragment.CHANGE_PASSWORD;
import static com.example.graduationproject.fragments.PagesFragment.EDIT;
import static com.example.graduationproject.fragments.PagesFragment.NOTIFICATION;
import static com.example.graduationproject.fragments.PagesFragment.PROFILE;
import static com.example.graduationproject.fragments.PagesFragment.SEARCH;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentSwitcher {
    private static final String TAG = "MainActivity";
    ActivityMainBinding binding;
    Context context = MainActivity.this;
    int category_id;
    DrawerLayout drawer;
    boolean isOpen = false; // by default is false
    boolean isAllPost = true;
    PagesFragment fragmentSelcted = ALL_POSTS;
    AppSharedPreferences sharedPreferences;
    View headerView;
    LayoutToolbarBinding toolbarBinding;
    private boolean connected;
    Bitmap bitmap;
    Switch langSwitch;
    String tag;
    BaseFragment fragment = null;
    private String currentLanguage;
    private Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = new AppSharedPreferences(getApplicationContext());
        serviceApi = Creator.getClient().create(ServiceApi.class);
        token = sharedPreferences.readString(AppSharedPreferences.AUTHENTICATION);
        EventBus.getDefault().register(this);
        NavigationView navigationView = binding.navView;
        UtilMethods.checkNetwork(context);
        toolbarBinding = binding.mainToolbar;
        drawer = binding.mainDrawer;

        toolbarBinding.toolbar.setTitle("");
        setSupportActionBar(toolbarBinding.toolbar);
        Log.e("onCreate", "onCreate");

        Menu menu = navigationView.getMenu();

        MenuItem title = menu.findItem(R.id.title);
        SpannableString s = new SpannableString(title.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.NavDrawerTextStyleTittle), 0, s.length(), 0);
        title.setTitle(s);
        navigationView.setNavigationItemSelectedListener(this);


        String post_id_notifaction = getIntent().getStringExtra("post_id");
        String user_id_notifaction = getIntent().getStringExtra("user_id");
        if (post_id_notifaction != null) {
            UtilMethods.getPostDetails(Integer.parseInt(post_id_notifaction), context, serviceApi, token, this::switchFragment,lang);
            switchFragment(NOTIFICATION, null, "");

        } else if (user_id_notifaction != null) {
            switchFragment(PagesFragment.PROFILE, new PostOrdersInfo(Integer.parseInt(user_id_notifaction)), "");
        } else
            switchFragment(ALL_POSTS, null, "");


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
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.inflateHeaderView(R.layout.nav_header);

        ImageView userImage = headerView.findViewById(R.id.user_image);
        TextView username = headerView.findViewById(R.id.user_profile);
        langSwitch = headerView.findViewById(R.id.lang_switch);
        String user = sharedPreferences.readUser(AppSharedPreferences.USER);
        Gson gson = new Gson();
        if (!user.isEmpty()) {
            User user1 = gson.fromJson(user, User.class);
            Glide.with(context).load(user1.getImageLink()).circleCrop()
                    .placeholder(R.drawable.usericon).into(userImage);
            username.setText(user1.getName());
        }
        binding.sv.setOnSearchViewListener(new OnSearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
//                binding.sv.closeSearch();
            }

            @Override
            public boolean onQueryTextSubmit(String s) {
                EventBus.getDefault().post(new MyTitleEventBus(SEARCH, s));
                return false;
            }

            @Override
            public void onQueryTextChange(String s) {
                EventBus.getDefault().post(new MyTitleEventBus(SEARCH, s));
            }
        }); // this class implements OnSearchViewListener
        setLang();
        changeLang();
    }

    private void setLang() {
        String lang = UtilMethods.getLang(context);
        if (lang.isEmpty() || lang.equals("en")) {
            currentLanguage="en";
            langSwitch.setChecked(false);
        } else{
            langSwitch.setChecked(true);
            currentLanguage="ar";
        }

    }

    private void changeLang() {
        langSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lang;
                if (langSwitch.isChecked()){
                    setLocale("ar");
                    sharedPreferences.writeString(AppSharedPreferences.LANG, "ar");
                }else {
                    setLocale("en");
                    sharedPreferences.writeString(AppSharedPreferences.LANG, "en");
                }
            }
        });
    }
    private void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            locale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = locale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this,
                    SplashActivity.class);
//            refresh.putExtra(currentLang, localeName);
            startActivity(refresh);
        } else {
            Toast.makeText(MainActivity.this, "Language already selected!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        isAllPost = false;
        Log.e("onNavigationItemSelected", item.getItemId() + "");
        switch (id) {
            case R.id.profile:
                UtilMethods.checkNetwork(context);
                switchFragment(PagesFragment.PROFILE, new PostOrdersInfo(0), "");
                break;

            case R.id.notifcation:
                UtilMethods.checkNetwork(context);
                switchFragment(PagesFragment.NOTIFICATION, null, "");
                break;

            case R.id.allPosts:
//                UtilMethods.checkNetwork(context);
                switchFragment(ALL_POSTS, null, "");
                isAllPost = true;
                break;

            case R.id.add_post:
                UtilMethods.checkNetwork(context);
                switchFragment(PagesFragment.ADD_POSTS, null, "");
                break;

            case R.id.nav_logout:
                UtilMethods.launchLoadingLottieDialog(context);
                UtilMethods.checkNetwork(context);
                requestLogout();
                break;

            case R.id.nav_change_password:
                switchFragment(PagesFragment.CHANGE_PASSWORD, null, "");
                break;


        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Subscribe
    public void onEvent(MyTitleEventBus event) {
        Log.e("onEvent Main activity", "onEvent");
        if (event.getType() == ALL_POSTS) {
            toolbarBinding.tvTitle.setText(event.getText());
            toolbarBinding.getRoot().setVisibility(View.VISIBLE);
            binding.marginTop.setVisibility(View.VISIBLE);

        } else if (event.getType() == ADD_POSTS) {
            toolbarBinding.tvTitle.setText(event.getText());
            toolbarBinding.getRoot().setVisibility(View.VISIBLE);
            binding.marginTop.setVisibility(View.VISIBLE);

        } else if (event.getType() == NOTIFICATION) {
            toolbarBinding.getRoot().setVisibility(View.VISIBLE);
            binding.marginTop.setVisibility(View.VISIBLE);
            toolbarBinding.tvTitle.setText(event.getText());
        } else if (event.getType() == CHANGE_PASSWORD) {
            toolbarBinding.getRoot().setVisibility(View.VISIBLE);
            binding.marginTop.setVisibility(View.VISIBLE);
            toolbarBinding.tvTitle.setText(event.getText());
        } else if (event.getType() == PROFILE)
            binding.marginTop.setVisibility(View.GONE);
        else if (event.getType() == EDIT) {
            switchFragment(ADD_POSTS, null, event.getText());

        }
    }

    @Override
    public void switchFragment(PagesFragment pagesFragment, PostOrdersInfo object, String post) {
        switch (pagesFragment) {
            case ADD_POSTS:
                isAllPost = false;
                toolbarBinding.getRoot().setVisibility(View.VISIBLE);
                fragment = AddPostFragment.newInstance(post, "");
                break;
            case ALL_POSTS:
                toolbarBinding.getRoot().setVisibility(View.VISIBLE);
                fragment = new AllPostsFragment();
                break;
            case NOTIFICATION:
                isAllPost = false;
                toolbarBinding.getRoot().setVisibility(View.VISIBLE);
                fragment = new NotificationFragment();
                break;
            case PROFILE:
                isAllPost = false;
                toolbarBinding.getRoot().setVisibility(View.GONE);
                fragment = ProfileFragment.newInstance(object.getUserId());
                break;
            case POST_ORDERS:
                isAllPost = false;
                toolbarBinding.getRoot().setVisibility(View.VISIBLE);
                fragment = PostOrdersFragment.newInstance(object);
                break;
            case CHANGE_PASSWORD:
                isAllPost = false;
                toolbarBinding.getRoot().setVisibility(View.VISIBLE);
                fragment = new ChangePasswordFragment();
                break;
            default:
        }
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
            case CHANGE_PASSWORD:
                binding.navView.getMenu().getItem(4).setChecked(true);
                binding.navView.getMenu().getItem(0).setChecked(false);

                break;

        }
        if (ALL_POSTS == pagesFragment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment, null).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment, null).commit();
        }
    }

    //------------------------------ menu ---------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (fragmentSelcted == ALL_POSTS) {
            getMenuInflater().inflate(R.menu.search_menu, menu);
            MenuItem item = menu.findItem(R.id.nav_search);
            binding.sv.setMenuItem(item);
        }


        return true;
    }

    //----------------------------------search-------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("TAG", "onOptionsItemSelected: Menu");
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;
        }
        return true;
    }

    //--------------------------------------------------
    @Override
    public void onBackPressed() {
        Log.e("isAllPost", isAllPost + "");
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
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
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
        //showDialog();
        Call<LogOut> call = serviceApi.logout("Bearer " + token);
        call.enqueue(new Callback<LogOut>() {

            @Override
            public void onResponse(Call<LogOut> call, Response<LogOut> response) {
                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    sharedPreferences.writeString(AppSharedPreferences.AUTHENTICATION, "");
                    Intent i = new Intent(context, SplashActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    Toast.makeText(context, R.string.session_was_expired_logout_processing, Toast.LENGTH_SHORT).show();
                    UtilMethods.launchLoadingLottieDialogDismiss(context);
                    //progressDialog.dismiss();

                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<LogOut> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void launchRippleLottieDialog() {
        int orangeColor = ContextCompat.getColor(context, R.color.color_app);

        LottieDialog dialog = new LottieDialog(context)
                .setAnimation(R.raw.ripple)
                .setAutoPlayAnimation(true)
                .setDialogHeightPercentage(.2f)
                .setDialogBackground(Color.BLACK)
                .setAnimationRepeatCount(LottieDialog.INFINITE)
                .setMessage("Loading...")
                .setMessageColor(orangeColor);

        dialog.show();
    }

}