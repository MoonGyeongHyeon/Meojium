package com.moon.meojium.ui.login.naver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.moon.meojium.base.NaverAPI;
import com.moon.meojium.base.util.SharedPreferencesService;
import com.moon.meojium.ui.home.HomeActivity;
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

public class NaverLogin implements NaverAPI {
    public static final String NAVER_TOKEN_TYPE = "Naver";
    private static final String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    private static NaverLogin naverLogin;

    private OAuthLogin oAuthLoginInstance;
    private Context context;

    public static NaverLogin getInstance() {
        if (naverLogin == null) {
            synchronized (NaverLogin.class) {
                if (naverLogin == null) {
                    naverLogin = new NaverLogin();
                }
            }
        }
        return naverLogin;
    }

    private NaverLogin() {
    }

    public void init(Context context) {
        this.context = context;

        oAuthLoginInstance = OAuthLogin.getInstance();
        oAuthLoginInstance.init(context, CLIENT_ID, CLIENT_SECRET, OAUTH_CLIENT_NAME);
    }

    private OAuthLoginHandler oAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                Log.d("Meojium/NaverLogin", "Naver login is successful");

                SharedPreferencesService service = SharedPreferencesService.getInstance();
                service.putData(SharedPreferencesService.TOKEN_KEY, oAuthLoginInstance.getAccessToken(context));
                service.putData(SharedPreferencesService.TOKEN_TYPE_KEY, NAVER_TOKEN_TYPE);
                service.putData(SharedPreferencesService.NICKNAME_KEY, requestUserNickname());

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
