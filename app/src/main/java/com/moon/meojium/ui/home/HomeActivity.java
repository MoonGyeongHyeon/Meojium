package com.moon.meojium.ui.home;

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
import android.view.Menu;
import android.view.MenuItem;

import com.moon.meojium.R;
import com.moon.meojium.model.museum.Museum;
import com.moon.meojium.model.story.Story;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

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


    private BackPressCloseHandler backPressCloseHandler;
    private List<Museum> popularMuseumList;
    private List<Museum> historyMuseumList;
    private List<Museum> tastingMuseumList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        backPressCloseHandler = new BackPressCloseHandler(this);

        initToastyConfig();

        initToolbar();
        initDrawerLayout();

        createPopularMuseumDummyData();
        initPopularMuseumViewPager();

        createHistoryMuseumDummyData();
        initHistoryMuseumViewPager();

        createTastingMuseumDummyData();
        initTastingMuseumRecyclerView();
    }

    private void initToastyConfig() {
        Toasty.Config config = Toasty.Config.getInstance();
        config.setInfoColor(ContextCompat.getColor(this, R.color.colorPrimary)).apply();
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
    }

    private void createPopularMuseumDummyData() {
        popularMuseumList = new ArrayList<>();

        Museum museum = new Museum();
        museum.setId(1);
        museum.setName("인기있는 석장리 박물관");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        popularMuseumList.add(museum);

        museum = new Museum();
        museum.setId(2);
        museum.setName("인기있는 국립 민속 박물관");
        museum.setImage(R.drawable.img_gookrip_minsok);
        museum.setAddress("충청남도 공주시 금벽로 990");
        popularMuseumList.add(museum);

        museum = new Museum();
        museum.setId(3);
        museum.setName("인기있는 석장리 박물관3");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        popularMuseumList.add(museum);

        museum = new Museum();
        museum.setId(4);
        museum.setName("인기있는 석장리 박물관4");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        popularMuseumList.add(museum);

        museum = new Museum();
        museum.setId(5);
        museum.setName("인기있는 석장리 박물관5");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        popularMuseumList.add(museum);

        museum = new Museum();
        museum.setId(6);
        museum.setName("인기있는 석장리 박물관6");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        popularMuseumList.add(museum);
    }

    private void initPopularMuseumViewPager() {
        MuseumViewPagerAdapter adapter = new MuseumViewPagerAdapter(getSupportFragmentManager(), popularMuseumList);
        popularMuseumViewPager.setAdapter(adapter);
        popularMuseumViewPager.setPageMargin(32);
    }

    private void createHistoryMuseumDummyData() {
        historyMuseumList = new ArrayList<>();

        Museum museum = new Museum();
        museum.setId(111);
        museum.setName("역사깊은 독립기념관");
        museum.setImage(R.drawable.img_dokrip);
        museum.setAddress("충청남도 공주시 금벽로 990");
        historyMuseumList.add(museum);

        museum = new Museum();
        museum.setId(222);
        museum.setName("역사깊은 석장리 박물관2");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        historyMuseumList.add(museum);

        museum = new Museum();
        museum.setId(333);
        museum.setName("역사깊은 석장리 박물관3");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        historyMuseumList.add(museum);

        museum = new Museum();
        museum.setId(444);
        museum.setName("역사깊은 석장리 박물관4");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        historyMuseumList.add(museum);

        museum = new Museum();
        museum.setId(555);
        museum.setName("역사깊은 석장리 박물관5");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        historyMuseumList.add(museum);

        museum = new Museum();
        museum.setId(666);
        museum.setName("역사깊은 석장리 박물관6");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        historyMuseumList.add(museum);
    }

    private void initHistoryMuseumViewPager() {
        MuseumViewPagerAdapter adapter = new MuseumViewPagerAdapter(getSupportFragmentManager(), historyMuseumList);
        historyMuseumViewPager.setAdapter(adapter);
        historyMuseumViewPager.setPageMargin(32);
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
