package com.example.graduationproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.graduationproject.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class NavigationDrowor extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    ArrayList<MuneItem> muneItems = new ArrayList();
    int prevouspostion = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drowor);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawer = (DrawerLayout)
                findViewById(R.id.drawer_layout);
        //  drawerToggle = setupDrawerToggle();

        drawerToggle.setDrawerIndicatorEnabled(true);
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        NavigationView navigationView =
                findViewById(R.id.nvView);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_haede);
        TextView ivHeaderPhoto = headerView.findViewById(R.id.text);
        ivHeaderPhoto.setText("jenin ayada");

        mDrawer = findViewById(R.id.drawer_layout);
        nvDrawer = findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);

        // for(int i =0 ; i<nvDrawer.getMenu().size() ; i++){
        //    muneItems.add(nvDrawer.getMenu().getItem(i));
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }

        });
    }
    //public void selectDrawerItem(MenuItem menuItem) {
    //  switch (menuItem.getItemId()) {
    //  case R.id.home:
    //    openFragment(HomeFragment.newInstance("", ""), R.id.container);
    //   break;
    // case R.id.new_post:
    //  openFragment(NewPostFragment.newInstance("", ""),  R.id.container);
    //  break;
    // case R.id.notifications:
    //   openFragment(NotificationsFragment.newInstance("", ""), R.id.container);
    // break;
    //  }
    //  menuItem.setChecked(true);

    //int oldPosition = muneItems.indexOf(menuItem);
    //nvDrawer.getMenu().getItem(oldPosition).setChecked(false);
    //mDrawer.closeDrawers();
    // }
    //public void openFragment(Fragment fragment, int
    //      container) {
    //   FragmentTransaction transaction =
    //      getSupportFragmentManager().beginTransaction();

    // transaction.replace(container, fragment);
    /// transaction.commit();
    // }
    // private ActionBarDrawerToggle setupDrawerToggle() {
// NOTE: Make sure you pass in a valid toolbar reference. ActionBarDrawToggle() does not require it
// and will not render the hamburger icon withoutit.
    // return new ActionBarDrawerToggle(this,
    //   mDrawer,
    //  toolbar,
    //  R.string.drawer_open,
    //   R.string.drawer_close);
    // }
    //@Override
    // public void onBackPressed() {
    //  if (mDrawer.isDrawerOpen(GravityCompat.START)) {
    //   mDrawer.closeDrawer(GravityCompat.START);
    //  } else {
    //  super.onBackPressed();
    // }
    // }
}