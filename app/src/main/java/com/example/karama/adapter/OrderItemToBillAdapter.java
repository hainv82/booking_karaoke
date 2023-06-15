package com.example.karama.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.karama.DetailBill;
import com.example.karama.R;
import com.example.karama.model.order.ProductInBill;
import com.example.karama.model.product.Products;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OrderItemToBillAdapter extends RecyclerView.Adapter<OrderItemToBillAdapter.ViewHolder> {
    Context mContext;
    List<Products> itemList;
    LayoutInflater layoutInflater;
    String BOOKINGID;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public OrderItemToBillAdapter(Context mContext, List<Products> itemList,String BOOKINGID) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.BOOKINGID = BOOKINGID;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.row_order,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products item= itemList.get(position);
        viewBinderHelper.bind(holder.swipe_layout,item.getId());
        viewBinderHelper.setOpenOnlyOne(true);
        holder.item_name.setText(item.getName());
        holder.item_id.setText(item.getId());
        holder.item_price.setText(item.getPrice());
        holder.tv_order_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.edt_sl.getText().toString().equals("")) {
                    Toast.makeText(mContext, "Vui lòng nhập số lượng", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("bookingId", Integer.parseInt(BOOKINGID));
                        jsonObject.put("productId", Integer.parseInt(item.getId()));
                        jsonObject.put("quantity", Integer.parseInt(holder.edt_sl.getText().toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //call api order item in detailbill
                    Log.e("==pramOrder:", jsonObject.toString());
                    DetailBill.getInstance().orderItem(jsonObject.toString());

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText edt_sl;
        TextView tv_order_item,item_name,item_price, item_id;
        SwipeRevealLayout swipe_layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edt_sl = itemView.findViewById(R.id.edt_sl);
            tv_order_item = itemView.findViewById(R.id.tv_order_item);
            item_name = itemView.findViewById(R.id.item_name);
            item_price = itemView.findViewById(R.id.item_price);
            item_id = itemView.findViewById(R.id.item_id);
            swipe_layout = itemView.findViewById(R.id.swipe_layout);
        }
    }
}
