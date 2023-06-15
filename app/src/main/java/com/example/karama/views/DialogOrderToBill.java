package com.example.karama.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karama.R;
import com.example.karama.adapter.OrderItemToBillAdapter;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.model.product.Products;
import com.example.karama.model.product.ResAllProducts;
import com.example.karama.services.KaraServices;
import com.example.karama.services.ProdServices;

import java.util.List;

import retrofit2.Response;

public class DialogOrderToBill extends Dialog {
    Activity activity;
    Context mContext;
    EditText edt_search;
    RecyclerView rcv_items;
    List<Products> productsList;
    OrderItemToBillAdapter adapter;
    String BOOKINGID;
    
    public DialogOrderToBill(@NonNull Activity context,Context context2,String BOOKINGID) {
        super(context);
        this.activity = context;
        this.BOOKINGID = BOOKINGID;
        this.mContext = context2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        loadItem();
    }

    private void loadItem() {
        ProdServices.getAllProduct(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                ResAllProducts resAllProducts = (ResAllProducts) response.body();
                if (resAllProducts.getStatus().equals("200")) {
                    productsList.clear();
                    for (int i = 0; i < resAllProducts.getData().getData().size(); i++) {
                        productsList.add(resAllProducts.getData().getData().get(i));
                    }
                    adapter = new OrderItemToBillAdapter(activity, productsList, BOOKINGID);
                    rcv_items.setAdapter(adapter);
                }
            }

            @Override
            public void Error(String error) {
                Log.e("==err", error);

            }
        });
    }

    private void initView() {
        edt_search = findViewById(R.id.edt_search);
        rcv_items = findViewById(R.id.rcv_items);
        rcv_items.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }
}
