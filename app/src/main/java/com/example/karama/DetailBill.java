package com.example.karama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karama.adapter.ItemOrderAdapter;
import com.example.karama.adapter.OrderItemToBillAdapter;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.IInterfaceModel;
import com.example.karama.helper.UIHelper;
import com.example.karama.model.ResNullData;
import com.example.karama.model.order.DataBill;
import com.example.karama.model.order.ProductInBill;
import com.example.karama.model.order.ResBill;
import com.example.karama.model.product.Products;
import com.example.karama.model.product.ResAllProducts;
import com.example.karama.model.room.ResOrder;
import com.example.karama.services.OrderServices;
import com.example.karama.services.ProdServices;
import com.example.karama.services.RumServices;
import com.example.karama.views.DialogDiscount;
import com.example.karama.views.DialogOrderToBill;
import com.example.karama.views.DialogPayment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class DetailBill extends AppCompatActivity implements View.OnClickListener {
    //recept
    TextView bookingId,roomId,orderId,time_start, statusCode;
    //event
    TextView discount,destroy_bill,order, pay;
    //bill
    TextView billId,time_used,discount_percent, percent_money,bill_sdt;
    RecyclerView rcv_order;
    String BOOKINGID="";
    Context mContext = this;
    List<ProductInBill> productInBillList;
    ItemOrderAdapter itemOrderAdapter;
    private static DetailBill instance;
    Dialog dialogOrder;
    List<Products> productsList;
    OrderItemToBillAdapter orderItemToBillAdapter;
    String ORDERID;
    ImageView view_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bill);
        getSupportActionBar().hide();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                BOOKINGID= null;
            } else {
                BOOKINGID= extras.getString("BOOKINGID");
            }
        } else {
            BOOKINGID= (String) savedInstanceState.getSerializable("BOOKINGID");
        }

        initView();
        initClick();
        loadRecept(BOOKINGID);
        loadBill(BOOKINGID);

    }

    private void loadBill(String bookingid) {
        OrderServices.getBillByBookingID(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                ResBill resBill = (ResBill) response.body();
                if (resBill != null) {
                    if (resBill.getStatus().equals("200")) {
                        billId.setText("BILL_ID: "+resBill.getData().getId());
                        try {
                            discount_percent.setText(resBill.getData().getDiscountPercent().split("\\.")[0]+"%");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            percent_money.setText(resBill.getData().getDiscountMoney().split("\\.")[0]+" VND");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        bill_sdt.setText(resBill.getData().getGuestPhoneNumber());
                        productInBillList.clear();
                        Log.e("==sizeProd", String.valueOf(resBill.getData().getProducts().size()));
                        if (resBill.getData().getProducts().size() >= 1) {
                            for (int i = 0; i < resBill.getData().getProducts().size(); i++) {
                                ProductInBill item = resBill.getData().getProducts().get(i);
                                productInBillList.add(item);
                            }
                            Log.e("==productInBillList", "size" + String.valueOf(productInBillList.size()));
                            itemOrderAdapter = new ItemOrderAdapter(mContext, productInBillList);
                            for (int i = 0; i < productInBillList.size(); i++) {
                                Log.e("==prod:", productInBillList.get(i).getName());

                            }
                            rcv_order.setAdapter(itemOrderAdapter);
                            itemOrderAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void Error(String error) {

            }
        },bookingid);
    }

    private void loadRecept(String bookingid) {
        RumServices.getOrderByBookingID(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                ResOrder resOrder = (ResOrder) response.body();
                if (resOrder != null) {
                    if (resOrder.getStatus().equals("200")) {
                        bookingId.setText("BOOKINGID: "+resOrder.getData().getBookingId());
                        roomId.setText("ROOMID: "+resOrder.getData().getRoomId());
                        orderId.setText("ORDERID: "+resOrder.getData().getOrderId());
                        ORDERID = resOrder.getData().getOrderId();
                        try {
                            time_start.setText(resOrder.getData().getStartTime().split("T")[0]+"\n"+resOrder.getData().getStartTime().split("T")[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        statusCode.setText(resOrder.getData().getStatusCodeName());
                    } else if (resOrder.getStatus().equals("403")){
                        Log.e("==403", resOrder.getMessage());
                        Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(DetailBill.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else{
                        UIHelper.showAlertDialog(mContext,resOrder.getStatus(),resOrder.getMessage(),R.drawable.troll_64);
                        Toast.makeText(mContext, resOrder.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("==err", resOrder.getStatus() + "-" + resOrder.getMessage());
                    }
                }
            }

            @Override
            public void Error(String error) {
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                Log.e("==Error", error);
            }
        },bookingid);
    }

    private void initClick() {
        order.setOnClickListener(this);
        discount.setOnClickListener(this);
        pay.setOnClickListener(this);
        destroy_bill.setOnClickListener(this);
        view_exit.setOnClickListener(this);
    }

    public static DetailBill getInstance() {
        if (instance == null) {
            instance = new DetailBill();
        }
        return instance;
    }
    private void initView() {
        instance = this;
        productInBillList = new ArrayList<>();
        bookingId = findViewById(R.id.bookingId);
        roomId = findViewById(R.id.roomId);
        orderId = findViewById(R.id.orderId);
        time_start = findViewById(R.id.time_start);
        statusCode = findViewById(R.id.statusCode);
        discount = findViewById(R.id.discount);
        destroy_bill = findViewById(R.id.destroy_bill);
        order = findViewById(R.id.order);
        pay = findViewById(R.id.pay);
        billId = findViewById(R.id.billId);
        time_used = findViewById(R.id.time_used);
        discount_percent = findViewById(R.id.discount_percent);
        bill_sdt = findViewById(R.id.bill_sdt);
        percent_money = findViewById(R.id.percent_money);
        rcv_order = findViewById(R.id.rcv_order);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rcv_order.setLayoutManager(layoutManager);
        dialogOrder = new Dialog(mContext);
        productsList = new ArrayList<>();
        view_exit = findViewById(R.id.view_exit);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order:
                showDialogOrder();
                break;
            case R.id.discount:
                discount();
                break;
            case R.id.destroy_bill:
                destBill();
                break;
            case R.id.pay:
                payment();
                break;
            case R.id.view_exit:
                onBackPressed();
                break;
        }
    }

    private void destBill() {
        OrderServices.detroyBill(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                ResNullData resDest = (ResNullData) response.body();
                if (resDest.getStatus().equals("200")) {
                    // back ->reload -> hide status cancel
                    onBackPressed();
                } else if (resDest.getStatus().equals("403")){
                    Log.e("==403", resDest.getMessage());
                    Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(DetailBill.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else{
                    UIHelper.showAlertDialog(mContext,resDest.getStatus(),resDest.getMessage(),R.drawable.troll_64);
                    Toast.makeText(mContext, resDest.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("==err", resDest.getStatus() + "-" + resDest.getMessage());
                }
            }

            @Override
            public void Error(String error) {

            }
        },ORDERID);
    }

    private void payment() {
        OrderServices.payment(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                ResBill resBill = (ResBill) response.body();
                if (resBill.getStatus().equals("200")) {
                    // show diloag bill-> DataaBill
                    Log.e("==pay:", "200"+resBill.getData());

                    DialogPayment dialogPayment = new DialogPayment(DetailBill.this, mContext,resBill.getData());
                    dialogPayment.show();

                } else if (resBill.getStatus().equals("403")){
                    Log.e("==403", resBill.getMessage());
                    Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(DetailBill.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else{
                    UIHelper.showAlertDialog(mContext,resBill.getStatus(),resBill.getMessage(),R.drawable.troll_64);
                    Toast.makeText(mContext, resBill.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("==err", resBill.getStatus() + "-" + resBill.getMessage());
                }
            }

            @Override
            public void Error(String error) {

            }
        },ORDERID);
    }

    private void showDialogOrder() {
        dialogOrder.setCanceledOnTouchOutside(false);
        dialogOrder.setContentView(R.layout.dialog_order_items);
        // Set dialog title
        dialogOrder.setTitle("Chose items order");
        Window window = dialogOrder.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialogOrder.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        RecyclerView rcv_items = dialogOrder.findViewById(R.id.rcv_items);
        rcv_items.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        ProdServices.getAllProduct(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                ResAllProducts resAllProducts = (ResAllProducts) response.body();
                if (resAllProducts.getStatus().equals("200")) {
                    productsList.clear();
                    for (int i = 0; i < resAllProducts.getData().getData().size(); i++) {
                        productsList.add(resAllProducts.getData().getData().get(i));
                    }
                    orderItemToBillAdapter = new OrderItemToBillAdapter(mContext, productsList, BOOKINGID);
                    rcv_items.setAdapter(orderItemToBillAdapter);
                    orderItemToBillAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void Error(String error) {
                Log.e("==err", error);
            }
        });
        dialogOrder.show();
    }

    private void discount() {
        DialogDiscount dldiscount = new DialogDiscount(DetailBill.this, BOOKINGID);
        dldiscount.setCanceledOnTouchOutside(false);
        dldiscount.show();
    }
    public void f5() {
        loadBill(BOOKINGID);
    }

    public void orderItem(String paramOrder) {
        Log.e("==", "call orderItem");
        OrderServices.addSPtoBill(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                Log.e("==", "onSuccess");
                ResNullData resOrder = (ResNullData) response.body();
                if (resOrder.getStatus().equals("200")) {
                    //reload bill
                    Log.e("==200", "sc");
                    Toast.makeText(mContext, "Order thành công", Toast.LENGTH_SHORT).show();
                    loadBill(BOOKINGID);
                }
            }

            @Override
            public void Error(String error) {
                Toast.makeText(mContext, "Order thất bại", Toast.LENGTH_SHORT).show();
                Log.e("==err", error);
            }
        },paramOrder);
    }
}