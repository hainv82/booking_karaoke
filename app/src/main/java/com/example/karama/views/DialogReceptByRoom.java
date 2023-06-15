package com.example.karama.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karama.MainActivity;
import com.example.karama.R;
import com.example.karama.adapter.ReceptByPhoneAdapter;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.IInterfaceModel;
import com.example.karama.helper.UIHelper;
import com.example.karama.model.room.DataOrder;
import com.example.karama.model.room.Res13ListOrder;
import com.example.karama.services.RumServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class DialogReceptByRoom extends Dialog implements View.OnClickListener {
    Activity activity;
    String roomId;
    DataOrder order;
    RecyclerView recyclerView;
    ReceptByPhoneAdapter receptByPhoneAdapter;
    List<DataOrder> orderList,listShow;
    TextView title;
    ImageView view_close;
    RadioButton rdbtn_booked,rdbtn_cancel, rdbtn_done,rdbtn_pending,rdbtn_all;
    private static DialogReceptByRoom instance;
    public DialogReceptByRoom(@NonNull Activity context,String roomId) {
        super(context);
        this.activity = context;
        this.roomId = roomId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_recept_by_phone);
        initView();

        loadAllOrderRecept();
        rdbtn_booked.setOnClickListener(this);
        rdbtn_cancel.setOnClickListener(this);
        rdbtn_done.setOnClickListener(this);
        rdbtn_pending.setOnClickListener(this);
        rdbtn_all.setOnClickListener(this);
        view_close.setOnClickListener(this);
    }

    public void loadAllOrderRecept() {
        RumServices.getListOrder(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                Res13ListOrder resListOrderRecept = (Res13ListOrder) response.body();
                if (resListOrderRecept != null) {
                    if (resListOrderRecept.getStatus().equals("200")) {
                        //suces
                        orderList.clear();
                        for (int i = 0; i < resListOrderRecept.getData().getData().size(); i++) {
                            DataOrder recep=resListOrderRecept.getData().getData().get(i);
                            orderList.add(recep);

                        }
                        receptByPhoneAdapter = new ReceptByPhoneAdapter(activity.getApplicationContext(), orderList,"ROOMID");
                        recyclerView.setAdapter(receptByPhoneAdapter);
                        receptByPhoneAdapter.notifyDataSetChanged();
                    } else if (resListOrderRecept.getStatus().equals("403")){
                        UIHelper.showAlertDialogV3(activity, resListOrderRecept.getStatus(), resListOrderRecept.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
                                Intent i = new Intent(activity, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                activity.startActivity(i);
                            }
                        });
                    } else{
                        UIHelper.showAlertDialog(activity,resListOrderRecept.getStatus(),resListOrderRecept.getMessage(),R.drawable.troll_64);
                    }
                }
            }

            @Override
            public void Error(String error) {

            }
        },roomId);
    }
    public static DialogReceptByRoom getInstance() {
        if (instance == null) {
            instance = new DialogReceptByRoom(instance.activity, getInstance().roomId);
        }
        return instance;
    }

    private void initView() {
        instance = this;
        orderList = new ArrayList<>();
        listShow = new ArrayList<>();
        title = findViewById(R.id.title_order);
        recyclerView = findViewById(R.id.rcv_order);
        view_close = findViewById(R.id.view_close);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity.getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        rdbtn_booked = findViewById(R.id.rdbtn_booked);
        rdbtn_cancel = findViewById(R.id.rdbtn_cancel);
        rdbtn_done = findViewById(R.id.rdbtn_done);
        rdbtn_pending = findViewById(R.id.rdbtn_pending);
        rdbtn_all = findViewById(R.id.rdbtn_all);
        rdbtn_all.setChecked(true);
        title.setText("List order by "+roomId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rdbtn_booked:
                Log.e("==", "rdbtn_booked");
                reloadOrderRecept("BOOKED");
                break;
            case R.id.rdbtn_done:
                Log.e("==", "rdbtn_done");
                reloadOrderRecept("DONE");
                break;
            case R.id.rdbtn_cancel:
                Log.e("==", "rdbtn_cancel");
                reloadOrderRecept("CANCEL");
                break;
            case R.id.rdbtn_pending:
                Log.e("==", "rdbtn_pending");
                reloadOrderRecept("PENDING");
                break;
            case R.id.rdbtn_all:
                loadAllOrderRecept();
                break;
            case R.id.view_close:
                dismiss();
                break;
        }

    }
    private void reloadOrderRecept(String type) {
        listShow.clear();
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getStatusCodeName().equals(type)) {
                listShow.add(orderList.get(i));
            }
        }
        receptByPhoneAdapter = new ReceptByPhoneAdapter(activity.getApplicationContext(), listShow,"ROOMID");
        recyclerView.setAdapter(receptByPhoneAdapter);
        receptByPhoneAdapter.notifyDataSetChanged();
    }
}
