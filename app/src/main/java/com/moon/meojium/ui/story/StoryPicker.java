package com.moon.meojium.ui.story;

import android.content.Context;

import com.moon.meojium.R;
import com.moon.meojium.base.BaseRetrofitService;
import com.moon.meojium.base.util.Dlog;
import com.moon.meojium.base.FrescoImageViewer;
import com.moon.meojium.base.ImageOverlayView;
import com.moon.meojium.database.dao.StoryContentDao;
import com.moon.meojium.model.story.Story;
import com.moon.meojium.model.story.StoryContent;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by moon on 2017. 8. 9..
 */

public class StoryPicker implements FrescoImageViewer {
    private Story story;
    private List<StoryContent> storyContentList;
    private List<String> imageUrlList, contentList;
    private ImageOverlayView overlayView;
    private StoryContentDao storyContentDao;
    private Context context;

    public StoryPicker(Story story, Context context) {
        this.story = story;
        this.context = context;

        Dlog.d("Story id: " + story.getId());
        Dlog.d("Story id: " + story.getTitle());

        storyContentDao = StoryContentDao.getInstance();
    }

    public void show() {
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
                Toasty.info(context, context.getResources().getString(R.string.fail_connection)).show();
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
        ImageViewer.Builder builder = new ImageViewer.Builder<>(context, imageUrlList)
                .setStartPosition(0);

        overlayView = new ImageOverlayView(context, imageUrlList.size());
        builder.setOverlayView(overlayView);
        builder.setImageChangeListener(getImageChangeListener());

        builder.show();
    }

    private ImageViewer.OnImageChangeListener getImageChangeListener() {
        return new ImageViewer.OnImageChangeListener() {
            @Override
            public void onImageChange(int position) {
                overlayView.setContent(contentList.get(position));
                overlayView.setPosition(position);
            }
        };
    }
}
