package com.moon.meojium.model.searchlog;

/**
 * Created by moon on 2017. 8. 3..
 */

public class SearchLog {
    private int id;
    private String keyword;
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
