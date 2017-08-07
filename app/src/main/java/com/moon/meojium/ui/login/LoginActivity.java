package com.moon.meojium.ui.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.moon.meojium.R;
import com.moon.meojium.ui.login.naver.NaverLogin;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.button_login_naver_id)
    OAuthLoginButton oAuthLoginButton;
    private NaverLogin naverLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initLoginButton();
    }

    private void initLoginButton() {
        initNaverLoginButton();
    }

    private void initNaverLoginButton() {
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