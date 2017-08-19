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
import com.moon.meojium.model.museum.Museum;
import com.moon.meojium.model.story.Story;
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
    private List<Museum> tastingMuseumList;
    private SharedPreferencesService sharedPreferencesService;
    private MuseumDao museumDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        backPressCloseHandler = new BackPressCloseHandler(this);
        museumDao = MuseumDao.getInstance();

        initToastyConfig();
        initSharedPreferences();

        initToolbar();
        initDrawerLayout();
        initNearbyImageView();

        createTastingMuseumDummyData();
        initTastingMuseumRecyclerView();

        requestPopularMuseumData();
        requestHistoryMuseumData();
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

    private void createTastingMuseumDummyData() {
        tastingMuseumList = new ArrayList<>();

        Museum museum = new Museum();
        museum.setId(1111);
        museum.setName("맛보기 독립기념관");
        museum.setImage(R.drawable.img_dokrip);
        museum.setAddress("충청남도 공주시 금벽로 990");

        Story story = new Story();
        story.setId(1111);
        story.setTitle("군인 휴가");
        List<Story> storyList = new ArrayList<>();
        storyList.add(story);
        museum.setStoryList(storyList);

        tastingMuseumList.add(museum);

        museum = new Museum();
        museum.setId(2222);
        museum.setName("맛보기 석장리 박물관2");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");

        story = new Story();
        story.setId(2222);
        story.setTitle("알쓸신잡 6회 - 선사인의 불");
        storyList = new ArrayList<>();
        storyList.add(story);
        museum.setStoryList(storyList);

        tastingMuseumList.add(museum);

        museum = new Museum();
        museum.setId(3333);
        museum.setName("맛보기 석장리 박물관3");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");

        story = new Story();
        story.setId(3333);
        story.setTitle("파른 손보기 선생 기념관");
        storyList = new ArrayList<>();
        storyList.add(story);
        museum.setStoryList(storyList);

        tastingMuseumList.add(museum);
    }

    private void initTastingMuseumRecyclerView() {
        TastingRecyclerViewAdapter adapter = new TastingRecyclerViewAdapter(tastingMuseumList, this);
        tastingRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        tastingRecyclerView.setLayoutManager(manager);

        tastingRecyclerView.setNestedScrollingEnabled(false);
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
