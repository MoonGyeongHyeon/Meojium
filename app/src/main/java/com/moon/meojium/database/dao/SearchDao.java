package com.moon.meojium.database.dao;

import com.moon.meojium.base.BaseRetrofitService;
import com.moon.meojium.base.UpdateResult;
import com.moon.meojium.database.service.SearchService;
import com.moon.meojium.model.museum.Museum;
import com.moon.meojium.model.searchlog.SearchLog;

import java.util.List;

import retrofit2.Call;

/**
 * Created by moon on 2017. 8. 18..
 */

public class SearchDao extends BaseRetrofitService {
    private static SearchDao dao;
    private SearchService service;

    public static SearchDao getInstance() {
        if (dao == null) {
            synchronized (SearchDao.class) {
                if (dao == null) {
                    dao = new SearchDao();
                }
            }
        }
        return dao;
    }

    private SearchDao() {
        init();
        setClass(SearchService.class);
    }

    private void setClass(Class<?> type) {
        service = (SearchService) retrofit.create(type);
    }

    public Call<List<Museum>> getMuseumListByKeyword(String keyword) {
        return service.getMuseumListByKeyword(keyword);
    }

    public Call<UpdateResult> addSearchLog(String userId, String keyword) {
        return service.addSearchLog(userId, keyword);
    }

    public Call<List<SearchLog>> getSearchLogList(String id) {
        return service.getSearchLogList(id);
    }
}
