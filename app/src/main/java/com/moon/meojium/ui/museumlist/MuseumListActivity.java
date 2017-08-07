package com.moon.meojium.ui.museumlist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.moon.meojium.R;

/**
 * Created by moon on 2017. 8. 6..
 */

public class MuseumListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_list);

        initToolbar();
        initDrawerLayout();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }

    private void initDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: 각 버튼에 해당하는 이벤트 세팅

        switch (item.getItemId()) {
            case R.id.action_filter:
                break;
            case R.id.action_search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // TODO: 각 버튼에 해당하는 이벤트 세팅
        switch (item.getItemId()) {
            case R.id.navigation_home:
                break;
            case R.id.navigation_favorite_museum:
                break;
            case R.id.navigation_setting:
                break;
            case R.id.navigation_logout:
                break;
        }

        drawerLayout.closeDrawers();

        return true;
    }
}
