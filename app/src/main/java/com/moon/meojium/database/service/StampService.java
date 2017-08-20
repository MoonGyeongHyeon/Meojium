package com.moon.meojium.database.service;

import com.moon.meojium.base.UpdateResult;
import com.moon.meojium.model.museum.Museum;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by moon on 2017. 8. 20..
 */

public interface StampService {
    @POST("/stamp/list")
    @FormUrlEncoded
    Call<List<Museum>> getStampMuseumList(@Field("id") String id, @Query("start") int start);

    @POST("/stamp/checked")
    @FormUrlEncoded
    Call<UpdateResult> isCheckedMuseum(@Field("user_id") String userId, @Field("museum_id") int museumId);

    @POST("/stamp/add")
    @FormUrlEncoded
    Call<UpdateResult> addStampMuseum(@Field("user_id") String userId, @Field("museum_id") int museumId);

    @POST("/stamp/delete")
    @FormUrlEncoded
    Call<UpdateResult> deleteStampMuseum(@Field("user_id") String userId, @Field("museum_id") int museumId);
}
