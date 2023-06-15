package com.example.karama.model.room;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResAddRoom {
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
    private ID data;

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

    public ID getData() {
        return data;
    }

    public void setData(ID data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResAddRoom{" +
                "timestamp='" + timestamp + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
