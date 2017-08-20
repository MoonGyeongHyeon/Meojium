package com.moon.meojium.ui.interested;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.moon.meojium.R;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested);
        ButterKnife.bind(this);

        initToolbar();
        initTabLayout();
    }

    private void initToolbar() {
        toolbar.setTitle("관심 박물관");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initTabLayout() {
        List<String> categoryList = new ArrayList<>();
        categoryList.add("찜목록");
        categoryList.add("다녀왔음");

        InterestedViewPagerAdapter adapter = new InterestedViewPagerAdapter(getSupportFragmentManager(), categoryList);
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
