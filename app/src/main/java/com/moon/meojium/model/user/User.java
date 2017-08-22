package com.moon.meojium.model.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by moon on 2017. 8. 22..
 */

public class User {
    @SerializedName("nickname")
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
