package com.moon.meojium.database.dao;

import com.moon.meojium.base.BaseRetrofitService;
import com.moon.meojium.model.UpdateResult;
import com.moon.meojium.database.service.ReviewService;
import com.moon.meojium.model.review.Review;

import java.util.List;

import retrofit2.Call;

/**
 * Created by moon on 2017. 8. 19..
 */

public class ReviewDao extends BaseRetrofitService {
    private static ReviewDao dao;
    private ReviewService service;

    public static ReviewDao getInstance() {
        if (dao == null) {
            synchronized (ReviewDao.class) {
                if (dao == null) {
                    dao = new ReviewDao();
                }
            }
        }
        return dao;
    }

    private ReviewDao() {
        init();
        setClass(ReviewService.class);
    }

    private void setClass(Class<?> type) {
        service = (ReviewService) retrofit.create(type);
    }

    public Call<List<Review>> getReviewList(int museumId, int startIndex) {
        return service.getReviewList(museumId, startIndex, 10);
    }

    public Call<List<Review>> getReviewList(int museumId) {
        return service.getReviewList(museumId, 0, 3);
    }

    public Call<UpdateResult> writeReview(String userId, String content, int museumId) {
        return service.writeReview(userId, content, museumId);
    }

    public Call<UpdateResult> deleteReview(int id) {
        return service.deleteReview(id);
    }
}
