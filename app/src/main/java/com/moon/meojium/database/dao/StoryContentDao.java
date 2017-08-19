package com.moon.meojium.database.dao;

import com.moon.meojium.base.BaseRetrofitService;
import com.moon.meojium.database.service.StoryContentService;
import com.moon.meojium.model.story.StoryContent;

import java.util.List;

import retrofit2.Call;

/**
 * Created by moon on 2017. 8. 18..
 */

public class StoryContentDao extends BaseRetrofitService {
    private static StoryContentDao dao;
    private StoryContentService service;

    public static StoryContentDao getInstance() {
        if (dao == null) {
            synchronized (StoryContentDao.class) {
                if (dao == null) {
                    dao = new StoryContentDao();
                }
            }
        }
        return dao;
    }

    private StoryContentDao() {
        init();
        setClass(StoryContentService.class);
    }

    private void setClass(Class<?> type) {
        service = (StoryContentService) retrofit.create(type);
    }

    public Call<List<StoryContent>> getStoryContentList(int id) {
        return service.getStoryContentList(id);
    }
}
