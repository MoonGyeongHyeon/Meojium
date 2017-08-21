package com.moon.meojium.database.service;

import com.moon.meojium.model.UpdateResult;
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

public interface FavoriteService {
    @POST("/favorite/list")
    @FormUrlEncoded
    Call<List<Museum>> getFavoriteMuseumList(@Field("id") String id, @Query("start") int startIndex);

    @POST("/favorite/checked")
    @FormUrlEncoded
    Call<UpdateResult> isCheckedMuseum(@Field("user_id") String userId, @Field("museum_id") int museumId);

    @POST("/favorite/add")
    @FormUrlEncoded
    Call<UpdateResult> addFavoriteMuseum(@Field("user_id") String userId, @Field("museum_id") int museumId);

    @POST("/favorite/delete")
    @FormUrlEncoded
    Call<UpdateResult> deleteFavoriteMuseum(@Field("user_id") String userId, @Field("museum_id") int museumId);
}
