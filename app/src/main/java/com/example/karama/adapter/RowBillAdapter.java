package com.example.karama.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karama.DetailBill;
import com.example.karama.R;
import com.example.karama.model.order.DataBill;

import java.util.List;

public class RowBillAdapter extends RecyclerView.Adapter<RowBillAdapter.ViewHolder> {
    Context mContext;
    List<DataBill> dataBillList;
    LayoutInflater layoutInflater;

    public RowBillAdapter(Context mContext, List<DataBill> dataBillList) {
        this.mContext = mContext;
        this.dataBillList = dataBillList;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.row_bill,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataBill bill = dataBillList.get(position);
        holder.bill_id.setText("Bill ID:\n"+bill.getId());
        holder.bookingId.setText("Booking ID:\n"+bill.getBookingId());
        holder.time_using.setText("Time Used:\n"+bill.getNumberHoursBooked());
        holder.sdt_book.setText(bill.getGuestPhoneNumber());
        //go detail
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDetail = new Intent(mContext, DetailBill.class);
                intentDetail.putExtra("BOOKINGID", bill.getBookingId());
                mContext.startActivity(intentDetail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataBillList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bill_id,bookingId, time_using,sdt_book;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bill_id = itemView.findViewById(R.id.bill_id);
            bookingId = itemView.findViewById(R.id.bookingId);
            time_using = itemView.findViewById(R.id.time_using);
            sdt_book = itemView.findViewById(R.id.sdt_book);
        }
    }
}
