package com.moon.meojium.ui.reviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.moon.meojium.R;
import com.moon.meojium.model.museum.Museum;
import com.moon.meojium.model.review.Review;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * Created by moon on 2017. 8. 15..
 */

public class ReviewActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview_review)
    RecyclerView recyclerView;

    private List<Review> reviewList;
    private Museum museum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        try {
            museum = Parcels.unwrap(intent.getParcelableExtra("museum"));
        } catch (Exception e) {
            e.printStackTrace();
            Toasty.info(this, getResources().getString(R.string.info_message)).show();
            onBackPressed();
        }

        createDummyData();

        initToolbar();
        initRecyclerView();
    }

    private void createDummyData() {
        reviewList = new ArrayList<>();

        Review review = new Review();
        review.setId(1);
        review.setNickname("송중기");
        review.setContent("여기 진짜 좋아용!!!!");
        review.setRegisteredDate("2017-07-24");
        review.setWriter(false);
        reviewList.add(review);

        review = new Review();
        review.setId(2);
        review.setNickname("송혜교");
        review.setContent("꼭 가봐야지~~");
        review.setRegisteredDate("2017-07-23");
        review.setWriter(true);
        reviewList.add(review);

        review = new Review();
        review.setId(3);
        review.setNickname("히히");
        review.setContent("1등");
        review.setRegisteredDate("2017-07-21");
        review.setWriter(true);
        reviewList.add(review);

        review = new Review();
        review.setId(3);
        review.setNickname("쿠쿠");
        review.setContent("0등");
        review.setRegisteredDate("2017-07-21");
        review.setWriter(false);
        reviewList.add(review);

        review = new Review();
        review.setId(3);
        review.setNickname("고구마피자");
        review.setContent("-1등");
        review.setRegisteredDate("2017-07-20");
        review.setWriter(false);
        reviewList.add(review);
    }

    private void initToolbar() {
        toolbar.setTitle("리뷰 보기");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void initRecyclerView() {
        ReviewRecyclerViewAdapter adapter = new ReviewRecyclerViewAdapter(reviewList, this);
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
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
