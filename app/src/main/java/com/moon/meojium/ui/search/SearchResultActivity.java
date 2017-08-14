package com.moon.meojium.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.moon.meojium.R;
import com.moon.meojium.model.museum.Museum;
import com.moon.meojium.ui.museum.MuseumFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * Created by moon on 2017. 8. 14..
 */

public class SearchResultActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String keyword;
    private List<Museum> museumList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        try {
            keyword = intent.getStringExtra("keyword");
            Log.d("Meojium/SearchResult", "keyword: " + keyword);
        } catch (Exception e) {
            e.printStackTrace();
            Toasty.info(this, "일시적인 오류가 발생했습니다.").show();
            onBackPressed();
        }

        createSearchResultDummyData();

        initToolbar();
        initMuseumFragment();
    }

    private void createSearchResultDummyData() {
        museumList = new ArrayList<>();

        Museum museum = new Museum();
        museum.setId(1001);
        museum.setName("검색결과 석장리 박물관");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        museumList.add(museum);

        museum = new Museum();
        museum.setId(1002);
        museum.setName("검색결과 국립 민속 박물관");
        museum.setImage(R.drawable.img_gookrip_minsok);
        museum.setAddress("충청남도 공주시 금벽로 990");
        museumList.add(museum);

        museum = new Museum();
        museum.setId(1003);
        museum.setName("검색결과 석장리 박물관3");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        museumList.add(museum);

        museum = new Museum();
        museum.setId(1004);
        museum.setName("검색결과 석장리 박물관4");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        museumList.add(museum);

        museum = new Museum();
        museum.setId(1005);
        museum.setName("검색결과 석장리 박물관5");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        museumList.add(museum);

        museum = new Museum();
        museum.setId(1006);
        museum.setName("검색결과 석장리 박물관6");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        museumList.add(museum);
    }

    private void initToolbar() {
        toolbar.setTitle("검색 결과");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initMuseumFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout_search_result_container, MuseumFragment.newInstance(museumList))
                .commit();
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
