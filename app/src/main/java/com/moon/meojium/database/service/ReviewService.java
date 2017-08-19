package com.moon.meojium.database.service;

import com.moon.meojium.base.UpdateResult;
import com.moon.meojium.model.review.Review;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by moon on 2017. 8. 19..
 */

public interface ReviewService {
    @GET("/review/{id}")
    Call<List<Review>> getReviewList(@Path("id") int museumId, @Query("start") int startIndex, @Query("count") int count);

    @POST("/review/write")
    @FormUrlEncoded
    Call<UpdateResult> writeReview(@Field("user_id") String userId, @Field("content") String content, @Field("museum_id") int museumId);

    @GET("/review/delete/{id}")
    Call<UpdateResult> deleteReview(@Path("id") int id);
}
