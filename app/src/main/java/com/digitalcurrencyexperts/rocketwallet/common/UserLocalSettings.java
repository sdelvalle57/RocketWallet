package com.digitalcurrencyexperts.rocketwallet.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class UserLocalSettings {


    private SharedPreferences appSharedPrefs;

    public UserLocalSettings(Context ctx) {
        appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public void setFiatName(String fiat){
        Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(Constants.FIAT, fiat);
        prefsEditor.apply();
    }

    public String getFiatName(){
       return appSharedPrefs.getString(Constants.FIAT, Constants.AUD);
    }

//
//
//    public void saveUser(User user) {
//        Editor prefsEditor = appSharedPrefs.edit();
//        //prefsEditor.putBoolean(UserLocalSettings.MODE_G_H, user.isGuestMode());
//        prefsEditor.putBoolean(UserLocalSettings.APPROVED, user.isApproved());
//        Gson gson = new Gson();
//        String userJson = gson.toJson(user);
//        prefsEditor.putString(USER, userJson);
//        prefsEditor.apply();
//    }
//
//
//    public User getUser() {
//        Gson gson = new Gson();
//        String userJson = appSharedPrefs.getString(USER, "");
//        return gson.fromJson(userJson, User.class);
//    }
//
//    public void clearUser() {
//        Editor prefsEditor = appSharedPrefs.edit();
//        prefsEditor.remove(USER);
//        prefsEditor.apply();
//    }
//
//
//
//    public void saveSharedPrefString(String para, String val) {
//        Editor prefsEditor = appSharedPrefs.edit();
//        prefsEditor.putString(para, val);
//        prefsEditor.apply();
//    }
//
//    public String getSharedPrefString(String para) {
//        return appSharedPrefs.getString(para, "");
//    }
//
//    public void saveSharedPrefInt(String para, int val) {
//        Editor prefsEditor = appSharedPrefs.edit();
//        prefsEditor.putInt(para, val);
//        prefsEditor.apply();
//    }
//
//    public int getSharedPrefInt(String para) {
//        return appSharedPrefs.getInt(para, -1);
//    }
//
//
}