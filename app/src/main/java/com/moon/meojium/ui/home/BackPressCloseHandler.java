package com.moon.meojium.ui.home;

import android.app.Activity;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * Created by moon on 2017. 8. 10..
 */

public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toasty.info(activity,  "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.");
        toast.show();
    }
}