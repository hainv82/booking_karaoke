package com.example.karama.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.karama.BookingRoom;
import com.example.karama.MainActivity;
import com.example.karama.R;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.UIHelper;
import com.example.karama.model.ResNullData;
import com.example.karama.model.room.DataRoom;
import com.example.karama.services.RumServices;

import retrofit2.Response;

public class DialogDetailRoom extends Dialog implements View.OnClickListener {
    Activity activity;
    DataRoom room;
    TextView update_room, check_in,cancel_ic,room_id,room_name,room_stt_booking,room_stt_type, tag_type_view,ic_lock,ic_unlock;
    ImageView view_close;
    public DialogDetailRoom(@NonNull Activity context,DataRoom room) {
        super(context);
        this.activity = context;
        this.room = room;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_detail_room);

        initView();
        update_room.setOnClickListener(this);
        check_in.setOnClickListener(this);
//        cancel_ic.setOnClickListener(this);
        view_close.setOnClickListener(this);
        ic_lock.setOnClickListener(this);
        ic_unlock.setOnClickListener(this);

    }

    private void initView() {
        update_room = findViewById(R.id.update_room);
        check_in = findViewById(R.id.check_in);
//        cancel_ic = findViewById(R.id.cancel_ic);

        room_id = findViewById(R.id.room_id);
        room_name = findViewById(R.id.room_name);
        room_stt_booking = findViewById(R.id.room_stt_booking);
        room_stt_type = findViewById(R.id.room_stt_type);
        tag_type_view = findViewById(R.id.tag_type_view);
        view_close = findViewById(R.id.view_close);
        ic_lock = findViewById(R.id.ic_lock);
        ic_unlock = findViewById(R.id.ic_unlock);

        room_id.setText(room.getId());
        room_name.setText(room.getName());
        room_stt_booking.setText(room.getRoomBookedStatus());
        if (room.getRoomBookedStatus().equals("EMPTY")) {
            room_stt_booking.setTextColor(Color.BLUE);
        }
        if (room.getRoomBookedStatus().equals("BOOKED")) {
            room_stt_booking.setTextColor(Color.RED);
        }

        room_stt_type.setText(room.getStatusCode());
        if (room.getType().equals("VIP")) {
            tag_type_view.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.vip_30, 0, 0);
            tag_type_view.setText("VIP");
        } else {
            tag_type_view.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.poor_30, 0, 0);
            tag_type_view.setText("Normal");
        }
        if (room.getStatusCode().equals("ENABLE")) {
            ic_lock.setVisibility(View.VISIBLE);
            ic_unlock.setVisibility(View.GONE);
        } else {
            ic_lock.setVisibility(View.GONE);
            ic_unlock.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_room:
                //call dialog update
                dismiss();
                BookingRoom.getInstance().updateRoom(room);
                break;
            case R.id.check_in:
                // load list orderID theo room ID, xong chojn -> check in
                BookingRoom.getInstance().getDialogRecept(room.getId());
                break;
//            case R.id.cancel_ic:
                // order ID by room ID
//                cancelbyRoomID();
//                break;
            case R.id.view_close:
                dismiss();
                break;
            case R.id.ic_lock:
                //call aoi lock
                lockRoom();
                break;
            case R.id.ic_unlock:
                //call api unclock
                unlockRoom();
                break;
        }
    }

    private void unlockRoom() {
        RumServices.unlockRoom(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                ResNullData resUnlock = (ResNullData) response.body();
                if (resUnlock != null) {
                    if (resUnlock.getStatus().equals("200")) {
                        Log.e("==", "unlock " + room.getId() + " success");
                        dismiss();
                        BookingRoom.getInstance().f5();
                    } else if (resUnlock.getStatus().equals("403")){
                        Log.e("==403", resUnlock.getMessage());
                        Toast.makeText(activity, resUnlock.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(activity, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(i);
                    } else{
                        UIHelper.showAlertDialog(activity,resUnlock.getStatus(),resUnlock.getMessage(),R.drawable.troll_64);
                        Toast.makeText(activity, resUnlock.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("==err", resUnlock.getStatus() + "-" + resUnlock.getMessage());
                    }
                }
            }

            @Override
            public void Error(String error) {

            }
        },room.getId());
    }

    private void lockRoom() {
        RumServices.lockRoom(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                ResNullData resLock = (ResNullData) response.body();
                if (resLock != null) {
                    if (resLock.getStatus().equals("200")) {
                        Log.e("==", "lock " + room.getId() + " success");
                        dismiss();
                        BookingRoom.getInstance().f5();
                    } else if (resLock.getStatus().equals("403")){
                        Log.e("==403", resLock.getMessage());
                        Toast.makeText(activity, resLock.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(activity, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(i);
                    } else{
                        UIHelper.showAlertDialog(activity,resLock.getStatus(),resLock.getMessage(),R.drawable.troll_64);
                        Toast.makeText(activity, resLock.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("==err", resLock.getStatus() + "-" + resLock.getMessage());
                    }
                }
            }

            @Override
            public void Error(String error) {
                Log.e("==err", error);
                Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();

            }
        },room.getId());
    }
}
