package com.example.karama.views;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.karama.MainActivity;
import com.example.karama.MemberActivity;
import com.example.karama.R;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.IInterfaceModel;
import com.example.karama.helper.UIHelper;
import com.example.karama.model.person.Customer;
import com.example.karama.model.ResNullData;
import com.example.karama.services.KaraServices;

import retrofit2.Response;

public class DialogDetailCustomer extends Dialog implements View.OnClickListener {
    public Activity activity;
    Customer customer;
    ImageView img_close,img_staff;
    TextView unlock_mem,lock_mem, update_mem,txt_first_name,txt_last_name,txt_gender,txt_email,txt_sdt,txt_adress1;
    ProgressDialog loadingLockMem, loadingUnlockMem;

    public DialogDetailCustomer(@NonNull Activity context, Customer customer) {
        super(context);
        this.activity = context;
        this.customer = customer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.card_member);
        initView();
        initClick();
    }

    private void initView() {
        img_close = findViewById(R.id.img_close);
        unlock_mem = findViewById(R.id.unlock_mem);
        lock_mem = findViewById(R.id.lock_mem);
        lock_mem.setVisibility(View.GONE);
        unlock_mem.setVisibility(View.GONE);
        update_mem = findViewById(R.id.update_mem);
        loadingLockMem = new ProgressDialog(activity);
        loadingUnlockMem = new ProgressDialog(activity);
        loadingLockMem.setMessage("Đang vô hiệu hóa khách hàng ...");
        loadingUnlockMem.setMessage("Đang kích hoạt lại khách hàng ...");

        txt_first_name = findViewById(R.id.txt_first_name);
        txt_last_name = findViewById(R.id.txt_last_name);
        txt_gender = findViewById(R.id.txt_gender);
        txt_email = findViewById(R.id.txt_email);
        txt_sdt = findViewById(R.id.txt_sdt);
        txt_adress1 = findViewById(R.id.txt_adress1);
        img_staff = findViewById(R.id.img_staff);

        txt_first_name.setText(customer.getFirstName());
        txt_last_name.setText(customer.getLastName());
        txt_gender.setText(customer.getGender());
        txt_email.setText(customer.getEmail());
        txt_sdt.setText(customer.getPhoneNumber());
        txt_adress1.setText(customer.getAddress1());
        if (customer.getStatus().equals("1")) {
            lock_mem.setVisibility(View.VISIBLE);
        } else {
            unlock_mem.setVisibility(View.VISIBLE);
        }
        if (customer.getGender().equals("FEMALE")) {
            img_staff.setImageDrawable(activity.getDrawable(R.drawable.sing_girl48));
        }
    }

    private void initClick() {
        img_close.setOnClickListener(this);
        update_mem.setOnClickListener(this);
        lock_mem.setOnClickListener(this);
        unlock_mem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                break;
                //lock, unlock , update -call api
            case R.id.update_mem:
                DialogAddCustomer dialogUpdateustomer = new DialogAddCustomer(activity, "UPDATE", customer);
                dialogUpdateustomer.setCanceledOnTouchOutside(false);
                dialogUpdateustomer.show();
                break;
            case R.id.lock_mem:
                UIHelper.showAlertDialogV3(activity, "COFIRM", "Bạn chắc chắc muốn vô hiệu hóa khách hàng này ?", R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                    @Override
                    public void onSuccess() {
                        lockMember();
                    }
                });
                break;
            case R.id.unlock_mem:
                UIHelper.showAlertDialogV3(activity, "COFIRM", "Bạn chắc chắc muốn kích hoạt khách hàng này ?", R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                    @Override
                    public void onSuccess() {
                        unlockMember();
                    }
                });
                break;

        }
    }

    private void unlockMember() {
        loadingUnlockMem.show();
        KaraServices.unlockCustomer(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                loadingUnlockMem.cancel();
                ResNullData resUnlock = (ResNullData) response.body();
                if (resUnlock != null) {
                    if (resUnlock.getStatus().equals("200")) {
                        dismiss();
                        MemberActivity.getInstance().loadCustomer();

                    } else if (resUnlock.getStatus().equals("403")){
                        UIHelper.showAlertDialogV3(activity, resUnlock.getStatus(), resUnlock.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
                                Intent i = new Intent(activity, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                activity.startActivity(i);
                            }
                        });
                    } else{
                        UIHelper.showAlertDialog(activity,resUnlock.getStatus(),resUnlock.getMessage(),R.drawable.troll_64);
                    }
                }
            }

            @Override
            public void Error(String error) {
                loadingUnlockMem.cancel();
                UIHelper.showAlertDialog(activity, "ERROR", error, R.drawable.bad_face_35);

            }
        }, customer.getPhoneNumber());
    }

    private void lockMember() {
        loadingLockMem.show();
        KaraServices.lockCustomer(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                loadingLockMem.cancel();
                ResNullData resLock = (ResNullData) response.body();
                if (resLock != null) {
                    if (resLock.getStatus().equals("200")) {
                        dismiss();
                        MemberActivity.getInstance().loadCustomer();
                    } else if (resLock.getStatus().equals("403")){
                        UIHelper.showAlertDialogV3(activity, resLock.getStatus(), resLock.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
                                Intent i = new Intent(activity, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                activity.startActivity(i);
                            }
                        });
                    } else{
                        UIHelper.showAlertDialog(activity,resLock.getStatus(),resLock.getMessage(),R.drawable.troll_64);
                    }
                }
            }

            @Override
            public void Error(String error) {
                loadingLockMem.cancel();
                UIHelper.showAlertDialog(activity,"ERROR",error,R.drawable.troll_64);

            }
        }, customer.getPhoneNumber());


    }
}
