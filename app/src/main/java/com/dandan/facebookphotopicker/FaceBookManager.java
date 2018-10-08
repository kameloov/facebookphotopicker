package com.dandan.facebookphotopicker;

import android.app.Activity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

/**
 * Created by kameloov on 10/4/2018.
 */

 class FaceBookManager {
    public static void registerLoginCallback(CallbackManager callbackManager,
                                             FacebookCallback<LoginResult> callback){
        LoginManager.getInstance().registerCallback(callbackManager,callback);
    }

    public static boolean isLogged(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return  accessToken!=null && !accessToken.isExpired();

    }
    public static void login(Activity activity){
        LoginManager.getInstance().logInWithReadPermissions(
                activity, Arrays.asList("user_photos","public_profile"));
    }
}
