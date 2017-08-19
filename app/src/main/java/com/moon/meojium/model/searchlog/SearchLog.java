package com.moon.meojium.model.searchlog;

import com.google.gson.annotations.SerializedName;

/**
 * Created by moon on 2017. 8. 3..
 */

public class SearchLog {
    @SerializedName("id")
    private int id;
    @SerializedName("keyword")
    private String keyword;
    @SerializedName("searched_date")
    private String searchedDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSearchedDate() {
        return searchedDate;
    }

    public void setSearchedDate(String searchedDate) {
        this.searchedDate = searchedDate;
    }
}
