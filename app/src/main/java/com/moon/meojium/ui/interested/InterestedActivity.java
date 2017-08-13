package com.moon.meojium.ui.interested;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.moon.meojium.R;
import com.moon.meojium.model.museum.Museum;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by moon on 2017. 8. 13..
 */

public class InterestedActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tablayout_interested)
    TabLayout tabLayout;
    @BindView(R.id.viewpager_interested)
    ViewPager viewPager;

    private List<Museum> favoriteMuseumList;
    private List<Museum> stampMuseumList;
    private List<List<Museum>> museumLists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested);
        ButterKnife.bind(this);

        museumLists = new ArrayList<>();

        createFavoriteMuseumDummyData();
        createStampMuseumDummyData();

        initToolbar();
        initTabLayout();
    }

    private void initToolbar() {
        toolbar.setTitle("관심 박물관");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void createFavoriteMuseumDummyData() {
        favoriteMuseumList = new ArrayList<>();

        Museum museum = new Museum();
        museum.setId(11111);
        museum.setName("찜해놓은 독립기념관");
        museum.setImage(R.drawable.img_dokrip);
        museum.setAddress("충청남도 공주시 금벽로 990");
        favoriteMuseumList.add(museum);

        museum = new Museum();
        museum.setId(22222);
        museum.setName("찜해놓은 석장리 박물관2");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        favoriteMuseumList.add(museum);

        museum = new Museum();
        museum.setId(33333);
        museum.setName("찜해놓은 석장리 박물관3");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        favoriteMuseumList.add(museum);

        museum = new Museum();
        museum.setId(44444);
        museum.setName("찜해놓은 석장리 박물관4");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        favoriteMuseumList.add(museum);

        museum = new Museum();
        museum.setId(55555);
        museum.setName("찜해놓은 석장리 박물관5");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        favoriteMuseumList.add(museum);

        museum = new Museum();
        museum.setId(66666);
        museum.setName("찜해놓은 석장리 박물관6");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        favoriteMuseumList.add(museum);

        museumLists.add(favoriteMuseumList);
    }

    private void createStampMuseumDummyData() {
        stampMuseumList = new ArrayList<>();

        Museum museum = new Museum();
        museum.setId(111111);
        museum.setName("다녀온 독립기념관");
        museum.setImage(R.drawable.img_dokrip);
        museum.setAddress("충청남도 공주시 금벽로 990");
        stampMuseumList.add(museum);

        museum = new Museum();
        museum.setId(222222);
        museum.setName("다녀온 석장리 박물관2");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        stampMuseumList.add(museum);

        museum = new Museum();
        museum.setId(333333);
        museum.setName("다녀온 석장리 박물관3");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        stampMuseumList.add(museum);

        museum = new Museum();
        museum.setId(444444);
        museum.setName("다녀온 석장리 박물관4");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        stampMuseumList.add(museum);

        museum = new Museum();
        museum.setId(555555);
        museum.setName("다녀온 석장리 박물관5");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        stampMuseumList.add(museum);

        museum = new Museum();
        museum.setId(666666);
        museum.setName("다녀온 석장리 박물관6");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        stampMuseumList.add(museum);

        museumLists.add(stampMuseumList);
    }

    private void initTabLayout() {
        InterestedViewPagerAdapter adapter = new InterestedViewPagerAdapter(getSupportFragmentManager(), museumLists);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
