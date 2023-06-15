package com.example.karama.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.karama.Booking;
import com.example.karama.BookingRoom;
import com.example.karama.R;
import com.example.karama.model.room.DataRoom;

import java.util.List;

public class GridRoomAdapter extends RecyclerView.Adapter<GridRoomAdapter.ViewHolder> {
    Context mContext;
    List<DataRoom> dataRoomList;
    LayoutInflater layoutInflater;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public GridRoomAdapter(Context mContext, List<DataRoom> dataRoomList) {
        this.mContext = mContext;
        this.dataRoomList = dataRoomList;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataRoom room= dataRoomList.get(position);
        viewBinderHelper.bind(holder.swipe_layout,room.getId());
        viewBinderHelper.setOpenOnlyOne(true);
        holder.room_name.setText(room.getName());
        holder.room_id.setText(room.getId());
        holder.room_stt_booking.setText(room.getRoomBookedStatus());
        if (room.getRoomBookedStatus().equals("EMPTY")) {
            holder.room_stt_booking.setTextColor(Color.BLUE);
        }
        if (room.getRoomBookedStatus().equals("BOOKED")) {
            holder.room_stt_booking.setTextColor(Color.RED);
        }
        if (room.getRoomBookedStatus().equals("RESERVED")) {
            holder.room_stt_booking.setTextColor(Color.GREEN);
        }
        holder.room_stt_type.setText(room.getStatusCode());

        if (room.getStatusCode().equals("ENABLE")) {
            //set backgoround black
            holder.room_stt_type.setTextColor(Color.parseColor("#058712"));
        } else {
            holder.main_layout.setBackground(mContext.getDrawable(R.drawable.square_button_block));
            holder.room_stt_type.setTextColor(Color.parseColor("#fa0505"));
        }


        if (room.getType().equals("VIP")) {
            holder.tag_type_view.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.vip_30,0,0);
            holder.tag_type_view.setText("VIP");
        } else {
            holder.tag_type_view.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.poor2_30,0,0);
            holder.tag_type_view.setText("Normal");
        }
        if (room.getRoomBookedStatus().equals("EMPTY")) {
            holder.tv_update.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.booking_45, 0, 0);
            holder.tv_update.setText("BOOK");
        } else {
            holder.tv_update.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.touch_45, 0, 0);
            holder.tv_update.setText("XXX");
        }
        if (!room.getStatusCode().equals("ENABLE")) {
            holder.main_layout.setBackground(mContext.getDrawable(R.drawable.square_button_red));
        }
        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "RoomID:"+room.getId(), Toast.LENGTH_SHORT).show();
                //call dialog Detail in main acti
                if (BookingRoom.getInstance() != null) {
                    BookingRoom.getInstance().callDialogDetailRoom(room);
                }
            }
        });
        holder.tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "BookID:"+room.getId(), Toast.LENGTH_SHORT).show();
//                if (BookingRoom.getInstance() != null) {
//                    BookingRoom.getInstance().openDatePicker();
//                }
                Intent intent = new Intent(mContext, Booking.class);
                intent.putExtra("idRoom", room.getId());
                mContext.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return dataRoomList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_update;
        TextView tv_update,room_name,room_id,room_stt_booking,room_stt_type;
        TextView tag_type_view;
        RelativeLayout main_layout;
        SwipeRevealLayout swipe_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_update = itemView.findViewById(R.id.layout_update);
            tv_update = itemView.findViewById(R.id.tv_update);
            room_name = itemView.findViewById(R.id.room_name);
            room_id = itemView.findViewById(R.id.room_id);
            room_stt_booking = itemView.findViewById(R.id.room_stt_booking);
            room_stt_type = itemView.findViewById(R.id.room_stt_type);
            tag_type_view = itemView.findViewById(R.id.tag_type_view);
            main_layout = itemView.findViewById(R.id.main_layout);
            swipe_layout = itemView.findViewById(R.id.swipe_layout);
        }
    }
}
