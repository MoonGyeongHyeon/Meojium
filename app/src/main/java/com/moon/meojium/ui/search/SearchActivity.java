package com.moon.meojium.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.moon.meojium.R;
import com.moon.meojium.base.UpdateResult;
import com.moon.meojium.base.util.SharedPreferencesService;
import com.moon.meojium.database.dao.SearchDao;
import com.moon.meojium.model.searchlog.SearchLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private SearchDao searchDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        searchDao = SearchDao.getInstance();

        requestSearchLogData();

        initToolbar();
        initSearchView();
    }

    private void requestSearchLogData() {
        Call<List<SearchLog>> call = searchDao.getSearchLogList(SharedPreferencesService.getInstance().getStringData(SharedPreferencesService.KEY_TOKEN));
        call.enqueue(new Callback<List<SearchLog>>() {
            @Override
            public void onResponse(Call<List<SearchLog>> call, Response<List<SearchLog>> response) {
                searchLogList = response.body();

                initRecyclerView();
                initNothingDataTextView();
            }

            @Override
            public void onFailure(Call<List<SearchLog>> call, Throwable t) {
                Toasty.info(SearchActivity.this, "서버 연결에 실패했습니다").show();
            }
        });
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
        Call<UpdateResult> call = searchDao.addSearchLog(
                SharedPreferencesService.getInstance().getStringData(SharedPreferencesService.KEY_TOKEN), s);
        call.enqueue(new Callback<UpdateResult>() {
            @Override
            public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                UpdateResult result = response.body();

                if (result.getCode() == UpdateResult.RESULT_OK) {
                    Log.d("Meojium/Search", "Success Adding SearchLog");
                } else {
                    Log.d("Meojium/Search", "Fail Adding SearchLog");
                    Toasty.info(SearchActivity.this, "서버 연결에 실패했습니다").show();
                }
            }

            @Override
            public void onFailure(Call<UpdateResult> call, Throwable t) {
                Toasty.info(SearchActivity.this, "서버 연결에 실패했습니다").show();
            }
        });
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
