package com.moon.meojium.ui.reviews;

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
import android.widget.EditText;
import android.widget.TextView;

import com.moon.meojium.R;
import com.moon.meojium.base.UpdateResult;
import com.moon.meojium.base.util.SharedPreferencesService;
import com.moon.meojium.database.dao.ReviewDao;
import com.moon.meojium.model.museum.Museum;
import com.moon.meojium.model.review.Review;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by moon on 2017. 8. 15..
 */

public class ReviewActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview_review)
    RecyclerView recyclerView;
    @BindView(R.id.edittext_review_content)
    EditText contentEditText;
    @BindView(R.id.textview_review_submit)
    TextView submitTextView;

    @OnClick(R.id.textview_review_submit)
    public void onClick(View view) {
        if (contentEditText.getText().toString().length() == 0 ) {
            Toasty.info(ReviewActivity.this, "내용을 입력해주세요.").show();
        } else {
            submitTextView.setClickable(false);
            final String content = contentEditText.getText().toString();
            final SharedPreferencesService sharedPreferencesService = SharedPreferencesService.getInstance();
            Call<UpdateResult> call = reviewDao.writeReview(sharedPreferencesService.getStringData(SharedPreferencesService.KEY_TOKEN),
                    content, museum.getId());
            call.enqueue(new Callback<UpdateResult>() {
                @Override
                public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                    UpdateResult result = response.body();

                    if (result.getCode() == UpdateResult.RESULT_OK) {
                        Log.d("Meojium/Review", "Success Adding Review");
                        Review review = new Review();
                        review.setId(result.getInsertId());
                        review.setNickname(sharedPreferencesService.getStringData(SharedPreferencesService.KEY_NICKNAME));
                        review.setContent(content);
                        review.setWriter(true);
                        Date date = Calendar.getInstance().getTime();
                        review.setRegisteredDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
                        reviewList.add(0, review);

                        adapter.notifyDataSetChanged();
                        adapter.setUpdated(true);

                        contentEditText.clearFocus();
                        contentEditText.setText("");

                        startIndex++;

                        submitTextView.setClickable(true);

                        initResultIntent();
                    } else {
                        Log.d("Meojium/Review", "Fail Adding Review");
                        Toasty.info(ReviewActivity.this, "서버 연결에 실패했습니다").show();
                        submitTextView.setClickable(true);
                    }
                }

                @Override
                public void onFailure(Call<UpdateResult> call, Throwable t) {
                    Toasty.info(ReviewActivity.this, "서버 연결에 실패했습니다").show();
                }
            });
        }
    }

    private List<Review> reviewList;
    private Museum museum;
    private ReviewDao reviewDao;
    private int startIndex = 0;
    private ReviewRecyclerViewAdapter adapter;

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

        reviewDao = ReviewDao.getInstance();

        requestReviewData();

        initToolbar();
    }

    private void requestReviewData() {
        Call<List<Review>> call = reviewDao.getReviewList(museum.getId(), startIndex);
        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                reviewList = response.body();

                String nickname = SharedPreferencesService.getInstance().getStringData(SharedPreferencesService.KEY_NICKNAME);
                for (Review review : reviewList) {
                    if (review.getNickname().equals(nickname)) {
                        review.setWriter(true);
                    }
                }

                initRecyclerView();
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Toasty.info(ReviewActivity.this, "서버 연결에 실패했습니다").show();
            }
        });
    }

    private void initRecyclerView() {
        adapter = new ReviewRecyclerViewAdapter(reviewList, this);
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
    }

    private void initToolbar() {
        toolbar.setTitle("리뷰 보기");
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

    @Override
    public void finish() {
        initResultIntent();

        super.finish();
    }

    private void initResultIntent() {
        Intent intent = new Intent();
        intent.putExtra("isUpdated", adapter.isUpdated());
        setResult(RESULT_OK, intent);
    }
}
