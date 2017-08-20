package com.moon.meojium.ui.museum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moon.meojium.R;
import com.moon.meojium.base.BaseRetrofitService;
import com.moon.meojium.base.util.SharedPreferencesService;
import com.moon.meojium.database.dao.FavoriteDao;
import com.moon.meojium.database.dao.SearchDao;
import com.moon.meojium.database.dao.StampDao;
import com.moon.meojium.model.museum.Museum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by moon on 2017. 8. 7..
 */

public class MuseumFragment extends Fragment {
    @BindView(R.id.recyclerview_museum)
    RecyclerView recyclerView;
    @BindView(R.id.textview_nothing_data)
    TextView nothingDataTextView;

    private List<Museum> museumList;
    private BaseRetrofitService dao;
    private String category;
    private String keyword;
    private int startIndex;

    public static Fragment newInstance(String category) {
        Fragment fragment = new MuseumFragment();
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        fragment.setArguments(bundle);

        return fragment;
    }

    public static Fragment newInstance(String category, String keyword) {
        Fragment fragment = new MuseumFragment();
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        bundle.putString("keyword", keyword);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_museum, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();

        category = bundle.getString("category");
        startIndex = 0;

        switch (category) {
            case "찜목록":
                dao = FavoriteDao.getInstance();
                requestFavoriteMuseumData();
                break;
            case "다녀왔음":
                dao = StampDao.getInstance();
                requestStampMuseumData();
                break;
            case "검색":
                dao = SearchDao.getInstance();
                keyword = bundle.getString("keyword");
                requestSearchData();
                break;
        }
    }

    private void requestFavoriteMuseumData() {
        Call<List<Museum>> call = ((FavoriteDao) dao).getFavoriteMuseumList(SharedPreferencesService.getInstance().getStringData(SharedPreferencesService.KEY_TOKEN),
                startIndex);
        call.enqueue(new Callback<List<Museum>>() {
            @Override
            public void onResponse(Call<List<Museum>> call, Response<List<Museum>> response) {
                museumList = response.body();

                checkDataExisted();
            }

            @Override
            public void onFailure(Call<List<Museum>> call, Throwable t) {
                Toasty.info(getContext(), "서버 연결에 실패했습니다").show();
            }
        });
    }

    private void requestStampMuseumData() {
        Call<List<Museum>> call = ((StampDao) dao).getStampMuseumList(SharedPreferencesService.getInstance().getStringData(SharedPreferencesService.KEY_TOKEN),
                startIndex);
        call.enqueue(new Callback<List<Museum>>() {
            @Override
            public void onResponse(Call<List<Museum>> call, Response<List<Museum>> response) {
                museumList = response.body();

                checkDataExisted();
            }

            @Override
            public void onFailure(Call<List<Museum>> call, Throwable t) {
                Toasty.info(getContext(), "서버 연결에 실패했습니다").show();
            }
        });
    }

    private void requestSearchData() {
        Call<List<Museum>> call = ((SearchDao) dao).getMuseumListByKeyword(keyword);
        call.enqueue(new Callback<List<Museum>>() {
            @Override
            public void onResponse(Call<List<Museum>> call, Response<List<Museum>> response) {
                museumList = response.body();

                checkDataExisted();
            }

            @Override
            public void onFailure(Call<List<Museum>> call, Throwable t) {
                Toasty.info(getContext(), "서버 연결에 실패했습니다").show();
            }
        });
    }

    private void checkDataExisted() {
        if (museumList.isEmpty()) {
            nothingDataTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            nothingDataTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            initRecyclerView();
        }
    }

    private void initRecyclerView() {
        MuseumRecyclerViewAdapter adapter = new MuseumRecyclerViewAdapter(museumList, getContext());
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
    }

}
