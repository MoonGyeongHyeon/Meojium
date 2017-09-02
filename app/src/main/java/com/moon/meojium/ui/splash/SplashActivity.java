package com.moon.meojium.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.moon.meojium.R;
import com.moon.meojium.base.util.Dlog;
import com.moon.meojium.base.util.SharedPreferencesService;
import com.moon.meojium.ui.home.HomeActivity;
import com.moon.meojium.ui.login.LoginActivity;

/**
 * Created by moon on 2017. 8. 15..
 */

public class SplashActivity extends AppCompatActivity {

    private SharedPreferencesService sharedPreferencesService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferencesService = SharedPreferencesService.getInstance();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                checkLogin();
                finish();
            }
        }, 2000);
    }

    private void checkLogin() {
        String encId = sharedPreferencesService.getStringData(SharedPreferencesService.KEY_ENC_ID);
        if (!encId.equals("")) {
            String tokenType = sharedPreferencesService.getStringData(SharedPreferencesService.KEY_TYPE);
            Dlog.d("encId: " + encId);
            Dlog.d(tokenType + " Already Login");

            startActivity(HomeActivity.class);
        } else {
            startActivity(LoginActivity.class);
        }
    }

    private void startActivity(Class klass) {
        Intent intent = new Intent(this, klass);
        startActivity(intent);
    }
}
