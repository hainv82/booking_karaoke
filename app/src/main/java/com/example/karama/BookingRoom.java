package com.example.karama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.karama.R;
import com.example.karama.adapter.GridRoomAdapter;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.IInterfaceModel;
import com.example.karama.helper.UIHelper;
import com.example.karama.model.room.DataRoom;
import com.example.karama.model.room.ResAddRoom;
import com.example.karama.model.room.ResListRoom;
import com.example.karama.services.RumServices;
import com.example.karama.views.DialogAddRoom;
import com.example.karama.views.DialogDetailRoom;
import com.example.karama.views.DialogReceptByPhone;
import com.example.karama.views.DialogReceptByRoom;
import com.example.karama.views.MainMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Response;

public class BookingRoom extends AppCompatActivity implements View.OnClickListener {
    Context mContext=this;
    RecyclerView grid_all_room;
    List<DataRoom> roomList;
    GridRoomAdapter roomAdapter;
    ProgressDialog loadRoom,loadingAddRoom,loadingEmptyRoom,loadingUsingRoom;
    TextView add_room;
    RadioButton all_room,empty_room, using_room;
    ImageView view_exit;
    int hour, minute;
    String timebook,datebook,hourbook;
    private DatePickerDialog datePickerDialog;
    private static BookingRoom instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_room);
        getSupportActionBar().hide();

        initView();
        initClick();
        loadAllRoom();
        initDatePicker();
