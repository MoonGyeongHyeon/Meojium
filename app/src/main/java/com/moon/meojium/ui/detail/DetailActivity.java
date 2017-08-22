package com.moon.meojium.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.moon.meojium.R;
import com.moon.meojium.base.BaseRetrofitService;
import com.moon.meojium.base.NaverAPI;
import com.moon.meojium.base.util.SharedPreferencesService;
import com.moon.meojium.database.dao.FavoriteDao;
import com.moon.meojium.database.dao.MuseumDao;
import com.moon.meojium.database.dao.ReviewDao;
import com.moon.meojium.database.dao.StampDao;
import com.moon.meojium.database.dao.StoryDao;
import com.moon.meojium.model.UpdateResult;
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

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by moon on 2017. 8. 7..
 */

public class DetailActivity extends AppCompatActivity
        implements NaverAPI {
    public static final int REQUEST_REVIEW_WRITE = 1;

    @BindView(R.id.include_detail_toolbar)
    Toolbar toolbar;
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
    @BindView(R.id.textview_detail_found_date)
    TextView foundDateTextView;
    @BindView(R.id.checkbox_detail_favorite)
    CheckBox favoriteCheckBox;
    @BindView(R.id.checkbox_detail_stamp)
    CheckBox stampCheckBox;
    @BindView(R.id.textview_detail_nothing_review)
    TextView nothingReviewTextView;
    @BindView(R.id.textview_detail_review_all)
    TextView reviewAllTextView;
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
    @BindView(R.id.viewpager_detail_thumb)
    ViewPager thumbViewPager;
    @BindView(R.id.tablayout_detail_thumb)
    TabLayout thumbTabLayout;

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
            Call<UpdateResult> call = favoriteDao.deleteFavoriteMuseum(SharedPreferencesService.getInstance().getStringData(SharedPreferencesService.KEY_ENC_ID),
                    museum.getId());
            call.enqueue(new Callback<UpdateResult>() {
                @Override
                public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                    UpdateResult result = response.body();

                    if (result.getCode() == UpdateResult.RESULT_OK) {
                        Log.d("Meojium/Detail", "Success Deleting Favorite Museum");
                        favoriteCheckBox.setChecked(false);
                    } else {
                        Log.d("Meojium/Detail", "Fail Deleting Favorite Museum");
                        Toasty.info(DetailActivity.this, "서버 연결에 실패했습니다").show();
                    }
                }

                @Override
                public void onFailure(Call<UpdateResult> call, Throwable t) {
                    Toasty.info(DetailActivity.this, "서버 연결에 실패했습니다").show();
                }
            });
        } else {
            Call<UpdateResult> call = favoriteDao.addFavoriteMuseum(SharedPreferencesService.getInstance().getStringData(SharedPreferencesService.KEY_ENC_ID),
                    museum.getId());
            call.enqueue(new Callback<UpdateResult>() {
                @Override
                public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                    UpdateResult result = response.body();

                    if (result.getCode() == UpdateResult.RESULT_OK) {
                        Log.d("Meojium/Detail", "Success Adding Favorite Museum");
                        favoriteCheckBox.setChecked(true);
                    } else {
                        Log.d("Meojium/Detail", "Fail Adding Favorite Museum");
                        Toasty.info(DetailActivity.this, "서버 연결에 실패했습니다").show();
                    }
                }

                @Override
                public void onFailure(Call<UpdateResult> call, Throwable t) {
                    Toasty.info(DetailActivity.this, "서버 연결에 실패했습니다").show();
                }
            });
        }
    }

    @OnClick(R.id.relativelayout_detail_stamp_container)
    public void onClickStamp(View view) {
        if (stampCheckBox.isChecked()) {
            Call<UpdateResult> call = stampDao.deleteStampMuseum(SharedPreferencesService.getInstance().getStringData(SharedPreferencesService.KEY_ENC_ID),
                    museum.getId());
            call.enqueue(new Callback<UpdateResult>() {
                @Override
                public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                    UpdateResult result = response.body();
                    if (result.getCode() == UpdateResult.RESULT_OK) {
                        Log.d("Meojium/Detail", "Success Deleting Stamp Museum");
                        stampCheckBox.setChecked(false);
                    } else {
                        Log.d("Meojium/Detail", "Fail Deleting Stamp Museum");
                        Toasty.info(DetailActivity.this, "서버 연결에 실패했습니다").show();
                    }
                }

                @Override
                public void onFailure(Call<UpdateResult> call, Throwable t) {
                    Toasty.info(DetailActivity.this, "서버 연결에 실패했습니다").show();
                }
            });
        } else {
            Call<UpdateResult> call = stampDao.addStampMuseum(SharedPreferencesService.getInstance().getStringData(SharedPreferencesService.KEY_ENC_ID),
                    museum.getId());
            call.enqueue(new Callback<UpdateResult>() {
                @Override
                public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                    UpdateResult result = response.body();

                    if (result.getCode() == UpdateResult.RESULT_OK) {
                        Log.d("Meojium/Detail", "Success Adding Stamp Museum");
                        stampCheckBox.setChecked(true);
                    } else {
                        Log.d("Meojium/Detail", "Fail Adding Stamp Museum");
                        Toasty.info(DetailActivity.this, "서버 연결에 실패했습니다").show();
                    }
                }

                @Override
                public void onFailure(Call<UpdateResult> call, Throwable t) {
                    Toasty.info(DetailActivity.this, "서버 연결에 실패했습니다").show();
                }
            });
        }
    }

    @OnClick(R.id.relativelayout_detail_review_container)
    public void onClickReview(View view) {
        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra("museum", Parcels.wrap(museum));
        startActivityForResult(intent, REQUEST_REVIEW_WRITE);
    }

    private Museum museum;
    private NMapContext mapContext;
    private MaterialSheetFab materialSheetFab;
    private MuseumDao museumDao;
    private StoryDao storyDao;
    private ReviewDao reviewDao;
    private FavoriteDao favoriteDao;
    private StampDao stampDao;
    private List<String> imageUrlList;
    private List<Story> storyList;
    private List<Review> reviewList;

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
                cascadeIntent.putExtra("story", intent.getParcelableExtra("story"));
                startActivity(cascadeIntent);
            }

            Log.d("Meojium/Detail", "Museum id: " + museum.getId());
        } catch (Exception e) {
            e.printStackTrace();
            Toasty.info(this, "일시적인 오류가 발생했습니다.").show();
            onBackPressed();
        }

        museumDao = MuseumDao.getInstance();
        storyDao = StoryDao.getInstance();
        reviewDao = ReviewDao.getInstance();
        favoriteDao = FavoriteDao.getInstance();
        stampDao = StampDao.getInstance();

        requestReviewData();
        requestStoryData();
        requestFavoriteCheckValue();
        requestStampCheckValue();
        requestThumbImage();

        initToolbar();
        initMapView();

        updateMuseumView();
    }

    private void requestReviewData() {
        Call<List<Review>> call = reviewDao.getReviewList(museum.getId());
        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                reviewList = response.body();

                if (reviewList != null && reviewList.size() != 0) {
                    reviewRecyclerView.setVisibility(View.VISIBLE);
                    nothingReviewTextView.setVisibility(View.GONE);
                    reviewAllTextView.setText(getResources().getString(R.string.review_all));
                    initReviewRecyclerView();
                } else {
                    nothingReviewTextView.setVisibility(View.VISIBLE);
                    reviewRecyclerView.setVisibility(View.GONE);
                    reviewAllTextView.setText(getResources().getString(R.string.review_write));
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Toasty.info(DetailActivity.this, "서버 연결에 실패했습니다").show();
            }
        });
    }

    private void initReviewRecyclerView() {
        ReviewRecyclerViewAdapter adapter = new ReviewRecyclerViewAdapter(reviewList, this);
        reviewRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reviewRecyclerView.setLayoutManager(manager);

        reviewRecyclerView.setNestedScrollingEnabled(false);
        reviewRecyclerView.setLayoutFrozen(true);
    }

    private void requestStoryData() {
        Call<List<Story>> call = storyDao.getStoryTitleList(museum.getId());
        call.enqueue(new Callback<List<Story>>() {
            @Override
            public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
                storyList = response.body();

                initFloatingActionButton();
            }

            @Override
            public void onFailure(Call<List<Story>> call, Throwable t) {
                Toasty.info(DetailActivity.this, "서버 연결에 실패했습니다").show();
            }
        });
    }

    private void initFloatingActionButton() {
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlayView,
                android.R.color.white, R.color.colorAccent);

        addButtonToSheet();
    }

    private void addButtonToSheet() {
        if (storyList != null && storyList.size() != 0) {
            for (final Story story : storyList) {
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
        } else {
            Button button = new Button(this);
            button.setText("준비 중입니다.");
            button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
            sheetContainer.addView(button);
        }
    }

    private void requestFavoriteCheckValue() {
        Call<UpdateResult> call = favoriteDao.isCheckedMuseum(SharedPreferencesService.getInstance().getStringData(SharedPreferencesService.KEY_ENC_ID),
                museum.getId());
        call.enqueue(new Callback<UpdateResult>() {
            @Override
            public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                UpdateResult result = response.body();

                if (result.getCode() == UpdateResult.RESULT_OK) {
                    Log.d("Meojium/Detail", "Success Checking Favorite Value");

                    if (result.getMsg().equals("Empty")) {
                        favoriteCheckBox.setChecked(false);
                    } else {
                        favoriteCheckBox.setChecked(true);
                    }
                } else {
                    Log.d("Meojium/Detail", "Fail Checking Favorite Value");
                    Toasty.info(DetailActivity.this, "서버 연결에 실패했습니다").show();
                }
            }

            @Override
            public void onFailure(Call<UpdateResult> call, Throwable t) {
                Toasty.info(DetailActivity.this, "서버 연결에 실패했습니다").show();
            }
        });
    }

    private void requestStampCheckValue() {
        Call<UpdateResult> call = stampDao.isCheckedMuseum(SharedPreferencesService.getInstance().getStringData(SharedPreferencesService.KEY_ENC_ID),
                museum.getId());
        call.enqueue(new Callback<UpdateResult>() {
            @Override
            public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                UpdateResult result = response.body();

                if (result.getCode() == UpdateResult.RESULT_OK) {
                    Log.d("Meojium/Detail", "Success Checking Stamp Value");

                    if (result.getMsg().equals("Empty")) {
                        stampCheckBox.setChecked(false);
                    } else {
                        stampCheckBox.setChecked(true);
                    }
                } else {
                    Log.d("Meojium/Detail", "Fail Checking Stamp Value");
                    Toasty.info(DetailActivity.this, "서버 연결에 실패했습니다").show();
                }
            }

            @Override
            public void onFailure(Call<UpdateResult> call, Throwable t) {
                Toasty.info(DetailActivity.this, "서버 연결에 실패했습니다").show();
            }
        });
    }

    private void requestThumbImage() {
        Call<List<String>> call = museumDao.getMuseumImageUrlList(museum.getId());
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<String> list = response.body();
                imageUrlList = new ArrayList<>();

                for (String item : list) {
                    imageUrlList.add(BaseRetrofitService.IMAGE_LOAD_URL + item);
                }
                initThumbViewPager();
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toasty.info(DetailActivity.this, "서버 연결에 실패했습니다").show();
            }
        });
    }

    private void initThumbViewPager() {
        final ThumbViewPagerAdapter adapter = new ThumbViewPagerAdapter(imageUrlList, this);
        adapter.setViewPager(thumbViewPager);
        thumbViewPager.setAdapter(adapter);
        thumbViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                adapter.setCurrentPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        thumbTabLayout.setupWithViewPager(thumbViewPager);
        thumbTabLayout.requestDisallowInterceptTouchEvent(false);
    }

    private void initToolbar() {
        toolbar.setTitle(museum.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private void updateMuseumView() {
        nameTextView.setText(museum.getName());
        addressTextView.setText(museum.getAddress());
        businessHourTextView.setText(museum.getBusinessHours());
        dayOffTextView.setText(museum.getDayOff());
        feeTextView.setText(museum.getFee());
        telTextView.setText(museum.getTel());
        homepageTextView.setText(museum.getHomepage());
        introTextView.setText(museum.getIntro());
        foundDateTextView.setText(museum.getFoundDate());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_REVIEW_WRITE) {
            if (resultCode == RESULT_OK) {
                if (data.getBooleanExtra("isUpdated", false)) {
                    requestReviewData();
                }
            }
        }
    }
}
