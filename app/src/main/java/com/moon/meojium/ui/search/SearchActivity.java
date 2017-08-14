package com.moon.meojium.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.moon.meojium.R;
import com.moon.meojium.model.searchlog.SearchLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by moon on 2017. 8. 14..
 */

public class SearchActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.searchview)
    SearchView searchView;
    @BindView(R.id.recyclerview_search_log)
    RecyclerView recyclerView;
    @BindView(R.id.textview_search_log_nothing_data)
    TextView nothingDataTextView;

    private List<SearchLog> searchLogList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        createSearchLogDummyData();

        initToolbar();
        initSearchView();
        initRecyclerView();
        initNothingDataTextView();
    }

    private void createSearchLogDummyData() {
        searchLogList = new ArrayList<>();

        SearchLog searchLog = new SearchLog();
        searchLog.setId(101);
        searchLog.setKeyword("석장리 박물관");
        searchLog.setSearchedDate("2017-08-12");
        searchLogList.add(searchLog);

        searchLog = new SearchLog();
        searchLog.setId(102);
        searchLog.setKeyword("부천");
        searchLog.setSearchedDate("2017-08-11");
        searchLogList.add(searchLog);

        searchLog = new SearchLog();
        searchLog.setId(103);
        searchLog.setKeyword("인천");
        searchLog.setSearchedDate("2017-08-09");
        searchLogList.add(searchLog);

        searchLog = new SearchLog();
        searchLog.setId(104);
        searchLog.setKeyword("서울");
        searchLog.setSearchedDate("2017-08-09");
        searchLogList.add(searchLog);

        searchLog = new SearchLog();
        searchLog.setId(105);
        searchLog.setKeyword("독립기념관");
        searchLog.setSearchedDate("2017-08-08");
        searchLogList.add(searchLog);
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initSearchView() {
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);

        int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView magImage = searchView.findViewById(magId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
    }

    private void initRecyclerView() {
        SearchRecyclerViewAdapter adapter = new SearchRecyclerViewAdapter(searchLogList, this);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
    }

    private void initNothingDataTextView() {
        if (searchLogList.size() == 0) {
            nothingDataTextView.setVisibility(View.VISIBLE);
        }
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

    @Override
    public boolean onQueryTextSubmit(String s) {
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("keyword", s);
        startActivity(intent);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
