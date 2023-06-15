package com.example.karama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.karama.adapter.ChoseCustomerAdapter;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.IInterfaceModel;
import com.example.karama.helper.UIHelper;
import com.example.karama.model.person.Customer;
import com.example.karama.model.person.ResAllCustomer;
import com.example.karama.model.room.DataReserveRoom;
import com.example.karama.model.room.Res9Reserve;
import com.example.karama.services.KaraServices;
import com.example.karama.services.RumServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Response;

public class Booking extends AppCompatActivity implements View.OnClickListener {
    TextView chose_time,chose_guest,booking,bar_customer,
            bookingId,startTime,roomId,guestPhoneNumber,guestFullName,staffUserName, statusCodeName;
    RelativeLayout layout_order;
    ImageView view_exit;
    String timebook="",phonebooking="",datebook,hourbook;
    String newString;
    int hour, minute;
    private DatePickerDialog datePickerDialog;
    Context mContext=this;
    ChoseCustomerAdapter customerAdapter;
    List<Customer> customerList;
    Dialog dialog ;
    private static Booking instance;
    ProgressDialog loadingBooking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        getSupportActionBar().hide();


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("idRoom");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("idRoom");
        }

        Log.e("==getEx:", newString);
        //
        initView();
        initDatePicker();
        initClick();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                // do something here
                onBackPressed();
                Log.e("==", "on Back ");
                BookingRoom.getInstance().f5();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initClick() {
        chose_time.setOnClickListener(this);
        chose_guest.setOnClickListener(this);
        booking.setOnClickListener(this);
        view_exit.setOnClickListener(this);


    }

    public static Booking getInstance() {
        if (instance == null) {
            instance = new Booking();
        }
        return instance;
    }
    private void initView() {
        instance = this;
        dialog= new Dialog(mContext);
        customerList = new ArrayList<>();
        chose_time = findViewById(R.id.chose_time);
        chose_guest = findViewById(R.id.chose_guest);
        booking = findViewById(R.id.booking);
        bookingId = findViewById(R.id.bookingId);
        startTime = findViewById(R.id.startTime);
        roomId = findViewById(R.id.roomId);
        guestPhoneNumber = findViewById(R.id.guestPhoneNumber);
        guestFullName = findViewById(R.id.guestFullName);
        staffUserName = findViewById(R.id.staffUserName);
        statusCodeName = findViewById(R.id.statusCodeName);
        layout_order = findViewById(R.id.layout_order);
        view_exit = findViewById(R.id.view_exit);
        bar_customer = findViewById(R.id.bar_customer);
        loadingBooking = new ProgressDialog(mContext);
        loadingBooking.setMessage("Booking...");
        bar_customer.setText("BOOKING Room ID "+newString);
        layout_order.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chose_time:
                openDatePicker();
                break;
            case R.id.chose_guest:
                dialogChoseGuest();
                break;
            case R.id.booking:
                checkValid();
                break;
            case R.id.view_exit:
                onBackPressed();
                BookingRoom.getInstance().f5();
                break;
        }

    }

    private void dialogChoseGuest() {

        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_chose_guest);
        // Set dialog title
        dialog.setTitle("Chose customer");
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        RecyclerView rcv_customer = dialog.findViewById(R.id.rcv_customer);
        rcv_customer.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        KaraServices.getAllCustomer(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                ResAllCustomer resAllCustomer = (ResAllCustomer) response.body();
                if (resAllCustomer != null) {
                    if (resAllCustomer.getStatus().equals("200")) {
                        customerList.clear();
                        for (int i = 0; i < resAllCustomer.getData().getData().size(); i++) {
                            Customer customer=resAllCustomer.getData().getData().get(i);
                            if (customer.getStatus().equals("1")) {
                                customerList.add(customer);
                            }

                        }
                        customerAdapter = new ChoseCustomerAdapter(mContext, customerList);
                        rcv_customer.setAdapter(customerAdapter);
                        customerAdapter.notifyDataSetChanged();

                    } else if (resAllCustomer.getStatus().equals("403")){
                        UIHelper.showAlertDialogV3(mContext, resAllCustomer.getStatus(), resAllCustomer.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
                                Intent i = new Intent(Booking.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        });
                    } else{
                        UIHelper.showAlertDialog(mContext,resAllCustomer.getStatus(),resAllCustomer.getMessage(),R.drawable.troll_64);
                    }
                }
            }

            @Override
            public void Error(String error) {

            }
        },"0","50","ASC");
        dialog.show();

    }

    private void checkValid() {
        if (timebook.equals("")) {
            Toast.makeText(mContext, "Vui lòng chọn thời gian", Toast.LENGTH_SHORT).show();
        } else if (phonebooking.equals("")) {
            Toast.makeText(mContext, "Vui lòng chọn khách hàng", Toast.LENGTH_SHORT).show();
        } else {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("startTime", timebook);
                jsonObject.put("roomId", newString);
                jsonObject.put("guestPhoneNumber", phonebooking);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("==callBooking:", jsonObject.toString());
            booking(jsonObject.toString());

        }
    }

    private void booking(String param) {
        loadingBooking.setCancelable(false);
        loadingBooking.show();
        RumServices.bookingRoom(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
               loadingBooking.cancel();
                Res9Reserve res9Reserve = (Res9Reserve) response.body();
                if (res9Reserve != null) {
                    if (res9Reserve.getStatus().equals("200")) {
                        Toast.makeText(mContext, res9Reserve.getStatus()+"-"+res9Reserve.getMessage(), Toast.LENGTH_SHORT).show();
                        booking.setClickable(false);
                        layout_order.setVisibility(View.VISIBLE);
                        DataReserveRoom dtaRoom= res9Reserve.getData();
                        bookingId.setText(dtaRoom.getBookingId());
                        startTime.setText(dtaRoom.getStartTime());
                        roomId.setText(dtaRoom.getRoomId());
                        guestPhoneNumber.setText(dtaRoom.getGuestPhoneNumber());
                        guestFullName.setText(dtaRoom.getGuestFullName());
                        staffUserName.setText(dtaRoom.getStaffUserName());
                        statusCodeName.setText(dtaRoom.getStatusCodeName());
                    } else if (res9Reserve.getStatus().equals("403")){
                        UIHelper.showAlertDialogV3(mContext, res9Reserve.getStatus(), res9Reserve.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
                                Intent i = new Intent(Booking.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        });
                    } else{
                        UIHelper.showAlertDialog(mContext,res9Reserve.getStatus(),res9Reserve.getMessage(),R.drawable.troll_64);
                    }
                }

            }

            @Override
            public void Error(String error) {
                loadingBooking.cancel();

            }
        },param);
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
                changeViewTime(timebook);

            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private void changeViewTime(String timebook) {
        chose_time.setText(timebook);
        chose_time.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.timeline_24,0,0,0);
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

    public void setPhoneBooking(Customer customer) {
        phonebooking = customer.getPhoneNumber();
        chose_guest.setText(customer.getFirstName()+" "+customer.getLastName()+"\n"+customer.getPhoneNumber());
        if (customer.getGender().equals("MALE")) {
            chose_guest.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.sing_boy35, 0, 0, 0);
        } else {
            chose_guest.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.sing_girl35, 0, 0, 0);
        }
        dialog.cancel();
    }
    public void f5(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}