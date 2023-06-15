package com.example.karama.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.karama.MemberActivity;
import com.example.karama.R;
import com.example.karama.model.person.Customer;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    Context mContext;
    List<Customer> customerList;
    LayoutInflater layoutInflater;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public CustomerAdapter(Context mContext, List<Customer> customerList) {
        this.mContext = mContext;
        this.customerList = customerList;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_list_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        viewBinderHelper.bind(holder.swipeRevealLayout, customer.getId());
        viewBinderHelper.setOpenOnlyOne(true);

        holder.layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dialog detail customer
                if (MemberActivity.getInstance() != null) {
                    MemberActivity.getInstance().showDetailCustomer(customer);
                }
            }
        });

        holder.tv_id.setText(customer.getId());
        if (customer.getGender().equals("MALE")) {
            holder.tv_id.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.sing_boy35, 0, 0);
        } else {
            holder.tv_id.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.sing_girl35, 0, 0);
        }
        //set lock user
        if (!customer.getStatus().equals("1")) {
            holder.tv_id.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.crimiral_35, 0, 0);
            holder.tv_id.setText("Wanted");
        } else {
            holder.layout_recept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, customer.getPhoneNumber(), Toast.LENGTH_SHORT).show();
                    MemberActivity.getInstance().getDialogRecept(customer.getPhoneNumber());
                }
            });
        }
        holder.text_name.setText(customer.getFirstName()+" "+customer.getLastName()+"\n"+customer.getPhoneNumber());

    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SwipeRevealLayout swipeRevealLayout;
        LinearLayout layout_recept;
        TextView tv_id, text_name;
        RelativeLayout layout_main;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_recept = itemView.findViewById(R.id.layout_recept);
            tv_id = itemView.findViewById(R.id.tv_id);
            text_name = itemView.findViewById(R.id.text_name);
            swipeRevealLayout = itemView.findViewById(R.id.swipe_layout);
            layout_main = itemView.findViewById(R.id.layout_main);


        }
    }
}
