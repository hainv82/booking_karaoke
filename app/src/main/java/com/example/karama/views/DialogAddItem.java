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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.karama.MainActivity;
import com.example.karama.R;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.IInterfaceModel;
import com.example.karama.helper.UHelper;
import com.example.karama.helper.UIHelper;
import com.example.karama.model.ResNullData;
import com.example.karama.model.product.Products;
import com.example.karama.model.product.ResProduct;
import com.example.karama.services.ProdServices;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;

public class DialogAddItem extends Dialog implements View.OnClickListener {
    Activity activity;
    ImageView view_close;
    EditText item_name,item_price, item_descrip;
    TextView add_item,update_item;
    RadioButton rdbtn_have, rdbtn_outstock;
    String typeUse;
    Products items;
    public DialogAddItem(@NonNull Activity context,String typeUse) {
        super(context);
        this.activity = context;
        this.typeUse = typeUse;
    }

    public DialogAddItem(@NonNull Activity context, String typeUse, Products items) {
        super(context);
        this.activity = context;
        this.typeUse = typeUse;
        this.items = items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_item);
        initView();
        view_close.setOnClickListener(this);
        add_item.setOnClickListener(this);
        update_item.setOnClickListener(this);

    }

    private void initView() {
        view_close = findViewById(R.id.view_close);
        item_name = findViewById(R.id.item_name);
        item_price = findViewById(R.id.item_price);
        item_descrip = findViewById(R.id.item_descrip);
        add_item = findViewById(R.id.add_item);
        update_item = findViewById(R.id.update_item);
        rdbtn_have = findViewById(R.id.rdbtn_have);
        rdbtn_outstock = findViewById(R.id.rdbtn_outstock);
        if (typeUse.equals("ADD")) {
            add_item.setVisibility(View.VISIBLE);
            update_item.setVisibility(View.GONE);
        } else {
            add_item.setVisibility(View.GONE);
            update_item.setVisibility(View.VISIBLE);
        }
        if (typeUse.equals("UPDATE")) {
            item_name.setText(items.getName());
            item_price.setText(items.getPrice());
            item_descrip.setText(items.getDescription());
            if (items.getStatus().equals("1")) {
                rdbtn_have.setChecked(true);
            } else {
                rdbtn_outstock.setChecked(true);
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_close:
                dismiss();
                break;
            case R.id.add_item:
                checkNull();
                break;
            case R.id.update_item:
                checkNull();
                break;
        }
    }

    private void checkNull() {
        String name = UHelper.onTextStr(item_name);
        String price = UHelper.onTextStr(item_price);
        String des = UHelper.onTextStr(item_descrip);
        if (UHelper.onValidate(name)) {
            Toast.makeText(activity, "Vui lòng nhập tên sản phẩm", Toast.LENGTH_SHORT).show();
            item_name.requestFocus();
        } else if (UHelper.onValidate(price)) {
            Toast.makeText(activity, "Vui lòng nhập giá sản phẩm", Toast.LENGTH_SHORT).show();
            item_price.requestFocus();
        } else if (UHelper.onValidate(des)) {
            Toast.makeText(activity, "Vui lòng nhập mô tả sản phẩm", Toast.LENGTH_SHORT).show();
            item_descrip.requestFocus();
        } else if (!rdbtn_have.isChecked() && !rdbtn_outstock.isChecked()) {
            Toast.makeText(activity, "Vui lòng chọn trạng thái sản phẩm", Toast.LENGTH_SHORT).show();
        } else {
            if (typeUse.equals("ADD")) {
                addItem();
            } else {
                updateItem(items.getId());
            }

        }
    }

    private void updateItem(String itemID) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", item_name.getText().toString());
            jsonObject.put("price", Integer.valueOf(item_price.getText().toString()));
            jsonObject.put("description", item_descrip.getText().toString());
            if (rdbtn_have.isChecked()) {
                jsonObject.put("status", 1);
            }
            if (rdbtn_outstock.isChecked()) {
                jsonObject.put("status", 0);
            }
            Log.e("==pramUpdate:", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ProdServices.updateProductById(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                ResNullData resUpdate = (ResNullData) response.body();
                if (resUpdate != null) {
                    if (resUpdate.getStatus().equals("200")) {
                        dismiss();
                        if (MainMenu.getInstance() != null) {
                            MainMenu.getInstance().loadListKItem();
                        }
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

            }
        },itemID, jsonObject.toString());

    }

    private void addItem() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", item_name.getText().toString());
            jsonObject.put("price", Integer.valueOf(item_price.getText().toString()));
            jsonObject.put("description", item_descrip.getText().toString());
            if (rdbtn_have.isChecked()) {
                jsonObject.put("status", 1);
            }
            if (rdbtn_outstock.isChecked()) {
                jsonObject.put("status", 0);
            }
            Log.e("==pramAdd:", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
            //call api add sp & reload
        ProdServices.addProduct(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                ResProduct resAdd = (ResProduct) response.body();
                if (resAdd != null) {
                    if (resAdd.getStatus().equals("200")) {
                        dismiss();
                        if (MainMenu.getInstance() != null) {
                            MainMenu.getInstance().loadListKItem();
                        }
                    } else if (resAdd.getStatus().equals("403")){
                        UIHelper.showAlertDialogV3(activity, resAdd.getStatus(), resAdd.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                            @Override
                            public void onSuccess() {
                                Intent i = new Intent(activity, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                activity.startActivity(i);
                            }
                        });
                    } else{
                        UIHelper.showAlertDialog(activity,resAdd.getStatus(),resAdd.getMessage(),R.drawable.troll_64);
                    }
                }

            }

            @Override
            public void Error(String error) {

            }
        }, jsonObject.toString());
    }
}
