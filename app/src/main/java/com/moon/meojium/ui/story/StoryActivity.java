package com.moon.meojium.ui.story;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.moon.meojium.base.BaseRetrofitService;
import com.moon.meojium.base.FrescoImageViewer;
import com.moon.meojium.base.ImageOverlayView;
import com.moon.meojium.database.dao.StoryContentDao;
import com.moon.meojium.model.story.Story;
import com.moon.meojium.model.story.StoryContent;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by moon on 2017. 8. 9..
 */

public class StoryActivity extends AppCompatActivity
        implements ImageViewer.OnDismissListener,
        FrescoImageViewer {
    private Story story;
    private List<StoryContent> storyContentList;
    private List<String> imageUrlList, contentList;
    private ImageOverlayView overlayView;
    private StoryContentDao storyContentDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        try {
            story = Parcels.unwrap(intent.getParcelableExtra("story"));

            Log.d("Meojium/Story", "Story id: " + story.getId());
            Log.d("Meojium/Story", "Story id: " + story.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
            Toasty.info(this, "일시적인 오류가 발생했습니다.").show();
            onBackPressed();
        }

        storyContentDao = StoryContentDao.getInstance();

        requestStoryContentData();
    }

    private void requestStoryContentData() {
        Call<List<StoryContent>> call = storyContentDao.getStoryContentList(story.getId());
        call.enqueue(new Callback<List<StoryContent>>() {
            @Override
            public void onResponse(Call<List<StoryContent>> call, Response<List<StoryContent>> response) {
                storyContentList = response.body();

                splitStoryContentToImageUrlAndContentValue();
                showPicker();
            }

            @Override
            public void onFailure(Call<List<StoryContent>> call, Throwable t) {
                Toasty.info(StoryActivity.this, "서버 연결에 실패했습니다").show();
            }
        });
    }

    private void splitStoryContentToImageUrlAndContentValue() {
        imageUrlList = new ArrayList<>();
        contentList = new ArrayList<>();

        for (StoryContent content : storyContentList) {
            imageUrlList.add(BaseRetrofitService.IMAGE_LOAD_URL + content.getImagePath());
            contentList.add(content.getContent());
        }
    }

    @Override
    public void showPicker() {
        ImageViewer.Builder builder = new ImageViewer.Builder<>(this, imageUrlList)
                .setStartPosition(0);

        overlayView = new ImageOverlayView(this);
        builder.setOverlayView(overlayView);
        builder.setImageChangeListener(getImageChangeListener());
        builder.setOnDismissListener(this);

        builder.show();
    }

    private ImageViewer.OnImageChangeListener getImageChangeListener() {
        return new ImageViewer.OnImageChangeListener() {
            @Override
            public void onImageChange(int position) {
                overlayView.setContent(contentList.get(position));
            }
        };
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
    public void onDismiss() {
        finish();
    }
}
