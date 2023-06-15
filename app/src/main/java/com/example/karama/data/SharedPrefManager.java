package com.example.karama.data;

import android.content.Context;

public class SharedPrefManager {

    private static SharedPrefManager instance;
    private MySharePreferences mySharePreferences;

    public static void init(Context context) {
        instance = new SharedPrefManager();
        instance.mySharePreferences = new MySharePreferences(context);
    }

    public static SharedPrefManager getInstance() {
        if (instance == null) {
            instance = new SharedPrefManager();
        }
        return instance;
    }
//  set share preferencea
    public static void setPrefFirstInstall(boolean isFirst) {
        SharedPrefManager.getInstance().mySharePreferences.putBooleanValue(KeyPem.PREF_FIRST_INSTALL, isFirst);
    }

    public static void setAccessToken(String accessToken) {
        SharedPrefManager.getInstance().mySharePreferences.putStringValue(KeyPem.SHARED_ACCESS_TOKEN,accessToken);
    }

    public static void setRefreshToken(String refreshToken) {
        SharedPrefManager.getInstance().mySharePreferences.putStringValue(KeyPem.SHARED_REFRESH_TOKEN,refreshToken);
    }
    public static void setUsername(String username) {
        SharedPrefManager.getInstance().mySharePreferences.putStringValue(KeyPem.SHARED_USERNAME,username);
    }

//  get share preferencea
    public static boolean getFirstInstall(){
        return SharedPrefManager.getInstance().mySharePreferences.getBooleanValue(KeyPem.PREF_FIRST_INSTALL);
    }

    public static String getAccessToken() {
        return SharedPrefManager.getInstance().mySharePreferences.getStringValue(KeyPem.SHARED_ACCESS_TOKEN);
    }
    public static String getRefreshToken() {
        return SharedPrefManager.getInstance().mySharePreferences.getStringValue(KeyPem.SHARED_REFRESH_TOKEN);
    }
    public static String getUsername() {
        return SharedPrefManager.getInstance().mySharePreferences.getStringValue(KeyPem.SHARED_USERNAME);
    }




}
