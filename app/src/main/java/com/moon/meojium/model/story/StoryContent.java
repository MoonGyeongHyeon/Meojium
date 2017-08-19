package com.moon.meojium.model.story;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by moon on 2017. 8. 3..
 */

@Parcel
public class StoryContent {
    @SerializedName("image_path")
    String imagePath;
    @SerializedName("content")
    String content;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
