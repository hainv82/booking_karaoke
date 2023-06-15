package com.example.karama.model.room;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataRoom {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("roomBookedStatus")
    @Expose
    private String roomBookedStatus;
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("type")
    @Expose
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomBookedStatus() {
        return roomBookedStatus;
    }

    public void setRoomBookedStatus(String roomBookedStatus) {
        this.roomBookedStatus = roomBookedStatus;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DataRoom{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", roomBookedStatus='" + roomBookedStatus + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
