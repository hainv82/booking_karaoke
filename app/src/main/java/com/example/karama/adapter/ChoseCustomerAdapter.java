package com.example.karama.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karama.Booking;
import com.example.karama.R;
import com.example.karama.model.person.Customer;

import java.util.List;

public class ChoseCustomerAdapter extends RecyclerView.Adapter<ChoseCustomerAdapter.ViewHolder> {
    Context context;
    List<Customer> customerList;
    LayoutInflater layoutInflater;

    public ChoseCustomerAdapter(Context context, List<Customer> customerList) {
        this.context = context;
        this.customerList = customerList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_chose_customer,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer customer= customerList.get(position);
        holder.tv_chose.setText(customer.getFirstName()+" "+customer.getLastName()+"\n"+customer.getPhoneNumber());
        if (customer.getGender().equals("MALE")) {
            holder.tv_chose.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.sing_boy35, 0, 0, 0);
        } else {
            holder.tv_chose.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.sing_girl35, 0, 0, 0);
        }
        holder.tv_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("==chose:", customer.getPhoneNumber());
                if (Booking.getInstance() != null) {
                    Booking.getInstance().setPhoneBooking(customer);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_chose;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_chose = itemView.findViewById(R.id.tv_chose_customer);
        }
    }
}
