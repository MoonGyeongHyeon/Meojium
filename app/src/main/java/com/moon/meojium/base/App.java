package com.moon.meojium.base;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.moon.meojium.base.util.SharedPreferencesService;

/**
 * Created by moon on 2017. 8. 12..
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
        SharedPreferencesService.getInstance().init(this);
    }
}
