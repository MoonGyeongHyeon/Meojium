package com.moon.meojium.ui.login.naver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.moon.meojium.R;
import com.moon.meojium.base.util.Dlog;
import com.moon.meojium.base.util.SharedPreferencesService;
import com.moon.meojium.database.dao.UserDao;
import com.moon.meojium.model.UpdateResult;
import com.moon.meojium.model.user.User;
import com.moon.meojium.ui.home.HomeActivity;
import com.moon.meojium.ui.login.LoginType;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.data.OAuthLoginState;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by moon on 2017. 8. 4..
 */

public class NaverLogin {
    private static final String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    private OAuthLogin oAuthLoginInstance;
    private Context context;
    private OAuthLoginHandler oAuthLoginHandler;
    private String nickname;
    private String encId;
    private UserDao userDao;
    private SharedPreferencesService service;

    public NaverLogin(Context context) {
        this.context = context;

        userDao = UserDao.getInstance();
        service = SharedPreferencesService.getInstance();
        oAuthLoginInstance = OAuthLogin.getInstance();
        oAuthLoginInstance.init(context, context.getResources().getString(R.string.naver_client_id),
                context.getResources().getString(R.string.naver_client_secret), OAUTH_CLIENT_NAME);
    }

    public void initHandlerListener() {
        oAuthLoginHandler = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                if (success) {
                    Dlog.d("Naver login is successful");

                    requestUserInfo();

                    Call<User> call = userDao.isExistedUser(encId);
                    call.enqueue(new retrofit2.Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User user = response.body();

                            if (user != null && user.getNickname() != null) {
                                Dlog.d("Exist User");

                                nickname = user.getNickname();

                                Dlog.d("nickname: " + nickname);
                                Dlog.d("encId: " + encId);

                                saveAndStart();
                            } else {
                                Dlog.d("Not Exist User");

                                Call<UpdateResult> call2 = userDao.addUser(encId, nickname);

                                call2.enqueue(new retrofit2.Callback<UpdateResult>() {
                                    @Override
                                    public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                                        UpdateResult result = response.body();

                                        if (result.getCode() == UpdateResult.RESULT_OK) {
                                            Dlog.d("Success Adding User Info");

                                            saveAndStart();
                                        } else {
                                            Dlog.d("Fail Adding User Info");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UpdateResult> call, Throwable t) {
                                        Dlog.d("Fail Adding User Info");
                                    }
                                });
                            }

                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Dlog.d("Fail Checking User Existed");
                        }
                    });

                } else {
                    String errorCode = oAuthLoginInstance.getLastErrorCode(context).getCode();
                    String errorDesc = oAuthLoginInstance.getLastErrorDesc(context);
                    Dlog.d("errorCode:" + errorCode + ", errorDesc:" + errorDesc);
                }
            }
        };
    }

    private void saveAndStart() {
        service.putData(SharedPreferencesService.KEY_ENC_ID, encId);
        service.putData(SharedPreferencesService.KEY_TYPE, LoginType.NAVER);
        service.putData(SharedPreferencesService.KEY_NICKNAME, nickname);

        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void startNaverLoginActivity(Activity activity) {
        oAuthLoginInstance.startOauthLoginActivity(activity, oAuthLoginHandler);
    }

    public void logout() {
        Dlog.d("Logout Naver");

        oAuthLoginInstance.logout(context);
    }

    private void requestUserInfo() {
        RequestApiTask task = new RequestApiTask();
        String[] result;

        try {
            result = task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            nickname = result[0];
            encId = result[1];
        } catch (InterruptedException e) {
            Dlog.d("InterruptedException is occurred");
            e.printStackTrace();
        } catch (ExecutionException e) {
            Dlog.d("executionException is occurred");
            e.printStackTrace();
        }
    }

    private class RequestApiTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            String url = "https://openapi.naver.com/v1/nid/me";
            String at = oAuthLoginInstance.getAccessToken(context);
            String body = oAuthLoginInstance.requestApi(context, at, url);

            String[] result = new String[2];

            try {
                JSONObject root = new JSONObject(body);
                JSONObject jo = root.getJSONObject("response");
                result[0] = jo.getString("nickname");
                result[1] = jo.getString("enc_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    public boolean isLogin() {
        return oAuthLoginInstance.getState(context) == OAuthLoginState.OK;
    }

    public void initLoginButton(OAuthLoginButton button) {
        button.setOAuthLoginHandler(oAuthLoginHandler);
    }
}
