package com.moon.meojium.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.moon.meojium.R;
import com.moon.meojium.base.FrescoImageViewer;
import com.moon.meojium.base.NaverAPI;
import com.moon.meojium.model.museum.Museum;
import com.moon.meojium.model.review.Review;
import com.moon.meojium.model.story.Story;
import com.moon.meojium.ui.reviews.ReviewActivity;
import com.moon.meojium.ui.reviews.ReviewRecyclerViewAdapter;
import com.moon.meojium.ui.story.StoryActivity;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import es.dmoral.toasty.Toasty;

/**
 * Created by moon on 2017. 8. 7..
 */

public class DetailActivity extends AppCompatActivity
        implements NaverAPI, FrescoImageViewer {

    @BindView(R.id.include_detail_toolbar)
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
    @BindView(R.id.checkbox_detail_favorite)
    CheckBox favoriteCheckBox;
    @BindView(R.id.checkbox_detail_stamp)
    CheckBox stampCheckBox;
    @BindView(R.id.mapview_detail)
    NMapView mapView;
    @BindView(R.id.fab)
    Fab fab;
    @BindView(R.id.fab_sheet)
    View sheetView;
    @BindView(R.id.fab_overlay)
    View overlayView;
    @BindView(R.id.fab_sheet_container)
    LinearLayout sheetContainer;
    @BindView(R.id.recyclerview_detail_review)
    RecyclerView reviewRecyclerView;
    @BindView(R.id.relativelayout_detail_review_container)
    RelativeLayout reviewContainer;

    @OnTouch(R.id.mapview_detail)
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                view.getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                view.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return false;
    }

    @OnClick(R.id.relativelayout_detail_favorite_container)
    public void onClickFavorite(View view) {
        if (favoriteCheckBox.isChecked()) {
            favoriteCheckBox.setChecked(false);
        } else {
            favoriteCheckBox.setChecked(true);
        }
    }

    @OnClick(R.id.relativelayout_detail_stamp_container)
    public void onClickStamp(View view) {
        if (stampCheckBox.isChecked()) {
            stampCheckBox.setChecked(false);
        } else {
            stampCheckBox.setChecked(true);
        }
    }

    @OnClick(R.id.imageview_detail_thumb)
    public void onClickThumb(View view) {
        showPicker();
    }

    @OnClick(R.id.relativelayout_detail_review_container)
    public void onClickReview(View view) {
        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra("museum", Parcels.wrap(museum));
        startActivity(intent);
    }

    private Museum museum;
    private List<Review> reviewList;
    private NMapContext mapContext;
    private MaterialSheetFab materialSheetFab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        try {
            museum = Parcels.unwrap(intent.getParcelableExtra("museum"));

            if (intent.getBooleanExtra("cascade", false)) {
                Intent cascadeIntent = new Intent(this, StoryActivity.class);
                cascadeIntent.putExtra("museum", Parcels.wrap(museum));
                cascadeIntent.putExtra("cascade", true);
                startActivity(cascadeIntent);
            }

            Log.d("Meojium/Detail", "Museum id: " + museum.getId());
        } catch (Exception e) {
            e.printStackTrace();
            Toasty.info(this, "일시적인 오류가 발생했습니다.").show();
            onBackPressed();
        }

        initToolbar();
        createDummyData();

        initReviewRecyclerView();

        updateMuseumTextView();

        initMapView();
        initFloatingActionButton();
    }

    private void initToolbar() {
        toolbar.setTitle(museum.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void createDummyData() {
        createMuseumData();
        createReviewData();
        createStoryData();
    }

    private void createMuseumData() {
        museum.setIntro(" 한국 구석기 유적이 최초로 발견된 곳에 세워진 박물관으로 구석기시대의 정취를 한껏 느낄 수 있고 구석기와 관련된 다양한 교육체험프로그램 진행으로 가족단위의 관람객들이 방문하기에 최적의 장소입니다");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setBusinessHours("10:00 ~ 18:00");
        museum.setDayOff("매주 월요일");
        museum.setFee("어린이: 800, 청소년: 1000, 성인: 1300");
        museum.setHomepage("http://naver.com");
        museum.setTel("041-123-4567");
        museum.setLatitude(36.447573);
        museum.setLongitude(127.189654);
    }

    private void createReviewData() {
        reviewList = new ArrayList<>();

        Review review = new Review();
        review.setId(1);
        review.setNickname("송중기");
        review.setContent("여기 진짜 좋아용!!!!");
        review.setRegisteredDate("2017-07-24");
        reviewList.add(review);

        review = new Review();
        review.setId(2);
        review.setNickname("송혜교");
        review.setContent("꼭 가봐야지~~");
        review.setRegisteredDate("2017-07-23");
        reviewList.add(review);

        review = new Review();
        review.setId(3);
        review.setNickname("히히");
        review.setContent("1등");
        review.setRegisteredDate("2017-07-21");
        reviewList.add(review);
    }

    private void createStoryData() {
        List<Story> storyList = new ArrayList<>();

        Story story = new Story();
        story.setId(1);
        story.setTitle("알쓸신잡 6회 - 선사인의 불");
        storyList.add(story);

        story = new Story();
        story.setId(2);
        story.setTitle("파른 손보기 선생 기념관");
        storyList.add(story);

        story = new Story();
        story.setId(3);
        story.setTitle("설립 취지");
        storyList.add(story);

        museum.setStoryList(storyList);
    }

    private void initReviewRecyclerView() {
        ReviewRecyclerViewAdapter adapter = new ReviewRecyclerViewAdapter(reviewList, this);
        reviewRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reviewRecyclerView.setLayoutManager(manager);

        reviewRecyclerView.setNestedScrollingEnabled(false);
        reviewRecyclerView.setLayoutFrozen(true);
    }

    private void updateMuseumTextView() {
        Glide.with(this)
                .load(museum.getImage())
                .into(thumbImageView);
        nameTextView.setText(museum.getName());
        addressTextView.setText(museum.getAddress());
        businessHourTextView.setText(museum.getBusinessHours());
        dayOffTextView.setText(museum.getDayOff());
        feeTextView.setText(museum.getFee());
        telTextView.setText(museum.getTel());
        homepageTextView.setText(museum.getHomepage());
        introTextView.setText(museum.getIntro());
    }

    private void initMapView() {
        mapContext = new NMapContext(this);
        mapContext.onCreate();

        mapView.setClientId(CLIENT_ID);
        mapView.setClickable(true);
        mapView.setEnabled(true);
        mapView.setFocusable(true);
        mapView.setFocusableInTouchMode(true);

        mapContext.setupMapView(mapView);

        mapView.getMapController().setZoomLevel(13);

        addMarker();
    }

    private void addMarker() {
        NMapViewerResourceProvider provider = new NMapViewerResourceProvider(this);

        NMapPOIdata poIdata = new NMapPOIdata(1, provider, true);

        poIdata.beginPOIdata(1);
        poIdata.addPOIitem(museum.getLongitude(), museum.getLatitude(), museum.getName(), NMapPOIflagType.PIN, null);
        poIdata.endPOIdata();

        NMapOverlayManager manager = new NMapOverlayManager(this, mapView, provider);

        NMapPOIdataOverlay poIdataOverlay = manager.createPOIdataOverlay(poIdata, null);
        poIdataOverlay.selectPOIitem(0, true);
    }

    private void initFloatingActionButton() {
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlayView,
                android.R.color.white, R.color.colorAccent);

        addButtonToSheet();
    }

    private void addButtonToSheet() {
        for (final Story story : museum.getStoryList()) {
            Button button = new Button(this);
            button.setText(story.getTitle());
            button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    materialSheetFab.hideSheet();

                    Intent intent = new Intent(DetailActivity.this, StoryActivity.class);
                    intent.putExtra("story", Parcels.wrap(story));
                    startActivity(intent);
                }
            });
            sheetContainer.addView(button);
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
    public void showPicker() {
        new ImageViewer.Builder<>(this, getImageUrlList())
                .setStartPosition(0)
                .show();
    }

    public List<String> getImageUrlList() {
        List<String> list = new ArrayList<>();

        list.add("http://cfile8.uf.tistory.com/image/161360154C8E90341F6BB2");
        list.add("http://cfile23.uf.tistory.com/image/27379D3352BD1C171FC82D");
        list.add("http://bbkk.kr/d/t/3/3550_%EC%B6%A9%EB%82%A8%EA%B3%B5%EC%A3%BC%EC%8B%9C%EC%84%9D%EC%9E%A5%EB%A6%AC%EB%B0%95%EB%AC%BC%EA%B4%80%EB%9F%AC%EB%B8%94%EB%A6%AC%EC%B9%98%EB%A0%B9%EB%A7%98%EA%B4%80077.jpg");
        list.add("http://cfile232.uf.daum.net/image/0110DA425109DA6E27FF70");

        return list;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapContext.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapContext.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapContext.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapContext.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapContext.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (materialSheetFab != null) {
            if (materialSheetFab.isSheetVisible()) {
                materialSheetFab.hideSheet();
                return;
            }
        }

        super.onBackPressed();
    }
}
