package com.moon.meojium.ui.museum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moon.meojium.R;
import com.moon.meojium.model.museum.Museum;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by moon on 2017. 8. 7..
 */

public class MuseumFragment extends Fragment {
    @BindView(R.id.recyclerview_museum)
    RecyclerView recyclerView;
    private List<Museum> museumList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_museum, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        createDummyData();

        MuseumRecyclerViewAdapter adapter = new MuseumRecyclerViewAdapter(museumList, getContext());
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
    }

    private void createDummyData() {
        museumList = new ArrayList<>();

        Museum museum = new Museum();
        museum.setId(1);
        museum.setName("석장리 박물관");
        museum.setContent("이 박물관은 석장리 박물관입니다.");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        museum.setBusinessHours("10:00 ~ 18:00");
        museum.setDayOff("매주 월요일");
        museum.setFee("어린이: 800, 청소년: 1000, 성인: 1300");
        museum.setHomepage("http://naver.com");
        museum.setTel("041-123-4567");

        museumList.add(museum);

        museum = new Museum();
        museum.setId(2);
        museum.setName("석장리 박물관2");
        museum.setContent("이 박물관은 석장리 박물관입니다.");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        museum.setBusinessHours("10:00 ~ 18:00");
        museum.setDayOff("매주 월요일");
        museum.setFee("어린이: 800, 청소년: 1000, 성인: 1300");
        museum.setHomepage("http://naver.com");
        museum.setTel("041-123-4567");

        museumList.add(museum);

        museum = new Museum();
        museum.setId(3);
        museum.setName("석장리 박물관3");
        museum.setContent("이 박물관은 석장리 박물관입니다.");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        museum.setBusinessHours("10:00 ~ 18:00");
        museum.setDayOff("매주 월요일");
        museum.setFee("어린이: 800, 청소년: 1000, 성인: 1300");
        museum.setHomepage("http://naver.com");
        museum.setTel("041-123-4567");

        museumList.add(museum);

    }
}
