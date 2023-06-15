package com.example.karama.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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
import com.example.karama.R;
import com.example.karama.model.product.Products;
import com.example.karama.views.DialogAddItem;
import com.example.karama.views.MainMenu;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    Context mContext;
    List<Products> productsList;
    LayoutInflater layoutInflater;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public ItemAdapter(Context mContext, List<Products> productsList) {
        this.mContext = mContext;
        this.productsList = productsList;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products products = productsList.get(position);
        viewBinderHelper.bind(holder.swipe_layout,products.getId());
        viewBinderHelper.setOpenOnlyOne(true);
        holder.layout_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call dialog update in Main
                if (MainMenu.getInstance() != null) {
                    MainMenu.getInstance().dialogUpdateItem(products);
                }
            }
        });
        holder.item_id.setText(products.getId());
        holder.item_name.setText(products.getName());
        holder.item_price.setText(products.getPrice());
        if (products.getStatus().equals("1")) {
            holder.item_lock.setVisibility(View.VISIBLE);
            holder.item_unlock.setVisibility(View.GONE);
//            holder.item_name.setTextColor(Color.parseColor("#0707a8"));
            holder.itemView.setBackground(mContext.getDrawable(R.drawable.square_button_white));
            holder.main_layout.setBackground(mContext.getDrawable(R.drawable.square_button_green));
            holder.tv_update.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.update_45,0,0);

        } else {
            holder.item_unlock.setVisibility(View.VISIBLE);
            holder.item_lock.setVisibility(View.GONE);
//            holder.item_name.setTextColor(Color.parseColor("#bf1806"));
            holder.itemView.setBackground(mContext.getDrawable(R.drawable.square_button_white2));
            holder.main_layout.setBackground(mContext.getDrawable(R.drawable.square_button_red));
            holder.tv_update.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.update2_45,0,0);
        }
        holder.item_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call api lock item
                if (MainMenu.getInstance() != null) {
                    MainMenu.getInstance().lockItem(products.getId());
                }
            }
        });
        holder.item_unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call api unlock
                if (MainMenu.getInstance() != null) {
                    MainMenu.getInstance().unLockItem(products.getId());
                }
            }
        });
        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call detail Item
                Toast.makeText(mContext, products.getDescription(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_id,item_name,item_price,item_unlock, item_lock,tv_update;
        SwipeRevealLayout swipe_layout;
        LinearLayout layout_update;
        RelativeLayout main_layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_id = itemView.findViewById(R.id.item_id);
            item_name = itemView.findViewById(R.id.item_name);
            item_price = itemView.findViewById(R.id.item_price);
            item_unlock = itemView.findViewById(R.id.item_unlock);
            swipe_layout = itemView.findViewById(R.id.swipe_layout);
            layout_update = itemView.findViewById(R.id.layout_update);
            main_layout = itemView.findViewById(R.id.main_layout);
            tv_update = itemView.findViewById(R.id.tv_update);
            item_unlock.setVisibility(View.GONE);
            item_lock = itemView.findViewById(R.id.item_lock);
            item_lock.setVisibility(View.GONE);
        }
    }
}
