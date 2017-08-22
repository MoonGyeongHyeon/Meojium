package com.moon.meojium.ui.allmuseum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_museum);
        ButterKnife.bind(this);

        initToolbar();
        initAllMuseumFragment();
    }

    private void initToolbar() {
        toolbar.setTitle("모든 박물관");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initAllMuseumFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout_all_museum_container, MuseumFragment.newInstance("전체 보기"))
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
