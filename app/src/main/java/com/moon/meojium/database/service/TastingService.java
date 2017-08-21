package com.moon.meojium.database.service;

import com.moon.meojium.model.tasting.Tasting;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by moon on 2017. 8. 20..
 */

public interface TastingService {
    @GET("/tasting")
    Call<List<Tasting>> getTastingMuseumList();
}
