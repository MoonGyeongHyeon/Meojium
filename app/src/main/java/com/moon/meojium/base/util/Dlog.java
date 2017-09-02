package com.moon.meojium.base.util;

import android.util.Log;

import com.moon.meojium.base.MeojiumApplication;

/**
 * Created by moon on 2017. 9. 2..
 */

public class Dlog {
    static final String TAG = "Moon";

    public static final void e(String message) {
        if (MeojiumApplication.DEBUG) Log.e(TAG, buildLogMsg(message));
    }

    public static final void w(String message) {
        if (MeojiumApplication.DEBUG) Log.w(TAG, buildLogMsg(message));
    }

    public static final void i(String message) {
        if (MeojiumApplication.DEBUG) Log.i(TAG, buildLogMsg(message));
    }

    public static final void d(String message) {
        if (MeojiumApplication.DEBUG) Log.d(TAG, buildLogMsg(message));
    }

    public static final void v(String message) {
        if (MeojiumApplication.DEBUG) Log.v(TAG, buildLogMsg(message));
    }

    private static String buildLogMsg(String message) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(ste.getFileName().replace(".java", ""));
        sb.append("::");
        sb.append(ste.getMethodName());
        sb.append("]");
        sb.append(message);
        return sb.toString();
    }
}
