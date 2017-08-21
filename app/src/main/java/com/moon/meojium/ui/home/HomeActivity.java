package com.moon.meojium.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moon.meojium.R;
import com.moon.meojium.base.util.SharedPreferencesService;
import com.moon.meojium.database.dao.MuseumDao;
import com.moon.meojium.database.dao.TastingDao;
import com.moon.meojium.model.museum.Museum;
import com.moon.meojium.model.tasting.Tasting;
import com.moon.meojium.ui.interested.InterestedActivity;
import com.moon.meojium.ui.login.LoginActivity;
import com.moon.meojium.ui.login.naver.NaverLogin;
import com.moon.meojium.ui.nearby.NearbyActivity;
import com.moon.meojium.ui.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by moon on 2017. 8. 6..
 */

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int TASTING_COUNT = 4;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawerlayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigationview)
    NavigationView navigationView;
    @BindView(R.id.viewpager_home_popular_museum)
    ViewPager popularMuseumViewPager;
    @BindView(R.id.viewpager_home_history_museum)
    ViewPager historyMuseumViewPager;
    @BindView(R.id.recyclerview_home_tasting)
    RecyclerView tastingRecyclerView;
    @BindView(R.id.imageview_home_nearby)
    ImageView nearbyImageView;

    @OnClick(R.id.imageview_home_nearby)
    public void onClick(View view) {
        Intent intent = new Intent(this, NearbyActivity.class);
        startActivity(intent);
    }

    private TextView usernameTextView;
    private BackPressCloseHandler backPressCloseHandler;
    private List<Museum> popularMuseumList;
    private List<Museum> historyMuseumList;
    private List<Tasting> tastingMuseumList;
    private SharedPreferencesService sharedPreferencesService;
    private MuseumDao museumDao;
    private TastingDao tastingDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        backPressCloseHandler = new BackPressCloseHandler(this);
        museumDao = MuseumDao.getInstance();
        tastingDao = TastingDao.getInstance();

        requestPopularMuseumData();
        requestHistoryMuseumData();
        requestTastingMuseumData();

        initToastyConfig();
        initSharedPreferences();
        initToolbar();
        initDrawerLayout();
        initNearbyImageView();
    }

    private void requestPopularMuseumData() {
        Call<List<Museum>> call = museumDao.getPopularMuseumList();
        call.enqueue(new Callback<List<Museum>>() {
            @Override
            public void onResponse(Call<List<Museum>> call, Response<List<Museum>> response) {
                Log.d("Meojium/Home", "Success Getting Popular Museum List");
                popularMuseumList = response.body();

                initPopularMuseumViewPager();
            }

            @Override
            public void onFailure(Call<List<Museum>> call, Throwable t) {
                t.printStackTrace();
                Toasty.info(HomeActivity.this, "서버 연결에 실패했습니다").show();
            }
        });
    }

    private void initPopularMuseumViewPager() {
        MuseumViewPagerAdapter adapter = new MuseumViewPagerAdapter(getSupportFragmentManager(), popularMuseumList);
        popularMuseumViewPager.setAdapter(adapter);
        popularMuseumViewPager.setPageMargin(32);
    }

    private void requestHistoryMuseumData() {
        Call<List<Museum>> call = museumDao.getHistoryMuseumList();
        call.enqueue(new Callback<List<Museum>>() {
            @Override
            public void onResponse(Call<List<Museum>> call, Response<List<Museum>> response) {
                Log.d("Meojium/Home", "Success Getting History Museum List");
                historyMuseumList = response.body();

                initHistoryMuseumViewPager();
            }

            @Override
            public void onFailure(Call<List<Museum>> call, Throwable t) {
                t.printStackTrace();
                Toasty.info(HomeActivity.this, "서버 연결에 실패했습니다").show();
            }
        });
    }

    private void initHistoryMuseumViewPager() {
        MuseumViewPagerAdapter adapter = new MuseumViewPagerAdapter(getSupportFragmentManager(), historyMuseumList);
        historyMuseumViewPager.setAdapter(adapter);
        historyMuseumViewPager.setPageMargin(32);
    }

    private void requestTastingMuseumData() {
        Call<List<Tasting>> call = tastingDao.getTastingMuseumList();
        call.enqueue(new Callback<List<Tasting>>() {
            @Override
            public void onResponse(Call<List<Tasting>> call, Response<List<Tasting>> response) {
                List<Tasting> list = response.body();
                tastingMuseumList = new ArrayList<>();
                boolean flag;

                for (Tasting baseTasting : list) {
                    flag = true;

                    for (Tasting tasting : tastingMuseumList) {
                        if (baseTasting.getMuseum().getId() == tasting.getMuseum().getId()) {
                            flag = false;
                            break;
                        }
                    }

                    if (flag) {
                        tastingMuseumList.add(baseTasting);
                    }

                    if (tastingMuseumList.size() == TASTING_COUNT) {
                        break;
                    }
                }

                initTastingMuseumRecyclerView();
            }

            @Override
            public void onFailure(Call<List<Tasting>> call, Throwable t) {
                Toasty.info(HomeActivity.this, "서버 연결에 실패했습니다").show();
            }
        });
    }

    private void initTastingMuseumRecyclerView() {
        TastingRecyclerViewAdapter adapter = new TastingRecyclerViewAdapter(tastingMuseumList, this);
        tastingRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        tastingRecyclerView.setLayoutManager(manager);

        tastingRecyclerView.setNestedScrollingEnabled(false);
    }

    private void initToastyConfig() {
        Toasty.Config config = Toasty.Config.getInstance();
        config.setInfoColor(ContextCompat.getColor(this, R.color.colorPrimary)).apply();
    }

    private void initSharedPreferences() {
        sharedPreferencesService = SharedPreferencesService.getInstance();
    }

    private void initToolbar() {
        toolbar.setTitle("머지엄");
        setSupportActionBar(toolbar);
    }

    private void initDrawerLayout() {
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        usernameTextView = header.findViewById(R.id.textview_navigation_username);

        usernameTextView.setText(String.format(getResources().getString(R.string.navigation_username),
                sharedPreferencesService.getStringData(SharedPreferencesService.KEY_NICKNAME)));
    }

    private void initNearbyImageView() {
        Glide.with(this)
                .load(R.drawable.img_nearby_museum)
                .into(nearbyImageView);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            backPressCloseHandler.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                break;
            case R.id.action_search:
                Intent searchIntent = new Intent(this, SearchActivity.class);
                searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(searchIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                break;
            case R.id.navigation_interested_museum:
                Intent interestedIntent = new Intent(this, InterestedActivity.class);
                startActivity(interestedIntent);
                break;
            case R.id.navigation_setting:
                break;
            case R.id.navigation_logout:
                Log.d("Meojium/Home", "Try Logout");

                String tokenType = sharedPreferencesService.getStringData(SharedPreferencesService.KEY_TOKEN_TYPE);

                Log.d("Meojium/Home", SharedPreferencesService.KEY_TOKEN_TYPE + ": " + tokenType);
                Log.d("Meojium/Home", SharedPreferencesService.KEY_NICKNAME + ": " +
                        sharedPreferencesService.getStringData(SharedPreferencesService.KEY_NICKNAME));

                switch (tokenType) {
                    case NaverLogin.NAVER_TOKEN_TYPE:
                        NaverLogin naverLogin = new NaverLogin(this);
                        naverLogin.logout();
                        break;
                }

                sharedPreferencesService.removeData(SharedPreferencesService.KEY_TOKEN,
                        SharedPreferencesService.KEY_TOKEN_TYPE, SharedPreferencesService.KEY_NICKNAME);

                Intent logoutIntent = new Intent(this, LoginActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);

                break;
        }

        drawerLayout.closeDrawers();

        return true;
    }
}
