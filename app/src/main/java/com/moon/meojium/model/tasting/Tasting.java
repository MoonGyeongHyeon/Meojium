package com.moon.meojium.model.tasting;

import com.google.gson.annotations.SerializedName;
import com.moon.meojium.model.museum.Museum;

/**
 * Created by moon on 2017. 8. 21..
 */

public class Tasting {
    @SerializedName("id")
    private int id;
    @SerializedName("story_title")
    private String title;
    @SerializedName("museum")
    private Museum museum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Museum getMuseum() {
        return museum;
    }

    public void setMuseum(Museum museum) {
        this.museum = museum;
    }
}
