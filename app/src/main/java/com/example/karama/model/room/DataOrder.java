package com.example.karama.model.room;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataOrder {
    @SerializedName("bookingId")
    @Expose
    private String bookingId;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("roomId")
    @Expose
    private String roomId;
    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("guestPhoneNumber")
    @Expose
    private String guestPhoneNumber;
    @SerializedName("guestFullName")
    @Expose
    private String guestFullName;
    @SerializedName("staffUserName")
    @Expose
    private String staffUserName;
    @SerializedName("statusCodeName")
    @Expose
    private String statusCodeName;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGuestPhoneNumber() {
        return guestPhoneNumber;
    }

    public void setGuestPhoneNumber(String guestPhoneNumber) {
        this.guestPhoneNumber = guestPhoneNumber;
    }

    public String getGuestFullName() {
        return guestFullName;
    }

    public void setGuestFullName(String guestFullName) {
        this.guestFullName = guestFullName;
    }

    public String getStaffUserName() {
        return staffUserName;
    }

    public void setStaffUserName(String staffUserName) {
        this.staffUserName = staffUserName;
    }

    public String getStatusCodeName() {
        return statusCodeName;
    }

    public void setStatusCodeName(String statusCodeName) {
        this.statusCodeName = statusCodeName;
    }

    @Override
    public String toString() {
        return "DataOrder{" +
                "bookingId='" + bookingId + '\'' +
                ", startTime='" + startTime + '\'' +
                ", roomId='" + roomId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", guestPhoneNumber='" + guestPhoneNumber + '\'' +
                ", guestFullName='" + guestFullName + '\'' +
                ", staffUserName='" + staffUserName + '\'' +
                ", statusCodeName='" + statusCodeName + '\'' +
                '}';
    }
}
