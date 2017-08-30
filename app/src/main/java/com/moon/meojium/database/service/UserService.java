package com.moon.meojium.database.service;

import com.moon.meojium.model.UpdateResult;
import com.moon.meojium.model.user.Info;
import com.moon.meojium.model.user.User;

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

    @POST("/user/update")
    @FormUrlEncoded
    Call<UpdateResult> updateNickname(@Field("id") String id, @Field("nickname") String nickname);

    @POST("/user/exist")
    @FormUrlEncoded
    Call<User> isExistedUser(@Field("id") String id);

    @POST("/user/info")
    @FormUrlEncoded
    Call<Info> getUserInfo(@Field("id") String id);

    @POST("/user/close")
    @FormUrlEncoded
    Call<UpdateResult> deleteUser(@Field("id") String id);
}
