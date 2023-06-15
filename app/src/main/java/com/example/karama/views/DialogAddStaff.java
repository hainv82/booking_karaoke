package com.example.karama.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.karama.R;
import com.example.karama.helper.UHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class DialogAddStaff extends Dialog {
    Activity activity;
    EditText add_username,add_pw,add_cf_pw,add_fname,add_lname,add_email,add_sdt,add_gender,add_adres1,add_adres2, add_role;
    TextView tv_add;
    ImageView view_colse;

    public DialogAddStaff(@NonNull Activity context) {
        super(context);
        this.activity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_staff);
        initView();
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNull();
            }
        });
        view_colse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        add_cf_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(add_pw.getText().toString())) {
                    add_cf_pw.setTextColor(Color.parseColor("#50EA14"));
                } else {
                    add_cf_pw.setTextColor(Color.parseColor("#E4122A"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        
    }

    private void checkNull() {
        String username = UHelper.onTextStr(add_username);
        String pw = UHelper.onTextStr(add_pw);
        String cf_pw = UHelper.onTextStr(add_cf_pw);
        String fname = UHelper.onTextStr(add_fname);
        String lname = UHelper.onTextStr(add_lname);
        String email = UHelper.onTextStr(add_email);
        String  sdt = UHelper.onTextStr(add_sdt);
        String gender = UHelper.onTextStr(add_gender);
        String adres1 = UHelper.onTextStr(add_adres1);
        String adres2 = UHelper.onTextStr(add_adres2);
        String role = UHelper.onTextStr(add_role);
        if (UHelper.onValidate(username)) {
            Toast.makeText(activity, "Vui lòng nhập Username", Toast.LENGTH_SHORT).show();
            add_username.requestFocus();
        } else if (UHelper.onValidate(pw)) {
            Toast.makeText(activity, "Vui lòng nhập Password", Toast.LENGTH_SHORT).show();
            add_pw.requestFocus();
        }else if (UHelper.onValidate(cf_pw)) {
            Toast.makeText(activity, "Vui lòng nhập Confirm Password", Toast.LENGTH_SHORT).show();
            add_cf_pw.requestFocus();
        }else if (UHelper.onValidate(fname)) {
            Toast.makeText(activity, "Vui lòng nhập First Name", Toast.LENGTH_SHORT).show();
            add_fname.requestFocus();
        }else if (UHelper.onValidate(lname)) {
            Toast.makeText(activity, "Vui lòng nhập Last Name", Toast.LENGTH_SHORT).show();
            add_lname.requestFocus();
        }else if (UHelper.onValidate(email)) {
            Toast.makeText(activity, "Vui lòng nhập Email", Toast.LENGTH_SHORT).show();
            add_email.requestFocus();
        }else if (UHelper.onValidate(sdt)) {
            Toast.makeText(activity, "Vui lòng nhập SĐT ", Toast.LENGTH_SHORT).show();
            add_sdt.requestFocus();
        }else if (UHelper.onValidate(gender)) {
            Toast.makeText(activity, "Vui lòng nhập giới tính", Toast.LENGTH_SHORT).show();
            add_gender.requestFocus();
        }else if (UHelper.onValidate(adres1)) {
            Toast.makeText(activity, "Vui lòng nhập địa chỉ thường trú", Toast.LENGTH_SHORT).show();
            add_adres1.requestFocus();
        }else if (UHelper.onValidate(adres2)) {
            Toast.makeText(activity, "Vui lòng nhập địa chỉ tạm trú", Toast.LENGTH_SHORT).show();
            add_adres2.requestFocus();
        }else if (UHelper.onValidate(role)) {
            Toast.makeText(activity, "Vui lòng nhập chức danh", Toast.LENGTH_SHORT).show();
            add_role.requestFocus();
        } else if (!pw.equals(cf_pw)) {
            Toast.makeText(activity, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
            add_cf_pw.requestFocus();
        } else {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", username);
                jsonObject.put("password", pw);
                jsonObject.put("firstName", fname);
                jsonObject.put("lastName", lname);
                jsonObject.put("gender", gender);
                jsonObject.put("address1", adres1);
                jsonObject.put("address2", adres2);
                jsonObject.put("phoneNumber", sdt);
                jsonObject.put("roleCodeName", role);
                jsonObject.put("email", email);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (MainMenu.getInstance() != null) {
                MainMenu.getInstance().addUser(jsonObject.toString());
            }

        }


    }

    private void initView() {
        add_username = findViewById(R.id.add_username);
        add_pw = findViewById(R.id.add_pw);
        add_cf_pw = findViewById(R.id.add_cf_pw);
        add_fname = findViewById(R.id.add_fname);
        add_lname = findViewById(R.id.add_lname);
        add_email = findViewById(R.id.add_email);
        add_sdt = findViewById(R.id.add_sdt);
        add_gender = findViewById(R.id.add_gender);
        add_adres1 = findViewById(R.id.add_adres1);
        add_adres2 = findViewById(R.id.add_adres2);
        add_role = findViewById(R.id.add_role);
        tv_add = findViewById(R.id.tv_add);
        view_colse = findViewById(R.id.view_colse);
    }
}
