package com.example.karama.model.room;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Booked{
    @SerializedName("isBooked")
    @Expose
    private Boolean isBooked;

    public Boolean getBooked() {
        return isBooked;
    }

    public void setBooked(Boolean booked) {
        isBooked = booked;
    }
}
