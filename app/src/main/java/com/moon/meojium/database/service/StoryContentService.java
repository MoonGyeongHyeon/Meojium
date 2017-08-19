package com.moon.meojium.database.service;

import com.moon.meojium.model.story.StoryContent;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by moon on 2017. 8. 18..
 */

public interface StoryContentService {
    @GET("/content/{id}")
    Call<List<StoryContent>> getStoryContentList(@Path("id") int id);
}
