package com.moon.meojium.ui.login.naver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.moon.meojium.base.NaverAPI;
import com.moon.meojium.model.UpdateResult;
import com.moon.meojium.base.util.SharedPreferencesService;
import com.moon.meojium.database.dao.UserDao;
import com.moon.meojium.ui.home.HomeActivity;
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

public class NaverLogin implements NaverAPI {
    public static final String NAVER_TYPE = "Naver";
    private static final String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    private OAuthLogin oAuthLoginInstance;
    private Context context;
    private OAuthLoginHandler oAuthLoginHandler;
    private String nickname;
    private String encId;

    public NaverLogin(Context context) {
        this.context = context;

        oAuthLoginInstance = OAuthLogin.getInstance();
        oAuthLoginInstance.init(context, CLIENT_ID, CLIENT_SECRET, OAUTH_CLIENT_NAME);
    }

    public void initHandlerListener() {
        oAuthLoginHandler = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                if (success) {
                    Log.d("Meojium/NaverLogin", "Naver login is successful");

                    requestUserInfo();

                    SharedPreferencesService service = SharedPreferencesService.getInstance();
                    service.putData(SharedPreferencesService.KEY_ENC_ID, encId);
                    service.putData(SharedPreferencesService.KEY_TYPE, NAVER_TYPE);
                    service.putData(SharedPreferencesService.KEY_NICKNAME, nickname);

                    Log.d("Meojium/Login", "nickname: " + nickname);
                    Log.d("Meojium/Login", "encId: " + encId);

                    UserDao userDao = UserDao.getInstance();
                    Call<UpdateResult> call = userDao.addUser(service.getStringData(SharedPreferencesService.KEY_ENC_ID),
                            service.getStringData(SharedPreferencesService.KEY_NICKNAME));

                    call.enqueue(new retrofit2.Callback<UpdateResult>() {
                        @Override
                        public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                            UpdateResult result = response.body();
                            if (result.getCode() == UpdateResult.RESULT_OK) {
                                Log.d("Meojium/Login", "Success Adding User Info");
                            } else {
                                Log.d("Meojium/Login", "Fail Adding User Info");
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateResult> call, Throwable t) {
                            Log.d("Meojium/Login", "Fail Adding User Info");
                        }
                    });

                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                } else {
                    String errorCode = oAuthLoginInstance.getLastErrorCode(context).getCode();
                    String errorDesc = oAuthLoginInstance.getLastErrorDesc(context);
                    Log.d("Meojium/NaverLogin", "errorCode:" + errorCode + ", errorDesc:" + errorDesc);
                }
            }
        };
    }

    public void startNaverLoginActivity(Activity activity) {
        oAuthLoginInstance.startOauthLoginActivity(activity, oAuthLoginHandler);
    }

    public void logout() {
        Log.d("Meojium/NaverLogin", "Logout Naver");

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
            Log.d("Meojium/NaverLogin", "InterruptedException is occurred");
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.d("Meojium/NaverLogin", "executionException is occurred");
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
