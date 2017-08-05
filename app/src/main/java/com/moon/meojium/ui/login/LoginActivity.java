package com.moon.meojium.ui.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.moon.meojium.R;
import com.moon.meojium.ui.login.naver.NaverLogin;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

public class LoginActivity extends AppCompatActivity {
    private OAuthLoginButton oAuthLoginButton;
    private NaverLogin naverLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initLoginButton();
    }

    private void initLoginButton() {
        initNaverLoginButton();
    }

    private void initNaverLoginButton() {
        oAuthLoginButton = (OAuthLoginButton) findViewById(R.id.button_login_naver_id);
        naverLogin = new NaverLogin(this);
        naverLogin.initLoginButton(oAuthLoginButton);

        oAuthLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                naverLogin.startNaverLoginActivity(LoginActivity.this);
            }
        });

    }
}