package com.example.karama.model.person;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResAllCustomer {
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private DataListCustomer data;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataListCustomer getData() {
        return data;
    }

    public void setData(DataListCustomer data) {
        this.data = data;
    }
}
