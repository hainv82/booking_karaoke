package com.example.karama.data;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharePreferences {
    private static final String MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES";
    private Context mContext;

    public MySharePreferences(Context mContext) {
        this.mContext = mContext;
    }

    public void putBooleanValue(String key, Boolean value) {
        SharedPreferences sharedPreferences= mContext.getSharedPreferences(MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void putStringValue(String key, String stringValue) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, stringValue);
        editor.apply();
    }

    public boolean getBooleanValue(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }

    public String getStringValue(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "NON");
    }

}
