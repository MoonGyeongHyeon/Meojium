package com.moon.meojium.model.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by moon on 2017. 8. 23..
 */

public class Info {
    @SerializedName("favoriteCount")
    private int favoriteCount;
    @SerializedName("stampCount")
    private int stampCount;
    @SerializedName("registeredDate")
    String registeredDate;

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getStampCount() {
        return stampCount;
    }

    public void setStampCount(int stampCount) {
        this.stampCount = stampCount;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }
}
