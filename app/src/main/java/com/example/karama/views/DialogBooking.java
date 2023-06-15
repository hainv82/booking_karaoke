package com.example.karama.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

public class DialogBooking extends Dialog {
    Activity activity;
    String roomId;
    public DialogBooking(@NonNull Activity context,String roomId) {
        super(context);
        this.activity = context;
        this.roomId = roomId;
    }
}
