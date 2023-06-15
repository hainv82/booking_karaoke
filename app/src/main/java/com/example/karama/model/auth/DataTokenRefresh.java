package com.example.karama.model.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataTokenRefresh {
    @SerializedName("access_token")
    @Expose
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
