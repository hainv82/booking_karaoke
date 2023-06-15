package com.example.karama.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karama.R;
import com.example.karama.adapter.ItemOrderAdapter;
import com.example.karama.model.order.DataBill;

public class DialogPayment extends Dialog {
    Activity activity;
    Context mContext;
    DataBill dataBill;
    TextView id,bookingId,guestPhoneNumber,numberHoursBooked,discountPercent,discountMoney, total;
    ItemOrderAdapter adapter;
    RecyclerView rcv_order;

    public DialogPayment(@NonNull Activity context,Context context2,DataBill dataBill) {
        super(context);
        this.activity = context;
        this.dataBill = dataBill;
        this.mContext = context2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.payment_bill);
        initView();


    }

    private void initView() {
        id = findViewById(R.id.id);
        bookingId = findViewById(R.id.bookingId);
        guestPhoneNumber = findViewById(R.id.guestPhoneNumber);
        numberHoursBooked = findViewById(R.id.numberHoursBooked);
        discountPercent = findViewById(R.id.discountPercent);
        discountMoney = findViewById(R.id.discountMoney);
        total = findViewById(R.id.total);
        rcv_order = findViewById(R.id.rcv_order);
        rcv_order.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        id.setText("ID: "+dataBill.getId());
        bookingId.setText("BOOKINGID: "+dataBill.getBookingId());
        guestPhoneNumber.setText("GUEST PHONE: "+dataBill.getGuestPhoneNumber());
        numberHoursBooked.setText("HOURS BOOKED: "+dataBill.getNumberHoursBooked());
        discountPercent.setText("Discount: "+dataBill.getDiscountPercent().split("\\.")+"%");
        discountMoney.setText("Discount: "+dataBill.getDiscountMoney().split("\\.")+" VND");
        total.setText("TOTAL: "+dataBill.getTotal().split("\\.")+" VND");

        adapter = new ItemOrderAdapter(mContext, dataBill.getProducts());
        rcv_order.setAdapter(adapter);

    }
}

