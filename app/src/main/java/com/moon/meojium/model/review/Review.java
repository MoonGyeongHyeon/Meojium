package com.moon.meojium.model.review;

/**
 * Created by moon on 2017. 8. 3..
 */

public class Review {
    private int id;
    private String nickname;
    private String content;
    private String registeredDate;
    private boolean isWriter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }

    public boolean isWriter() {
        return isWriter;
    }

    public void setWriter(boolean writer) {
        isWriter = writer;
    }
}
