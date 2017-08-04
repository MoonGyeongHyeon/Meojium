package com.moon.meojium.model.review;

import com.moon.meojium.model.user.User;

/**
 * Created by moon on 2017. 8. 3..
 */

public class Review {
    private int id;
    private User user;
    private String content;
    private String registeredDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
