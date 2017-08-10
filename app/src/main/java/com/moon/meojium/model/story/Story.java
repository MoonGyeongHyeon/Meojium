package com.moon.meojium.model.story;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by moon on 2017. 8. 3..
 */

@Parcel
public class Story {
    int id;
    String title;
    List<StoryContent> contentList;

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

    public List<StoryContent> getContentList() {
        return contentList;
    }

    public void setContentList(List<StoryContent> contentList) {
        this.contentList = contentList;
    }
}
