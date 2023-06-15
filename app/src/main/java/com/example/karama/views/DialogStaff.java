package com.example.karama.views;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.karama.R;
import com.example.karama.model.person.Staff;

public class DialogStaff extends Dialog {
    public Activity activity;
    Staff staff;
    TextView txt_username,txt_first_name,txt_last_name,txt_gender,txt_email,txt_sdt,txt_adress1, txt_adress2;
    Button btn_changepass;
    ImageView img_close;
    public DialogStaff(@NonNull Activity context,Staff staff) {
        super(context);
        this.activity = context;
        this.staff = staff;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_staff);
        initView();
        initClick();

    }

    private void initClick() {
        btn_changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog change pass
                if (MainMenu.getInstance() != null) {
                    MainMenu.getInstance().dialogNewpass(staff.getUsername());
                }
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initView() {
        txt_username = findViewById(R.id.txt_username);
        txt_sdt = findViewById(R.id.txt_sdt);
        txt_gender = findViewById(R.id.txt_gender);
        txt_adress2 = findViewById(R.id.txt_adress2);
        txt_last_name = findViewById(R.id.txt_last_name);
        txt_adress1 = findViewById(R.id.txt_adress1);
        txt_first_name = findViewById(R.id.txt_first_name);
        txt_email = findViewById(R.id.txt_email);
        btn_changepass = findViewById(R.id.btn_changepass);
        img_close = findViewById(R.id.img_close);

        txt_username.setText(staff.getUsername());
        txt_sdt.setText(staff.getPhoneNumber());
        txt_gender.setText(staff.getGender());
        txt_adress1.setText(staff.getAddress1());
        txt_adress2.setText(staff.getAddress2());
        txt_first_name.setText(staff.getFirstName());
        txt_last_name.setText(staff.getLastName());
        txt_email.setText(staff.getEmail());


    }
}
