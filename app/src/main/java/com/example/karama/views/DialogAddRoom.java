package com.example.karama.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.karama.BookingRoom;
import com.example.karama.MainActivity;
import com.example.karama.R;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.UHelper;
import com.example.karama.helper.UIHelper;
import com.example.karama.model.ResNullData;
import com.example.karama.model.room.DataRoom;
import com.example.karama.services.RumServices;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;

public class DialogAddRoom extends Dialog implements View.OnClickListener {
    ImageView view_close;
    EditText room_name;
    RadioButton rdbtn_vip,rdbtn_normal;
    TextView add_room,update_room;
    Activity activity;
    String typeuse;
    DataRoom roomGet;
    public DialogAddRoom(@NonNull Activity context,String typeuse) {
        super(context);
        this.activity = context;
        this.typeuse = typeuse;
    }
    public DialogAddRoom(@NonNull Activity context,String typeuse,DataRoom room) {
        super(context);
        this.activity = context;
        this.typeuse = typeuse;
        this.roomGet = room;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_room);
        initView();
        view_close.setOnClickListener(this);
        add_room.setOnClickListener(this);
    }

    private void initView() {
        view_close = findViewById(R.id.view_close);
        room_name = findViewById(R.id.room_name);
        rdbtn_normal = findViewById(R.id.rdbtn_normal);
        rdbtn_vip = findViewById(R.id.rdbtn_vip);
        add_room = findViewById(R.id.add_room);
        update_room = findViewById(R.id.update_room);
        if (typeuse.equals("UPDATE")) {
            update_room.setVisibility(View.VISIBLE);
            room_name.setText(roomGet.getName());
            if (roomGet.getType().equals("VIP")) {
                rdbtn_vip.setChecked(true);
            } else {
                rdbtn_normal.setChecked(true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_room:
                //call api add room
                checkValid();
                //reload room
                break;
            case R.id.view_close:
                dismiss();
                break;
            case R.id.update_room:
                updateRoom();
                break;
        }
    }

    private void updateRoom() {
        JSONObject jsonObject = new JSONObject();
        String typeUp = "";
        if (rdbtn_vip.isChecked()) {
            typeUp = "VIP";
        }
        if (rdbtn_normal.isChecked()) {
            typeUp = "NORMAL";
        }
        if (room_name.getText().toString().equals("")) {
            Toast.makeText(activity, "Vui lòng nhập tên phòng update ", Toast.LENGTH_SHORT).show();
        } else {
            try {
                jsonObject.put("name", room_name.getText().toString());
                jsonObject.put("type", typeUp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("==pramUdateRum:", jsonObject.toString());
        RumServices.updateRoom(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                ResNullData resUpdate = (ResNullData) response.body();
                if (resUpdate != null) {
                    if (resUpdate.getStatus().equals("200")) {
                        Log.e("==", "udpate room success");
                        dismiss();
                        dismiss();
                        BookingRoom.getInstance().loadAllRoom();

                    } else if (resUpdate.getStatus().equals("403")){
                        Log.e("==403", resUpdate.getMessage());
                        Toast.makeText(activity, resUpdate.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(activity, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(i);
                    } else{
                        UIHelper.showAlertDialog(activity,resUpdate.getStatus(),resUpdate.getMessage(),R.drawable.troll_64);
                        Toast.makeText(activity, resUpdate.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("==err", resUpdate.getStatus() + "-" + resUpdate.getMessage());
                    }
                }
            }

            @Override
            public void Error(String error) {
                Log.e("==err", error);
                Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();

            }
        },roomGet.getId(), jsonObject.toString());
    }

    private void checkValid() {
        String name = UHelper.onTextStr(room_name);
        if (UHelper.onValidate(name)) {
            Toast.makeText(activity, "Vui lòng nhập tên phòng ", Toast.LENGTH_SHORT).show();
            room_name.requestFocus();
        } else if (!rdbtn_vip.isChecked() && !rdbtn_normal.isChecked()) {
            Toast.makeText(activity, "Vui lòng chọn loại phòng", Toast.LENGTH_SHORT).show();
        } else {
            if (BookingRoom.getInstance() != null) {
                if (rdbtn_vip.isChecked()) {
                    BookingRoom.getInstance().addRoom(name,"VIP");
                    dismiss();
                }
                if (rdbtn_normal.isChecked()) {
                    BookingRoom.getInstance().addRoom(name,"NORMAL");
                    dismiss();
                }
            }
        }

    }
}
