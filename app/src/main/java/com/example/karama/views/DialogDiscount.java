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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.karama.DetailBill;
import com.example.karama.MainActivity;
import com.example.karama.R;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.UIHelper;
import com.example.karama.model.ResNullData;
import com.example.karama.services.OrderServices;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;

public class DialogDiscount extends Dialog {
    Activity activity;
    EditText dc_pcent, dc_money;
    TextView apply;
    String pcent="",mney="";
    String bookingID;
    public DialogDiscount(@NonNull Activity context,String bookingID) {
        super(context);
        this.activity = context;
        this.bookingID = bookingID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_discount);

        initView();
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();

                if (dc_pcent.getText().toString().equals("")) {
                    pcent = "0";
                } else {
                    pcent = dc_pcent.getText().toString();
                }

                if (dc_money.getText().toString().equals("")) {
                    mney = "0";
                } else {
                    mney = dc_money.getText().toString();
                }

                if (pcent.equals("0") && mney.equals("0")) {
                    Toast.makeText(activity, "Bạn chưa nhập giảm giá", Toast.LENGTH_SHORT).show();
                } else {
                    if (!pcent.equals("0")) {
                        try {
                            jsonObject.put("discountPercent", pcent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!mney.equals("0")) {
                        try {
                            jsonObject.put("discountMoney", mney);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    String dcount= jsonObject.toString();
                    Log.e("==pram_discount:", jsonObject.toString());
                    OrderServices.discountBillByBookingID(new CallbackResponse() {
                        @Override
                        public void Success(Response<?> response) {
                            ResNullData resDiscount = (ResNullData) response.body();
                            if (resDiscount != null) {
                                if (resDiscount.getStatus().equals("200")) {
                                    Toast.makeText(activity, "Giảm giá thành công", Toast.LENGTH_SHORT).show();
                                    //reload
                                    DetailBill.getInstance().f5();
                                    dismiss();
                                } else if (resDiscount.getStatus().equals("403")){
                                    Log.e("==403", resDiscount.getMessage());
                                    Toast.makeText(activity, "", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(activity, MainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    activity.startActivity(i);
                                } else{
                                    UIHelper.showAlertDialog(activity,resDiscount.getStatus(),resDiscount.getMessage(),R.drawable.troll_64);
                                    Toast.makeText(activity, resDiscount.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("==err", resDiscount.getStatus() + "-" + resDiscount.getMessage());
                                }
                            }
                        }

                        @Override
                        public void Error(String error) {
                            dismiss();
                            Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
                            Log.e("==err", error);

                        }
                    }, bookingID, dcount);
                }
            }
        });
    }

    private void initView() {
        dc_money = findViewById(R.id.dc_money);
        dc_pcent = findViewById(R.id.dc_pcent);
        apply = findViewById(R.id.apply);


    }
}
