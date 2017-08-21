package com.moon.meojium.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by moon on 2017. 8. 17..
 */

public class UpdateResult {
    public static final int RESULT_OK = 1;
    public static final int RESULT_FAIL = -1;

    @SerializedName("code")
    private int code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("insertId")
    private int insertId;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getInsertId() {
        return insertId;
    }

    public void setInsertId(int insertId) {
        this.insertId = insertId;
    }
}
