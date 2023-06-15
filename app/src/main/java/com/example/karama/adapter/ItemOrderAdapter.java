package com.example.karama.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karama.R;
import com.example.karama.model.order.ProductInBill;

import java.util.List;

public class ItemOrderAdapter extends RecyclerView.Adapter<ItemOrderAdapter.ViewHolder> {
    Context mContext;
    List<ProductInBill> billList;
    LayoutInflater layoutInflater;

    public ItemOrderAdapter(Context mContext, List<ProductInBill> billList) {
        this.mContext = mContext;
        this.billList = billList;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_order,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductInBill item= billList.get(position);

        try {
            holder.item_price.setText(item.getPrice().split("\\.")[0]+" VND"); //item.getPrice()
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.item_name.setText(item.getName());
        holder.sl.setText("x"+item.getQuantity());//item.getQuantity()

    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sl,item_name, item_price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sl = itemView.findViewById(R.id.sl);
            item_name = itemView.findViewById(R.id.item_name);
            item_price = itemView.findViewById(R.id.item_price);
        }
    }
}
