package com.moon.meojium.model.story;

/**
 * Created by moon on 2017. 8. 3..
 */

public class StoryContent {
    private String imageURL;
    private String content;
    private int priority;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
