package com.example.karama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karama.adapter.RowBillAdapter;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.UIHelper;
import com.example.karama.model.order.DataBill;
import com.example.karama.model.order.ResBill;
import com.example.karama.model.order.ResLitBill;
import com.example.karama.services.OrderServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Response;

public class BillActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView view_exit;
    TextView date_sort, customer_sort;
    private DatePickerDialog datePickerDialog;
    String datebook = "";
    RecyclerView rcv_bill;
    RowBillAdapter rowBillAdapter;
    List<DataBill> resBillList;
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        getSupportActionBar().hide();

        initView();
        initDatePicker();
        initClick();
        Log.e("==date", getCurrentTimeStamp());
        try {
            loadBillByDate(getCurrentTimeStamp());
        } catch (Exception e) {
            Log.e("==excep:", e.getMessage());
        }
    }



    private void initClick() {
        view_exit.setOnClickListener(this);
        date_sort.setOnClickListener(this);
         customer_sort.setOnClickListener(this);
    }
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    private void initView() {
        rcv_bill = findViewById(R.id.rcv_bill);
        rcv_bill.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        resBillList = new ArrayList<>();
        view_exit = findViewById(R.id.view_exit);
        date_sort = findViewById(R.id.date_sort);
        customer_sort = findViewById(R.id.customer_sort);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_exit:
                onBackPressed();
                break;
            case R.id.date_sort:
                datePickerDialog.show();
                break;
            case R.id.customer_sort:
                Intent intentDetail = new Intent(mContext, DetailBill.class);
                intentDetail.putExtra("BOOKINGID", "55");
                mContext.startActivity(intentDetail);
                break;
        }
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                Log.e("==initDatePicker", year + "-" + month + "-" + day);
                datebook = year + "-" + month + "-" + day;
//                popTimePicker();
                //call api get list
                loadBillByDate(datebook);

            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private void loadBillByDate(String datebook) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("date", datebook);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OrderServices.getBillByDate(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {

                ResLitBill resBill = (ResLitBill) response.body();
                if (resBill != null) {
                    if (resBill.getStatus().equals("200")) {
                        resBillList.clear();
                        if (resBill.getData().getData().size() > 0) {
                            Log.e("==sizeLBill:", String.valueOf(resBill.getData().getData().size()));
                            for (int i = 0; i < resBill.getData().getData().size(); i++) {
                                DataBill bill = resBill.getData().getData().get(i);
                                resBillList.add(bill);
                            }
                        } else {
                            Toast.makeText(mContext, datebook + " không có bill", Toast.LENGTH_SHORT).show();
                        }
                        rowBillAdapter = new RowBillAdapter(mContext, resBillList);
                        rcv_bill.setAdapter(rowBillAdapter);
                        rowBillAdapter.notifyDataSetChanged();

                    } else if (resBill.getStatus().equals("403")){
                        Log.e("==403", resBill.getMessage());
                        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(BillActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else{
                        UIHelper.showAlertDialog(getApplicationContext(),resBill.getStatus(),resBill.getMessage(),R.drawable.troll_64);
                        Toast.makeText(getApplicationContext(), resBill.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("==err", resBill.getStatus() + "-" + resBill.getMessage());
                    }
                }
            }

            @Override
            public void Error(String error) {

            }
        }, jsonObject.toString());

    }
}