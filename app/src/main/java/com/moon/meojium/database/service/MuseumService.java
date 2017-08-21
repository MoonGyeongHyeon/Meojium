package com.moon.meojium.database.service;

import com.moon.meojium.model.museum.Museum;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by moon on 2017. 8. 17..
 */

public interface MuseumService {
    @GET("/museum/list/popular")
    Call<List<Museum>> getPopularMuseumList();

    @GET("/museum/list/history")
    Call<List<Museum>> getHistoryMuseumList();

    @GET("/museum/{id}/image")
    Call<List<String>> getMuseumImageUrlList(@Path("id") int id);

}