package com.example.karama.views;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karama.MainActivity;
import com.example.karama.R;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.IInterfaceModel;
import com.example.karama.helper.UHelper;
import com.example.karama.helper.UIHelper;
import com.example.karama.model.person.ResProfile;
import com.example.karama.services.APIServices;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class DialogRegis extends Dialog {
    public Activity activity;
    EditText txt_username,txt_pass,txt_cf_pass,txt_first_name,txt_last_name, txt_mail,txt_sex;
    TextView tvcfpass;
    Button btn_signin;
    ImageView img_close;
    ProgressDialog pd;
    public DialogRegis(Activity context) {
        super(context);
        this.activity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_sign_in);
        initView();
        initClick();

    }

    private void initView() {
        txt_username = findViewById(R.id.txt_username);
        txt_first_name = findViewById(R.id.txt_first_name);
        txt_last_name = findViewById(R.id.txt_last_name);
        txt_pass = findViewById(R.id.txt_pass);
        txt_cf_pass = findViewById(R.id.txt_cf_pass);
        txt_mail = findViewById(R.id.txt_mail);
        txt_sex = findViewById(R.id.txt_sex);
        btn_signin = findViewById(R.id.btn_signin);
        img_close = findViewById(R.id.img_close);
        tvcfpass = findViewById(R.id.tvcfpass);
        pd= new ProgressDialog(activity);
        pd.setMessage("Đang đăng kí _ Vui lòng chờ!");
    }

    private void initClick() {
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNullReg();
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        txt_cf_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("==cfpass:", s.toString());
                if (s.toString().equals(txt_pass.getText().toString())) {
                    tvcfpass.setTextColor(Color.parseColor("#50EA14"));
                } else {
                    tvcfpass.setTextColor(Color.parseColor("#E4122A"));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkNullReg() {
        String username = UHelper.onTextStr(txt_username);
        String pass = UHelper.onTextStr(txt_pass);
        String confirmPass = UHelper.onTextStr(txt_cf_pass);
        String gender = UHelper.onTextStr(txt_sex);
        String firstName = UHelper.onTextStr(txt_first_name);
        String lastName = UHelper.onTextStr(txt_last_name);
        String mail = UHelper.onTextStr(txt_mail);
        if (UHelper.onValidate(username)) {
            Toast.makeText(activity, "Vui lòng nhập Username", Toast.LENGTH_SHORT).show();
            txt_username.requestFocus();
        } else if (UHelper.onValidate(pass)) {
            Toast.makeText(activity, "Vui lòng nhập Password", Toast.LENGTH_SHORT).show();
            txt_pass.requestFocus();
        } else if (UHelper.onValidate(confirmPass)) {
            Toast.makeText(activity, "Vui lòng nhập Confirm password", Toast.LENGTH_SHORT).show();
            txt_cf_pass.requestFocus();
        } else if (UHelper.onValidate(firstName)) {
            Toast.makeText(activity, "Vui lòng nhập First Name", Toast.LENGTH_SHORT).show();
            txt_first_name.requestFocus();
        } else if (UHelper.onValidate(lastName)) {
            Toast.makeText(activity, "Vui lòng nhập Last Name", Toast.LENGTH_SHORT).show();
            txt_last_name.requestFocus();
        } else if (UHelper.onValidate(gender)) {
            Toast.makeText(activity, "Vui lòng nhập giới tính", Toast.LENGTH_SHORT).show();
            txt_sex.requestFocus();
        } else if (UHelper.onValidate(mail)) {
            Toast.makeText(activity, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            txt_mail.requestFocus();
        } else if (!pass.equals(confirmPass)) {
            Toast.makeText(activity, "Confirm Failure", Toast.LENGTH_SHORT).show();
            txt_cf_pass.requestFocus();
        } else {
            Log.e("==reg:", "valid");
            // call api send otp
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", username);
                jsonObject.put("password", confirmPass);
                jsonObject.put("firstName", firstName);
                jsonObject.put("lastName", lastName);
                jsonObject.put("gender", gender);
                jsonObject.put("email", mail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("==paramReg:", jsonObject.toString());


            pd.show();
            Log.e("==callRegis", "here");
            APIServices.regis(new CallbackResponse() {
                @Override
                public void Success(Response<?> response) {
                    pd.cancel();
                    ResProfile resRegis = (ResProfile) response.body();
                    if (resRegis != null) {
                        if (resRegis.getStatus().equals("200")) {
                            UIHelper.showAlertDialogV3(activity, "SUCCESS", resRegis.getMessage(), R.drawable.ic_success_35, new IInterfaceModel.OnBackIInterface() {
                                @Override
                                public void onSuccess() {
                                    if (MainActivity.getManagerInstance() != null) {
                                        MainActivity.getManagerInstance().dialogOTP(username);
                                    }
                                }
                            });
                        } else {
                            UIHelper.showAlertDialog(activity,"ERROR",resRegis.getMessage(),R.drawable.bad_face_35);
                        }

                    }
                    ResponseBody resRegisErr = response.errorBody();
//                    ResProfile resRegisErr = (ResProfile) response.errorBody();

                }

                @Override
                public void Error(String error) {
                    pd.cancel();
                    UIHelper.showAlertDialog(activity,"ERROR",error,R.drawable.ic_err_35);
                }
            }, jsonObject.toString());

        }





    }
}
