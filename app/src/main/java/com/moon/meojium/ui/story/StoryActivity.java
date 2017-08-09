package com.moon.meojium.ui.story;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.moon.meojium.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by moon on 2017. 8. 9..
 */

public class StoryActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        try {
            id = Integer.parseInt(intent.getStringExtra("id"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "일시적인 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        Log.d("Meojium/Detail", "Story id: " + id);

        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
