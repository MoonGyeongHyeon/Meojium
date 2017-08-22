package com.moon.meojium.database.service;

import com.moon.meojium.model.UpdateResult;
import com.moon.meojium.model.museum.Museum;
import com.moon.meojium.model.searchlog.SearchLog;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by moon on 2017. 8. 18..
 */

public interface SearchService {
    @GET("/search")
    Call<List<Museum>> getMuseumListByKeyword(@Query("keyword") String keyword);

    @POST("/search/add")
    @FormUrlEncoded
    Call<UpdateResult> addSearchLog(@Field("user_id") String userId, @Field("keyword") String keyword);

    @POST("/search/list")
    @FormUrlEncoded
    Call<List<SearchLog>> getSearchLogList(@Field("id") String id);

    @POST("/search/delete")
    @FormUrlEncoded
    Call<UpdateResult> deleteSearchLog(@Field("id") String id);

    @POST("/search/update")
    @FormUrlEncoded
    Call<UpdateResult> updateSearchLog(@Field("id") String id, @Field("keyword") String keyword);
}
