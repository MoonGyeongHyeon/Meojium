package com.moon.meojium.ui.museumdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.moon.meojium.R;
import com.moon.meojium.base.NaverAPI;
import com.moon.meojium.model.museum.Museum;
import com.moon.meojium.model.review.Review;
import com.moon.meojium.model.story.Story;
import com.moon.meojium.ui.story.StoryActivity;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

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
        implements NaverAPI {

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

    private Museum museum;
    private Review review1, review2, review3;
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
        initData();

        updateMuseumTextView();
        updateReviewData();

        initMapView();
        initFloatingActionButton();
    }

    private void initToolbar() {
        toolbar.setTitle(museum.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        initMuseumData();
        initReviewData();
        initStoryData();
    }

    private void initMuseumData() {
        museum.setIntro("이 박물관은 석장리 박물관입니다.");
        museum.setImage(R.drawable.img_seokjangni);
        museum.setBusinessHours("10:00 ~ 18:00");
        museum.setDayOff("매주 월요일");
        museum.setFee("어린이: 800, 청소년: 1000, 성인: 1300");
        museum.setHomepage("http://naver.com");
        museum.setTel("041-123-4567");
        museum.setLatitude(36.447573);
        museum.setLongitude(127.189654);
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

    private void initStoryData() {
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
