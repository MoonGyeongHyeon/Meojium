package com.moon.meojium.model.story;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by moon on 2017. 8. 3..
 */

@Parcel
public class Story {
    @SerializedName("id")
    int id;
    @SerializedName("story_title")
    String title;

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
}
