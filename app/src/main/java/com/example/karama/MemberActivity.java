package com.example.karama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karama.adapter.CustomerAdapter;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.IInterfaceModel;
import com.example.karama.helper.UIHelper;
import com.example.karama.model.person.Customer;
import com.example.karama.model.person.ResAllCustomer;
import com.example.karama.services.KaraServices;
import com.example.karama.views.DialogAddCustomer;
import com.example.karama.views.DialogDetailCustomer;
import com.example.karama.views.DialogReceptByPhone;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class MemberActivity extends AppCompatActivity implements View.OnClickListener {
    public Context mContext=this;
    TextView add_customer;
    ImageView view_exit;
    TextView textlist;
    RecyclerView rcv_customer;
    ProgressDialog loadingGetCustomer;
    public List<Customer> customerList,listShow;
    CustomerAdapter customerAdapter;
    private static MemberActivity instance;
    EditText sdt_input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        getSupportActionBar().hide();

        initView();
        initClick();
        loadCustomer();
    }

    private void initClick() {
        add_customer.setOnClickListener(this);
        view_exit.setOnClickListener(this);
        textlist.setOnClickListener(this);
        sdt_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("==", s.toString());
                listShow.clear();
                for (int i = 0; i < customerList.size(); i++) {
                    if (customerList.get(i).getPhoneNumber().contains(s.toString())) {
                        listShow.add(customerList.get(i));
                    }
                }
                customerAdapter = new CustomerAdapter(mContext, listShow);
                rcv_customer.setAdapter(customerAdapter);
                customerAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static MemberActivity getInstance() {
        if (instance == null) {
            instance = new MemberActivity();
        }
        return instance;
    }
    public void showDetailCustomer(Customer customer) {
        DialogDetailCustomer dialogDetailCustomer = new DialogDetailCustomer(MemberActivity.this, customer);
        dialogDetailCustomer.setCanceledOnTouchOutside(false);
        dialogDetailCustomer.show();
    }
    private void initView() {
        sdt_input = findViewById(R.id.sdt_input);
        instance = this;
        loadingGetCustomer=new ProgressDialog(mContext);
        loadingGetCustomer.setMessage("Đang lấy danh sách khách hàng ...");
        loadingGetCustomer.setCancelable(false);
        add_customer = findViewById(R.id.add_customer);
        view_exit = findViewById(R.id.view_exit);
        textlist = findViewById(R.id.textlist);
        rcv_customer = findViewById(R.id.rcv_customer);
        rcv_customer.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        customerList = new ArrayList<>();
        listShow = new ArrayList<>();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_customer:
                DialogAddCustomer dialogAddCustomer = new DialogAddCustomer(MemberActivity.this,"ADD");
                dialogAddCustomer.setCanceledOnTouchOutside(false);
                dialogAddCustomer.show();
                break;
            case R.id.view_exit:
                onBackPressed();
                break;
            case R.id.textlist:
                loadCustomer();
                break;

        }
    }

    public void loadCustomer() {
        customerList.clear();
        loadingGetCustomer.show();
        KaraServices.getAllCustomer(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                loadingGetCustomer.cancel();
                ResAllCustomer resAllCustomer = (ResAllCustomer) response.body();
                if (resAllCustomer != null) {
                    if (resAllCustomer.getStatus().equals("200")) {
                        for (int i = 0; i < resAllCustomer.getData().getData().size(); i++) {
                            Customer customer=resAllCustomer.getData().getData().get(i);
                            customerList.add(customer);
                        }
                        customerAdapter = new CustomerAdapter(mContext, customerList);
                        rcv_customer.setAdapter(customerAdapter);
                        customerAdapter.notifyDataSetChanged();
                    } else if (resAllCustomer.getStatus().equals("403")){
                        UIHelper.showAlertDialogV3(mContext, resAllCustomer.getStatus(), resAllCustomer.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
                                Intent i = new Intent(MemberActivity.this, MainActivity.class);
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
                loadingGetCustomer.cancel();
                UIHelper.showAlertDialog(mContext,"ERROR",error,R.drawable.troll_64);

            }
        },"0","30","ASC");
    }

    public void getDialogRecept(String sdt) {
        DialogReceptByPhone dialogReceptByPhone = new DialogReceptByPhone(MemberActivity.this, sdt);
        dialogReceptByPhone.setCancelable(false);
        dialogReceptByPhone.show();

    }
}