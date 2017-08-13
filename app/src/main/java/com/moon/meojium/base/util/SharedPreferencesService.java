package com.moon.meojium.base.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by moon on 2017. 8. 3..
 */

public class SharedPreferencesService {
    private static final String SHARED_PREF_NAME = "Meojium";

    private static SharedPreferencesService sharedPreferencesService;
    private SharedPreferences preferences;

    private SharedPreferencesService() {
    }

    public static SharedPreferencesService getInstance() {
        if (sharedPreferencesService == null) {
            synchronized (SharedPreferencesService.class) {
                if (sharedPreferencesService == null) {
                    sharedPreferencesService = new SharedPreferencesService();
                }
            }
        }
        return sharedPreferencesService;
    }

    public void init(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        }
    }

    public long getLongData(String key) {
        return getLongData(key, 0);
    }

    public long getLongData(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    public int getIntegerData(String key) {
        return getIntegerData(key, 0);
    }

    public int getIntegerData(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public float getFloatData(String key) {
        return getFloatData(key, 0.f);
    }

    public float getFloatData(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    public String getStringData(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public String getStringData(String key) {
        return getStringData(key, "");
    }

    public boolean getBooleanData(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public boolean getBooleanData(String key) {
        return getBooleanData(key, false);
    }

    public void putData(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(key, value);
        editor.apply();
    }

    public void putData(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(key, value);
        editor.apply();
    }

    public void putData(String key, float value) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putFloat(key, value);
        editor.apply();
    }

    public void putData(String key, long value) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong(key, value);
        editor.apply();
    }

    public void putData(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(key, value);
        editor.apply();
    }

    public void removeData(String... keys) {
        SharedPreferences.Editor editor = preferences.edit();

        for (String key : keys) {
            editor.remove(key);
        }

        editor.apply();
    }

    public void clear() {
        preferences.edit()
                .clear()
                .apply();
    }
}
