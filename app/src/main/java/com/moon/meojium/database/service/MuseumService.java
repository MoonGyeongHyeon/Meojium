package com.moon.meojium.database.service;

import com.moon.meojium.model.museum.Museum;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by moon on 2017. 8. 17..
 */

public interface MuseumService {
    @GET("/museum/{id}")
    Call<Museum> getMuseum(@Path("id") int id);

    @GET("/museum/list/all")
    Call<List<Museum>> getAllMuseumList(@Query("start") int start);

    @GET("/museum/list/popular")
    Call<List<Museum>> getPopularMuseumList();

    @GET("/museum/list/history")
    Call<List<Museum>> getHistoryMuseumList();

    @GET("/museum/{id}/image")
    Call<List<String>> getMuseumImageUrlList(@Path("id") int id);

    @GET("/museum/list/nearby")
    Call<List<Museum>> getNearbyMuseumList(@Query("latitude") double latitude, @Query("longitude") double longitude, @Query("distance") double distance);

    @GET("/museum/list/area")
    Call<List<Museum>> getAreaMuseumList();
}
