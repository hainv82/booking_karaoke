package com.example.karama.views;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.karama.MainActivity;
import com.example.karama.R;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.IInterfaceModel;
import com.example.karama.helper.UHelper;
import com.example.karama.helper.UIHelper;
import com.example.karama.model.person.Customer;
import com.example.karama.model.person.ResCustomer;
import com.example.karama.model.ResNullData;
import com.example.karama.services.KaraServices;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;

public class DialogAddCustomer extends Dialog {
    Activity activity;
    ImageView view_colse;
    EditText member_fname,mem_lname,mem_gender,mem_adress,mem_phone, mem_email;
    TextView tv_add,tv_update;
    String typeUser;
    Customer customer;
    ProgressDialog loadingAddCustomer,loadingUpdateCustomer;
    public DialogAddCustomer(@NonNull Activity context,String type) {
        super(context);
        this.activity = context;
        this.typeUser = type;
    }

    public DialogAddCustomer(@NonNull Activity activity, String typeUser, Customer customer) {
        super(activity);
        this.activity = activity;
        this.typeUser = typeUser;
        this.customer = customer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_customer);

        initView();
        view_colse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check input and call api add
                onCheckNull("ADD");
            }
        });
        //tv_update -chua xu ly
        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckNull("UPDATE");
            }
        });

    }

    private void onCheckNull(String typeuse) {
        String fname = UHelper.onTextStr(member_fname);
        String lname = UHelper.onTextStr(mem_lname);
        String gend = UHelper.onTextStr(mem_gender);
        String adres = UHelper.onTextStr(mem_adress);
        String phone = UHelper.onTextStr(mem_phone);
        String mail = UHelper.onTextStr(mem_email);
        if (UHelper.onValidate(fname)) {
            Toast.makeText(activity, "Vui lòng nhập first name", Toast.LENGTH_SHORT).show();
            member_fname.requestFocus();
        } else if (UHelper.onValidate(lname)) {
            Toast.makeText(activity, "Vui lòng nhập last name", Toast.LENGTH_SHORT).show();
            mem_lname.requestFocus();
        }else if (UHelper.onValidate(gend)) {
            Toast.makeText(activity, "Vui lòng nhập giới tính", Toast.LENGTH_SHORT).show();
            mem_gender.requestFocus();
        }else if (UHelper.onValidate(adres)) {
            Toast.makeText(activity, "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show();
            mem_adress.requestFocus();
        }else if (UHelper.onValidate(phone)) {
            Toast.makeText(activity, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            mem_phone.requestFocus();
        } else if (UHelper.onValidate(mail) && typeuse.equals("ADD")) {

            Toast.makeText(activity, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            mem_email.requestFocus();
        } else {
            UIHelper.showAlertDialogV3(activity, "CONFIRM", "Bạn chắc chắn đã kiểm tra kỹ thông tin ", R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                @Override
                public void onSuccess() {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("firstName", fname);
                        jsonObject.put("lastName", lname);
                        jsonObject.put("gender", gend);
                        jsonObject.put("address1", adres);
                        jsonObject.put("phoneNumber", phone);
                        if (typeuse.equals("ADD")) {
                            jsonObject.put("email", mail);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (typeuse.equals("ADD")) {
                        loadingAddCustomer.show();
                        KaraServices.addCustomer(new CallbackResponse() {
                            @Override
                            public void Success(Response<?> response) {
                                loadingAddCustomer.cancel();
                                ResCustomer resAddCustomer = (ResCustomer) response.body();
                                if (resAddCustomer != null) {
                                    if (resAddCustomer.getStatus().equals("200")) {
                                        UIHelper.showAlertDialogV3(activity, "SUCCESS", resAddCustomer.getData().toString(), R.drawable.ic_success_35, new IInterfaceModel.OnBackIInterface() {
                                            @Override
                                            public void onSuccess() {
                                                dismiss();
                                            }
                                        });
                                    } else if (resAddCustomer.getStatus().equals("403")){
                                        UIHelper.showAlertDialogV3(activity, resAddCustomer.getStatus(), resAddCustomer.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                                            @Override
                                            public void onSuccess() {
                                                Intent i = new Intent(activity, MainActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                activity.startActivity(i);
                                            }
                                        });
                                    } else{
                                        UIHelper.showAlertDialog(activity,resAddCustomer.getStatus(),resAddCustomer.getMessage(),R.drawable.troll_64);
                                    }
                                }
                            }

                            @Override
                            public void Error(String error) {
                                loadingAddCustomer.cancel();
                                UIHelper.showAlertDialog(activity, "ERROR", error, R.drawable.troll_64);

                            }
                        },jsonObject.toString());
                    }

                    if (typeuse.equals("UPDATE")) {
                        loadingUpdateCustomer.show();
                        KaraServices.updateCustomer(new CallbackResponse() {
                            @Override
                            public void Success(Response<?> response) {
                                loadingUpdateCustomer.cancel();
                                ResNullData resUpdate = (ResNullData) response.body();
                                if (resUpdate != null) {
                                    if (resUpdate.getStatus().equals("200")) {
                                        UIHelper.showAlertDialogV3(activity, "SUCCESS", "Update thông tin khách hàng thành công", R.drawable.ic_success_35, new IInterfaceModel.OnBackIInterface() {
                                            @Override
                                            public void onSuccess() {
                                                dismiss();
                                            }
                                        });
                                    } else if (resUpdate.getStatus().equals("403")){
                                        UIHelper.showAlertDialogV3(activity, resUpdate.getStatus(), resUpdate.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                                            @Override
                                            public void onSuccess() {
                                                Intent i = new Intent(activity, MainActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                activity.startActivity(i);
                                            }
                                        });
                                    } else{
                                        UIHelper.showAlertDialog(activity,resUpdate.getStatus(),resUpdate.getMessage(),R.drawable.troll_64);
                                    }
                                }
                            }

                            @Override
                            public void Error(String error) {
                                loadingUpdateCustomer.cancel();
                                UIHelper.showAlertDialog(activity, "ERROR", error, R.drawable.troll_64);

                            }
                        },jsonObject.toString(),phone);
                    }


                }
            });
        }
    }

    private void initView() {
        member_fname = findViewById(R.id.member_fname);
        mem_lname = findViewById(R.id.mem_lname);
        mem_gender = findViewById(R.id.mem_gender);
        mem_adress = findViewById(R.id.mem_adress);
        mem_phone = findViewById(R.id.mem_phone);
        mem_email = findViewById(R.id.mem_email);
        tv_add = findViewById(R.id.tv_add);
        tv_update = findViewById(R.id.tv_update);
        view_colse = findViewById(R.id.view_colse);
        loadingAddCustomer = new ProgressDialog(activity);
        loadingUpdateCustomer = new ProgressDialog(activity);
        loadingAddCustomer.setMessage("Đang thêm mới khách hàng ...");
        loadingUpdateCustomer.setMessage("Đang update thông tin khách hàng...");
        if (typeUser.equals("UPDATE")) {
            member_fname.setText(customer.getFirstName());
            mem_lname.setText(customer.getLastName());
            mem_gender.setText(customer.getGender());
            mem_adress.setText(customer.getAddress1());
            mem_phone.setText(customer.getPhoneNumber());
//            mem_email.setText(customer.getEmail());
            mem_email.setVisibility(View.GONE);
            tv_update.setVisibility(View.VISIBLE);
        }
        if (typeUser.equals("ADD")) {
            tv_add.setVisibility(View.VISIBLE);
        }
    }
}
