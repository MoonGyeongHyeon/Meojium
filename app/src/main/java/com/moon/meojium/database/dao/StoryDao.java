package com.moon.meojium.database.dao;

import com.moon.meojium.base.BaseRetrofitService;
import com.moon.meojium.database.service.StoryService;
import com.moon.meojium.model.story.Story;

import java.util.List;

import retrofit2.Call;

/**
 * Created by moon on 2017. 8. 18..
 */

public class StoryDao extends BaseRetrofitService {
    private static StoryDao dao;
    private StoryService service;

    public static StoryDao getInstance() {
        if (dao == null) {
            synchronized (StoryDao.class) {
                if (dao == null) {
                    dao = new StoryDao();
                }
            }
        }
        return dao;
    }

    private StoryDao() {
        init();
        setClass(StoryService.class);
    }

    private void setClass(Class<?> type) {
        service = (StoryService) retrofit.create(type);
    }

    public Call<List<Story>> getStoryTitleList(int id) {
        return service.getStoryTitleList(id);
    }

}
