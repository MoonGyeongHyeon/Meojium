package com.moon.meojium.database.service;

import com.moon.meojium.model.UpdateResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by moon on 2017. 8. 17..
 */

public interface UserService {
    @POST("/user/add")
    @FormUrlEncoded
    Call<UpdateResult> addUser(@Field("id") String id, @Field("nickname") String nickname);
}
