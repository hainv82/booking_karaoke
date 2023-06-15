package com.example.karama.model.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResTokenRefresh {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private DataTokenRefresh data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataTokenRefresh getData() {
        return data;
    }

    public void setData(DataTokenRefresh data) {
        this.data = data;
    }
}
