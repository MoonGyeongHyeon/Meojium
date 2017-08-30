package com.moon.meojium.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
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
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.button_login_naver_id)
    OAuthLoginButton naverLoginButton;
    @BindView(R.id.button_login_kakao_id)
    LoginButton kakaoLoginButton;
    @BindView(R.id.button_login_google_id)
    SignInButton googleLoginButton;

    @OnClick(R.id.imageview_login_naver)
    public void onNaverClick(View view) {
        naverLoginButton.performClick();
    }

    @OnClick(R.id.imageview_login_kakao)
    public void onKakaoClick(View view) {
        kakaoLoginButton.performClick();
    }

    @OnClick(R.id.button_login_google_id)
    public void onClick() {
        signIn();
    }

    private NaverLogin naverLogin;
    private SessionCallback callback;
    private UserDao userDao;
    private String id, nickname;
    private SharedPreferencesService service;
    private GoogleApiClient googleApiClient;
    private int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        userDao = UserDao.getInstance();
        service = SharedPreferencesService.getInstance();

        initLoginButton();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            id = acct.getId();
            nickname = acct.getDisplayName();

            insertUser(LoginType.GOOGLE);
        }
    }

    private void initLoginButton() {
        initKakaoLogin();
        initNaverLogin();
        initGoogleLogin();
    }

    private void initKakaoLogin() {
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    private void initNaverLogin() {
        naverLogin = new NaverLogin(this);
        naverLogin.initHandlerListener();
        naverLogin.initLoginButton(naverLoginButton);

        naverLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                naverLogin.startNaverLoginActivity(LoginActivity.this);
            }
        });
    }

    private void initGoogleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_web_api))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleLoginButton.setSize(SignInButton.SIZE_WIDE);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

                    insertUser(LoginType.KAKAO);
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

    private void insertUser(final String type) {
        Call<User> call = userDao.isExistedUser(id);
        call.enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();

                if (user != null && user.getNickname() != null) {
                    Log.d("Meojium/Login", "Exist User");

                    nickname = user.getNickname();

                    Log.d("Meojium/Login", "nickname: " + nickname);
                    Log.d("Meojium/Login", "id: " + id);

                    saveAndStart(type);
                } else {
                    Log.d("Meojium/Login", "Not Exist User");

                    Call<UpdateResult> call2 = userDao.addUser(id, nickname);

                    call2.enqueue(new retrofit2.Callback<UpdateResult>() {
                        @Override
                        public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                            UpdateResult result = response.body();

                            if (result.getCode() == UpdateResult.RESULT_OK) {
                                Log.d("Meojium/Login", "Success Adding User Info");

                                saveAndStart(type);
                            } else {
                                Log.d("Meojium/Login", "Fail Adding User Info");
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateResult> call, Throwable t) {
                            Log.d("Meojium/Login", "Fail Adding User Info");
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

    private void saveAndStart(String type) {
        service.putData(SharedPreferencesService.KEY_ENC_ID, id);
        service.putData(SharedPreferencesService.KEY_TYPE, type);
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

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

}