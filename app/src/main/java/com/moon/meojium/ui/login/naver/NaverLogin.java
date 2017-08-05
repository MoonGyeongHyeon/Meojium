package com.moon.meojium.ui.login.naver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.moon.meojium.ui.museumlist.MuseumListActivity;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.data.OAuthLoginState;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by moon on 2017. 8. 4..
 */

public class NaverLogin {
    private static String OAUTH_CLIENT_ID = "zp_U6QNm3sWr4V1O7Xa5";
    private static String OAUTH_CLIENT_SECRET = "wCzTb1divC";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    private OAuthLogin oAuthLoginInstance;
    private Context context;

    public NaverLogin(Context context) {
        this.context = context;

        oAuthLoginInstance = OAuthLogin.getInstance();
        oAuthLoginInstance.init(context, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
    }

    private OAuthLoginHandler oAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                Log.d("Meojium/NaverLogin", "Naver login is successful");

                // TODO: SharedPreferences에 토큰 추가하고 DB에 user정보 삽입.

                Intent intent = new Intent(context, MuseumListActivity.class);
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

    public void startNaverLoginActivity(Activity activity) {
        oAuthLoginInstance.startOauthLoginActivity(activity, oAuthLoginHandler);
    }

    public void logout() {
        Log.d("Meojium/NaverLogin", "Logout Naver");

        oAuthLoginInstance.logout(context);
    }

    public String requestUserNickname() {
        RequestApiTask task = new RequestApiTask();
        String nickname = null;

        try {
            nickname = task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
        } catch (InterruptedException e) {
            Log.d("Meojium/NaverLogin", "InterruptedException is occurred");
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.d("Meojium/NaverLogin", "executionException is occurred");
            e.printStackTrace();
        }

        return nickname;
    }

    private class RequestApiTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String url = "https://openapi.naver.com/v1/nid/me";
            String at = oAuthLoginInstance.getAccessToken(context);
            String body = oAuthLoginInstance.requestApi(context, at, url);

            String nickname = null;

            try {
                JSONObject root = new JSONObject(body);
                JSONObject jo = root.getJSONObject("response");
                nickname = jo.getString("nickname");

                Log.d("Meojium/NaverLogin", "Nickname: " + nickname);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return nickname;
        }
    }

    public boolean isLogin() {
        return oAuthLoginInstance.getState(context) == OAuthLoginState.OK;
    }

    public void initLoginButton(OAuthLoginButton button) {
        button.setOAuthLoginHandler(oAuthLoginHandler);
    }
}
