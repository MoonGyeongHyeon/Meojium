package com.moon.meojium.database.dao;

import com.moon.meojium.base.BaseRetrofitService;
import com.moon.meojium.database.service.TastingService;
import com.moon.meojium.model.tasting.Tasting;

import java.util.List;

import retrofit2.Call;

/**
 * Created by moon on 2017. 8. 20..
 */

public class TastingDao extends BaseRetrofitService {
    private static TastingDao dao;
    private TastingService service;

    public static TastingDao getInstance() {
        if (dao == null) {
            synchronized (TastingDao.class) {
                if (dao == null) {
                    dao = new TastingDao();
                }
            }
        }
        return dao;
    }

    private TastingDao() {
        init();
        setClass(TastingService.class);
    }

    private void setClass(Class<?> type) {
        service = (TastingService) retrofit.create(type);
    }

    public Call<List<Tasting>> getTastingMuseumList() {
        return service.getTastingMuseumList();
    }

}
