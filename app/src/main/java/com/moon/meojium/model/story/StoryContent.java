package com.moon.meojium.model.story;

import org.parceler.Parcel;

/**
 * Created by moon on 2017. 8. 3..
 */

@Parcel
public class StoryContent {
    int image;
    String content;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
