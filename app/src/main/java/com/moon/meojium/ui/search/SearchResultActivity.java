package com.moon.meojium.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.moon.meojium.R;
import com.moon.meojium.base.util.Dlog;
import com.moon.meojium.ui.museum.MuseumFragment;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        try {
            keyword = intent.getStringExtra("keyword");
            Dlog.d("keyword: " + keyword);
        } catch (Exception e) {
            e.printStackTrace();
            Toasty.info(this, "일시적인 오류가 발생했습니다.").show();
            onBackPressed();
        }

        initToolbar();
        initMuseumFragment();
    }

    private void initToolbar() {
        toolbar.setTitle("검색 결과");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initMuseumFragment() {
        String category = "검색";

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout_search_result_container, MuseumFragment.newInstance(category, keyword))
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
