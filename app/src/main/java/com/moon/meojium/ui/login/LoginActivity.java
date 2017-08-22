package com.moon.meojium.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.moon.meojium.R;
import com.moon.meojium.base.util.SharedPreferencesService;
import com.moon.meojium.database.dao.UserDao;
import com.moon.meojium.model.UpdateResult;
import com.moon.meojium.model.user.User;
import com.moon.meojium.ui.home.HomeActivity;
import com.moon.meojium.ui.login.naver.NaverLogin;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.button_login_naver_id)
    OAuthLoginButton oAuthLoginButton;

    private NaverLogin naverLogin;
    private SessionCallback callback;
    private UserDao userDao;
    private String id, nickname;
    private SharedPreferencesService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        userDao = UserDao.getInstance();
        service = SharedPreferencesService.getInstance();

        initLoginButton();
    }

    private void initLoginButton() {
        initKakaoLogin();
        initNaverLogin();
    }

    private void initKakaoLogin() {
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    private void initNaverLogin() {
        naverLogin = new NaverLogin(this);
        naverLogin.initHandlerListener();
        naverLogin.initLoginButton(oAuthLoginButton);

        oAuthLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                naverLogin.startNaverLoginActivity(LoginActivity.this);
            }
        });
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            UserManagement.requestMe(new MeResponseCallback() {
                @Override
                public void onSessionClosed(ErrorResult errorResult) {

                }

                @Override
                public void onNotSignedUp() {

                }

                @Override
                public void onSuccess(UserProfile result) {
                    Log.d("Meojium/KakaoLogin", "Kakao login is successful");

                    id = String.valueOf(result.getId());
                    nickname = result.getNickname();

                    Call<User> call = userDao.isExistedUser(id);
                    call.enqueue(new retrofit2.Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User user = response.body();

                            if (user != null && user.getNickname() != null) {
                                Log.d("Meojium/KakaoLogin", "Exist User");

                                nickname = user.getNickname();

                                Log.d("Meojium/KakaoLogin", "nickname: " + nickname);
                                Log.d("Meojium/KakaoLogin", "encId: " + id);

                                saveAndStart();
                            } else {
                                Log.d("Meojium/KakaoLogin", "Not Exist User");

                                Call<UpdateResult> call2 = userDao.addUser(id, nickname);

                                call2.enqueue(new retrofit2.Callback<UpdateResult>() {
                                    @Override
                                    public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                                        UpdateResult result = response.body();

                                        if (result.getCode() == UpdateResult.RESULT_OK) {
                                            Log.d("Meojium/KakaoLogin", "Success Adding User Info");

                                            saveAndStart();
                                        } else {
                                            Log.d("Meojium/KakaoLogin", "Fail Adding User Info");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UpdateResult> call, Throwable t) {
                                        Log.d("Meojium/KakaoLogin", "Fail Adding User Info");
                                    }
                                });
                            }

                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.d("Meojium/Login", "Fail Checking User Existed");
                        }
                    });
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
        }
    }

    private void saveAndStart() {
        service.putData(SharedPreferencesService.KEY_ENC_ID, id);
        service.putData(SharedPreferencesService.KEY_TYPE, LoginType.KAKAO);
        service.putData(SharedPreferencesService.KEY_NICKNAME, nickname);

        Log.d("Test", "ID: " + id);
        Log.d("Test", "NICKNAME: " + nickname);
        Log.d("Test", "TYPE: " + service.getStringData(SharedPreferencesService.KEY_TYPE));

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

}