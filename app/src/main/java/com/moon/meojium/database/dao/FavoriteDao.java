package com.moon.meojium.database.dao;

import com.moon.meojium.base.BaseRetrofitService;
import com.moon.meojium.base.UpdateResult;
import com.moon.meojium.database.service.FavoriteService;
import com.moon.meojium.model.museum.Museum;

import java.util.List;

import retrofit2.Call;

/**
 * Created by moon on 2017. 8. 20..
 */

public class FavoriteDao extends BaseRetrofitService {
    private static FavoriteDao dao;
    private FavoriteService service;

    public static FavoriteDao getInstance() {
        if (dao == null) {
            synchronized (FavoriteDao.class) {
                if (dao == null) {
                    dao = new FavoriteDao();
                }
            }
        }
        return dao;
    }

    private FavoriteDao() {
        init();
        setClass(FavoriteService.class);
    }

    private void setClass(Class<?> type) {
        service = (FavoriteService) retrofit.create(type);
    }

    public Call<List<Museum>> getFavoriteMuseumList(String id, int start) {
        return service.getFavoriteMuseumList(id, start);
    }

    public Call<UpdateResult> isCheckedMuseum(String userId, int museumId) {
        return service.isCheckedMuseum(userId, museumId);
    }

    public Call<UpdateResult> addFavoriteMuseum(String userId, int museumId) {
        return service.addFavoriteMuseum(userId, museumId);
    }

    public Call<UpdateResult> deleteFavoriteMuseum(String userId, int museumId) {
        return service.deleteFavoriteMuseum(userId, museumId);
    }
}
