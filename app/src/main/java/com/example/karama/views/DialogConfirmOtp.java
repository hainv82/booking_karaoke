package com.example.karama.views;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karama.R;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.UIHelper;
import com.example.karama.model.person.ResProfile;
import com.example.karama.services.APIServices;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;

public class DialogConfirmOtp extends Dialog implements View.OnClickListener {
    public Activity activity;
    ImageView tv_close_confirm;
    Button btn_resend_otp, btn_confirm;
    EditText edt_otp;
    TextView tv_mess;
    String username;
    ProgressDialog loadingCongfirm,dialogResend;
    public DialogConfirmOtp(Activity context,String username) {
        super(context);
        this.activity = context;
        this.username = username;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_confirm_otp);
        tv_close_confirm = findViewById(R.id.tv_close_confirm);
        btn_resend_otp = findViewById(R.id.btn_resend_otp);
        btn_confirm = findViewById(R.id.btn_confirm);
        edt_otp = findViewById(R.id.edt_otp);
        tv_mess = findViewById(R.id.tv_mess);
        loadingCongfirm = new ProgressDialog(activity);
        dialogResend = new ProgressDialog(activity);
        loadingCongfirm.setMessage("Đang xác thực OTP _");
        dialogResend.setMessage("Đang gửi lại OTP");
        initClick();
    }

    private void initClick() {
        tv_close_confirm.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        btn_resend_otp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close_confirm:
                dismiss();
                break;
            case R.id.btn_resend_otp:
                Log.e("==click", "resend_otp");
                //call api resend 
                resendOTP();
                
                break;
            case R.id.btn_confirm:
                Log.e("==click:", "confirm_otp");
                Log.e("==otp:", edt_otp.getText().toString());
                if (edt_otp.getText().toString().equals("")) {
                    Toast.makeText(activity, "Bạn chưa nhập OTP", Toast.LENGTH_SHORT).show();
                } else {
                    confirm(edt_otp.getText().toString());
                }
                break;

        }
    }

    private void resendOTP() {
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("username", username);
        dialogResend.show();
        APIServices.resend(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                dialogResend.cancel();
                ResProfile resProfile = (ResProfile) response.body();
                if (resProfile != null) {
                    if (resProfile.getStatus().equals("200")) {
                        UIHelper.showAlertDialog(activity, "SUCCESS", resProfile.getMessage(), R.drawable.ic_success_35);
                    } else {
                        UIHelper.showAlertDialog(activity,"FAILURE",resProfile.getMessage(),R.drawable.bad_face_35);
                    }
                }
            }

            @Override
            public void Error(String error) {
                dialogResend.cancel();
                UIHelper.showAlertDialog(activity,"ERROR",error,R.drawable.ic_err_35);

            }
        },username);
    }

    private void confirm(String otp) {
        loadingCongfirm.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("otp", otp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("==paramConfirm:", jsonObject.toString());
        APIServices.confirm(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                loadingCongfirm.cancel();
                ResProfile resConfirm = (ResProfile) response.body();
                if (resConfirm != null) {
                    if (resConfirm.getStatus().equals("200")) {
                        UIHelper.showAlertDialog(activity, "Thông  báo", resConfirm.getMessage(), R.drawable.ic_success_35);
                    } else {
                        UIHelper.showAlertDialog(activity, "Thông  báo", resConfirm.getMessage(), R.drawable.bad_face_35);
                    }
                }

            }

            @Override
            public void Error(String error) {
                loadingCongfirm.cancel();
                UIHelper.showAlertDialog(activity, "FAILURE", error, R.drawable.bad_face_35);

            }
        }, jsonObject.toString());
    }

}
