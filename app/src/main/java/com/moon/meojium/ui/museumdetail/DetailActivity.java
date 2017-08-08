package com.moon.meojium.ui.museumdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.moon.meojium.R;
import com.moon.meojium.model.museum.Museum;
import com.moon.meojium.model.review.Review;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by moon on 2017. 8. 7..
 */

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageview_detail_thumb)
    ImageView thumbImageView;
    @BindView(R.id.textview_detail_name)
    TextView nameTextView;
    @BindView(R.id.textview_detail_address)
    TextView addressTextView;
    @BindView(R.id.textview_detail_business_hour)
    TextView businessHourTextView;
    @BindView(R.id.textview_detail_day_off)
    TextView dayOffTextView;
    @BindView(R.id.textview_detail_fee)
    TextView feeTextView;
    @BindView(R.id.textview_detail_tel)
    TextView telTextView;
    @BindView(R.id.textview_detail_homepage)
    TextView homepageTextView;
    @BindView(R.id.textview_detail_intro)
    TextView introTextView;
    @BindView(R.id.textview_detail_review_user_nickname1)
    TextView reviewNicknameTextView1;
    @BindView(R.id.textview_detail_review_registered_date1)
    TextView reviewRegisteredDateTextView1;
    @BindView(R.id.textview_detail_review_content1)
    TextView reviewContentTextView1;
    @BindView(R.id.textview_detail_review_user_nickname2)
    TextView reviewNicknameTextView2;
    @BindView(R.id.textview_detail_review_registered_date2)
    TextView reviewRegisteredDateTextView2;
    @BindView(R.id.textview_detail_review_content2)
    TextView reviewContentTextView2;
    @BindView(R.id.textview_detail_review_user_nickname3)
    TextView reviewNicknameTextView3;
    @BindView(R.id.textview_detail_review_registered_date3)
    TextView reviewRegisteredDateTextView3;
    @BindView(R.id.textview_detail_review_content3)
    TextView reviewContentTextView3;

    private Museum museum;
    private int id;
    private Review review1, review2, review3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        if (intent != null) {
            Log.d("Meojium", intent.getStringExtra("id"));
            id = Integer.parseInt(intent.getStringExtra("id"));
        } else {
            Toast.makeText(this, "일시적인 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        initToolbar();

        initMuseumData();
        updateMuseumTextView();

        initReviewData();
        updateReviewData();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initMuseumData() {
        museum = new Museum();

        museum.setId(id);
        museum.setName("석장리 박물관3");
        museum.setIntro("이 박물관은 석장리 박물관입니다.");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setAddress("충청남도 공주시 금벽로 990");
        museum.setBusinessHours("10:00 ~ 18:00");
        museum.setDayOff("매주 월요일");
        museum.setFee("어린이: 800, 청소년: 1000, 성인: 1300");
        museum.setHomepage("http://naver.com");
        museum.setTel("041-123-4567");
    }


    private void updateMuseumTextView() {
        thumbImageView.setImageResource(museum.getImage());
        nameTextView.setText(museum.getName());
        addressTextView.setText(museum.getAddress());
        businessHourTextView.setText(museum.getBusinessHours());
        dayOffTextView.setText(museum.getDayOff());
        feeTextView.setText(museum.getFee());
        telTextView.setText(museum.getTel());
        homepageTextView.setText(museum.getHomepage());
        introTextView.setText(museum.getIntro());
    }

    private void initReviewData() {
        review1 = new Review();
        review1.setId(1);
        review1.setNickname("송중기");
        review1.setContent("여기 진짜 좋아용");
        review1.setRegisteredDate("2017-07-24");

        review2 = new Review();
        review2.setId(2);
        review2.setNickname("송혜교");
        review2.setContent("꼭 가봐야지~~");
        review2.setRegisteredDate("2017-07-23");

        review3 = new Review();
        review3.setId(3);
        review3.setNickname("히히");
        review3.setContent("1등");
        review3.setRegisteredDate("2017-07-21");
    }

    private void updateReviewData() {
        reviewNicknameTextView1.setText(review1.getNickname());
        reviewRegisteredDateTextView1.setText(review1.getRegisteredDate());
        reviewContentTextView1.setText(review1.getContent());

        reviewNicknameTextView2.setText(review2.getNickname());
        reviewRegisteredDateTextView2.setText(review2.getRegisteredDate());
        reviewContentTextView2.setText(review2.getContent());

        reviewNicknameTextView3.setText(review3.getNickname());
        reviewRegisteredDateTextView3.setText(review3.getRegisteredDate());
        reviewContentTextView3.setText(review3.getContent());
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
