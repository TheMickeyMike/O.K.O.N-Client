package com.roundstarstudio.maciej.okon.activities.ui.activities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Maciej on 02.11.15.
 */
public class UserLocalStore {
    public static final String SP_NAME = "userAuthentication";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME,0);
    }

    public void storeAccessToken(String accessToken, String tokenType) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("accessToken", accessToken);
        spEditor.putString("tokenType", tokenType);
        spEditor.commit();
    }

    public String getAccessToken() {
        String accessToken = userLocalDatabase.getString("accessToken", "");
        return  accessToken;
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn() {
        if (userLocalDatabase.getBoolean("loggedIn", false)) {
            return true;
        } else {
            return false;
        }
    }
    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

}
