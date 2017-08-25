package com.moon.meojium.ui.allmuseum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.moon.meojium.R;
import com.moon.meojium.ui.museum.MuseumFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by moon on 2017. 8. 21..
 */

public class AllMuseumActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private int orderBy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_museum);
        ButterKnife.bind(this);

        orderBy = MuseumFragment.ORDER_BY_POPULAR;

        initToolbar();
        initAllMuseumFragment();
    }

    private void initToolbar() {
        toolbar.setTitle("모든 박물관 - 인기 순");
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_filter));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initAllMuseumFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout_all_museum_container, MuseumFragment.newInstance("전체 보기", orderBy))
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_order_by_popular:
                orderBy = MuseumFragment.ORDER_BY_POPULAR;
                changeTitle("모든 박물관 - 인기 순");
                initAllMuseumFragment();
                break;
            case R.id.menu_order_by_history:
                orderBy = MuseumFragment.ORDER_BY_HISTORY;
                changeTitle("모든 박물관 - 설립일자 순");
                initAllMuseumFragment();
                break;
            case R.id.menu_order_by_area:
                orderBy = MuseumFragment.ORDER_BY_AREA;
                changeTitle("모든 박물관 - 규모 순");
                initAllMuseumFragment();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all_museum, menu);
        return true;
    }
}
