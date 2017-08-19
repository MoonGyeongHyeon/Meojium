package com.moon.meojium.base;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by moon on 2017. 8. 3..
 */

public class BaseRetrofitService {
    public static final String BASE_URL = "http://13.124.136.181/";
    public static final String IMAGE_LOAD_URL = BASE_URL + "image/load?path=";

    protected Retrofit retrofit;

    public void init() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
