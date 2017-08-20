package com.moon.meojium.database.dao;

import com.moon.meojium.base.BaseRetrofitService;
import com.moon.meojium.base.UpdateResult;
import com.moon.meojium.database.service.StampService;
import com.moon.meojium.model.museum.Museum;

import java.util.List;

import retrofit2.Call;

/**
 * Created by moon on 2017. 8. 20..
 */

public class StampDao extends BaseRetrofitService {
    private static StampDao dao;
    private StampService service;

    public static StampDao getInstance() {
        if (dao == null) {
            synchronized (StampDao.class) {
                if (dao == null) {
                    dao = new StampDao();
                }
            }
        }
        return dao;
    }

    private StampDao() {
        init();
        setClass(StampService.class);
    }

    private void setClass(Class<?> type) {
        service = (StampService) retrofit.create(type);
    }

    public Call<List<Museum>> getStampMuseumList(String id, int start) {
        return service.getStampMuseumList(id, start);
    }

    public Call<UpdateResult> isCheckedMuseum(String userId, int museumId) {
        return service.isCheckedMuseum(userId, museumId);
    }

    public Call<UpdateResult> addStampMuseum(String userId, int museumId) {
        return service.addStampMuseum(userId, museumId);
    }

    public Call<UpdateResult> deleteStampMuseum(String userId, int museumId) {
        return service.deleteStampMuseum(userId, museumId);
    }
}
