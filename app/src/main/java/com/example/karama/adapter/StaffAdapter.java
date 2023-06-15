package com.example.karama.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karama.R;
import com.example.karama.model.person.Staff;
import com.example.karama.views.MainMenu;

import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {
    Context mContext;
    List<Staff> staffList;
    LayoutInflater layoutInflater;

    public StaffAdapter(Context mContext, List<Staff> staffList) {
        this.mContext = mContext;
        this.staffList = staffList;
        this.layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_staff, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Staff staff= staffList.get(position);
        holder.name_staff.setText(staff.getUsername());
        holder.sdt_staff.setText(staff.getPhoneNumber());
        holder.tv_role.setText(staff.getRoleCodeName());
        if (staff.getRoleCodeName().equals("MANAGER")) {
            holder.extend.setImageDrawable(mContext.getDrawable(R.drawable.manager_64));
        }
        if (staff.getRoleCodeName().equals("STAFF")) {
            holder.extend.setImageDrawable(mContext.getDrawable(R.drawable.employee_64));
        }
        if (staff.getRoleCodeName().equals("ACCOUNTANT")) {
            holder.extend.setImageDrawable(mContext.getDrawable(R.drawable.ketoan_64));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainMenu.getInstance() != null) {
                    MainMenu.getInstance().dialogStaff(staff);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_staff, sdt_staff,tv_role;
        ImageView extend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_staff = itemView.findViewById(R.id.name_staff);
            sdt_staff = itemView.findViewById(R.id.sdt_staff);
            tv_role = itemView.findViewById(R.id.tv_role);

            extend = itemView.findViewById(R.id.extend);
        }


    }
}