//        Log.e("date:", getTodaysDate());
    }

    public void loadAllRoom() {
        loadRoom.show();
        RumServices.getAllRoom(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                loadRoom.cancel();
                ResListRoom resListRoom = (ResListRoom) response.body();
                if (resListRoom != null) {
                    if (resListRoom.getStatus().equals("200")) {
                        roomList.clear();
                        for (int i = 0; i < resListRoom.getData().getData().size(); i++) {
                            DataRoom room = resListRoom.getData().getData().get(i);
                            roomList.add(room);
                        }
                        roomAdapter = new GridRoomAdapter(mContext, roomList);
                        grid_all_room.setAdapter(roomAdapter);
                        roomAdapter.notifyDataSetChanged();
                    } else if (resListRoom.getStatus().equals("403")){
                        UIHelper.showAlertDialogV3(mContext, resListRoom.getStatus(), resListRoom.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
                                Intent i = new Intent(BookingRoom.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        });
                    } else{
                        UIHelper.showAlertDialog(mContext,resListRoom.getStatus(),resListRoom.getMessage(),R.drawable.troll_64);
                    }
                }
            }

            @Override
            public void Error(String error) {
                loadRoom.cancel();
                UIHelper.showAlertDialog(mContext,"ERROR",error,R.drawable.bad_face_35);

            }
        });
    }

    private void initClick() {
        add_room.setOnClickListener(this);
        all_room.setOnClickListener(this);
        empty_room.setOnClickListener(this);
        using_room.setOnClickListener(this);
        view_exit.setOnClickListener(this);
    }
    public static BookingRoom getInstance(){
        if (instance == null) {
            instance = new BookingRoom();
        }
        return instance;
    }

    public void addRoom(String name, String type) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("==pramAddRoom:", jsonObject.toString());
        loadingAddRoom.show();
        RumServices.addRoom(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                loadingAddRoom.cancel();
                ResAddRoom resAddRoom = (ResAddRoom) response.body();
                if (resAddRoom != null) {
                    if (resAddRoom.getStatus().equals("200")) {
                        Toast.makeText(mContext, resAddRoom.getStatus()+"-"+resAddRoom.getMessage(), Toast.LENGTH_SHORT).show();
                        //reload view rooom
                        loadAllRoom();
                    } else if (resAddRoom.getStatus().equals("403")){
                        UIHelper.showAlertDialogV3(mContext, resAddRoom.getStatus(), resAddRoom.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
                                Intent i = new Intent(BookingRoom.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        });
                    } else{
                        UIHelper.showAlertDialog(mContext,resAddRoom.getStatus(),resAddRoom.getMessage(),R.drawable.troll_64);
                    }
                }

            }

            @Override
            public void Error(String error) {
                loadingAddRoom.cancel();
                UIHelper.showAlertDialog(mContext,"ERROR",error,R.drawable.bad_face_35);

            }
        }, jsonObject.toString());
    }

    private void initView() {
        instance = this;
        grid_all_room = findViewById(R.id.grid_all_room);
        grid_all_room.setLayoutManager(new GridLayoutManager(mContext,2));
        roomList = new ArrayList<>();
        loadRoom = new ProgressDialog(mContext);
        loadRoom.setMessage("Đang load danh sách phòng ...");
        loadingAddRoom = new ProgressDialog(mContext);
        loadingAddRoom.setMessage("Đang thêm phòng mới ...");
        loadingEmptyRoom= new ProgressDialog(mContext);
        loadingEmptyRoom.setMessage("Đang load danh sách phòng trống ...");
        loadingUsingRoom = new ProgressDialog(mContext);
        loadingUsingRoom.setMessage("Đang load Using room ...");



        add_room = findViewById(R.id.add_room);
        all_room = findViewById(R.id.all_room);
        empty_room = findViewById(R.id.empty_room);
        all_room.setChecked(true);
        using_room = findViewById(R.id.using_room);
        view_exit = findViewById(R.id.view_exit);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_room:
                DialogAddRoom dialogAddRoom = new DialogAddRoom(BookingRoom.this, "ADD");
                dialogAddRoom.setCanceledOnTouchOutside(false);
                dialogAddRoom.show();
                break;
            case R.id.all_room:
                loadAllRoom();
                break;
            case R.id.empty_room:
                loadEmptyRoom();
                break;
            case R.id.using_room:
                loadUsingRoom();
                break;
            case R.id.view_exit:
                onBackPressed();
                break;
        }

    }

    public void loadUsingRoom() {
        loadingUsingRoom.show();
        RumServices.getListRoomUsed(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                loadingUsingRoom.cancel();
                ResListRoom resListRoom = (ResListRoom) response.body();
                if (resListRoom != null) {
                    if (resListRoom.getStatus().equals("200")) {
                        roomList.clear();
                        for (int i = 0; i < resListRoom.getData().getData().size(); i++) {
                            DataRoom room = resListRoom.getData().getData().get(i);
                            roomList.add(room);
                        }
                        roomAdapter = new GridRoomAdapter(mContext, roomList);
                        grid_all_room.setAdapter(roomAdapter);
                        roomAdapter.notifyDataSetChanged();
                    } else if (resListRoom.getStatus().equals("403")){
                        UIHelper.showAlertDialogV3(mContext, resListRoom.getStatus(), resListRoom.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
                                Intent i = new Intent(BookingRoom.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        });
                    } else{
                        UIHelper.showAlertDialog(mContext,resListRoom.getStatus(),resListRoom.getMessage(),R.drawable.troll_64);
                    }
                }
            }

            @Override
            public void Error(String error) {
                loadingUsingRoom.cancel();
                UIHelper.showAlertDialog(mContext,"ERROR",error,R.drawable.bad_face_35);

            }
        });
    }

    public void loadEmptyRoom() {
        loadingEmptyRoom.show();
        RumServices.getListRoomEmpty(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                loadingEmptyRoom.cancel();
                ResListRoom resListRoom = (ResListRoom) response.body();
                if (resListRoom != null) {
                    if (resListRoom.getStatus().equals("200")) {
                        roomList.clear();
                        for (int i = 0; i < resListRoom.getData().getData().size(); i++) {
                            DataRoom room = resListRoom.getData().getData().get(i);
                            roomList.add(room);
                        }
                        roomAdapter = new GridRoomAdapter(mContext, roomList);
                        grid_all_room.setAdapter(roomAdapter);
                        roomAdapter.notifyDataSetChanged();
                    } else if (resListRoom.getStatus().equals("403")){
                        UIHelper.showAlertDialogV3(mContext, resListRoom.getStatus(), resListRoom.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
                                Intent i = new Intent(BookingRoom.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        });
                    } else{
                        UIHelper.showAlertDialog(mContext,resListRoom.getStatus(),resListRoom.getMessage(),R.drawable.troll_64);
                    }
                }
            }

            @Override
            public void Error(String error) {
                loadingEmptyRoom.cancel();
                UIHelper.showAlertDialog(mContext,"ERROR",error,R.drawable.bad_face_35);

            }
        });
    }

    public void callDialogDetailRoom(DataRoom dataRoom) {
        DialogDetailRoom dialogDetailRoom = new DialogDetailRoom(BookingRoom.this,dataRoom);
        dialogDetailRoom.setCanceledOnTouchOutside(false);
        dialogDetailRoom.show();
    }
    public void popTimePicker()
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
//                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
                Log.e("==pickTime:", String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                Log.e("==myTime", hour + ":" + minute + ":00");
                timebook = datebook + hour + ":" + minute + ":00";
                Log.e("==endTime:", timebook);

            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                Log.e("==initDatePicker", year+"-"+month+":"+day);
                datebook = year + "-" + month + "-" + day + "T";
                popTimePicker();
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    public void openDatePicker()
    {
        datePickerDialog.show();
    }
    public void f5(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void updateRoom(DataRoom room) {
        DialogAddRoom dialogUpdateRoom = new DialogAddRoom(BookingRoom.this, "UPDATE", room);
        dialogUpdateRoom.setCanceledOnTouchOutside(false);
        dialogUpdateRoom.show();

    }
    public void getDialogRecept(String roomId) {
        DialogReceptByRoom dialogReceptByRoom = new DialogReceptByRoom(BookingRoom.this, roomId);
        dialogReceptByRoom.setCancelable(false);
        dialogReceptByRoom.show();

    }

}