package com.moon.meojium.base.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by moon on 2017. 7. 21..
 */

public class PermissionChecker {
    public static final int REQUEST_LOCATION_INFO = 1;

    public static boolean checkPermission(Context context, String permission) {
        int permissionResult = ActivityCompat.checkSelfPermission(context, permission);

        return permissionResult == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean verifyPermission(int[] grantResults) {
        if (grantResults.length == 0)
            return false;

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

}