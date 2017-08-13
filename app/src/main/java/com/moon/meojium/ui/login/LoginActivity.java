package com.moon.meojium.ui.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.moon.meojium.R;
import com.moon.meojium.base.util.SharedPreferencesService;
import com.moon.meojium.ui.login.naver.NaverLogin;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.button_login_naver_id)
    OAuthLoginButton oAuthLoginButton;
    private NaverLogin naverLogin;
    private SharedPreferencesService sharedPreferencesService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initSharedPreferencesService();
        initLoginButton();

        checkLogin();
    }

    private void initSharedPreferencesService() {
        sharedPreferencesService = SharedPreferencesService.getInstance();
    }

    private void initLoginButton() {
        initNaverLoginButton();
    }

    private void initNaverLoginButton() {
        naverLogin = NaverLogin.getInstance();
        naverLogin.init(this);

        naverLogin.initLoginButton(oAuthLoginButton);

        oAuthLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                naverLogin.startNaverLoginActivity(LoginActivity.this);
            }
        });
    }

    private void checkLogin() {
        String token = sharedPreferencesService.getStringData("token");
        if (!token.equals("")) {
            String tokenType = sharedPreferencesService.getStringData("tokenType");

            Log.d("Meojium/Login", tokenType + " Already Login");

            switch (tokenType) {
                case NaverLogin.TOKEN_TYPE:
                    naverLogin.startNaverLoginActivity(LoginActivity.this);
                    break;
            }
        }
    }
}