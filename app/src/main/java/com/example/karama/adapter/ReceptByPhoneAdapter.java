package com.example.karama.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karama.Booking;
import com.example.karama.MainActivity;
import com.example.karama.R;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.IInterfaceModel;
import com.example.karama.helper.UIHelper;
import com.example.karama.model.ResNullData;
import com.example.karama.model.room.DataOrder;
import com.example.karama.model.room.ResCheckin;
import com.example.karama.services.RumServices;
import com.example.karama.views.DialogReceptByPhone;
import com.example.karama.views.DialogReceptByRoom;

import java.util.List;

import retrofit2.Response;

public class ReceptByPhoneAdapter extends RecyclerView.Adapter<ReceptByPhoneAdapter.ViewHolder> {
    Context mContext;
    List<DataOrder> orderList;
    LayoutInflater layoutInflater;
    String from;

    public ReceptByPhoneAdapter(Context mContext, List<DataOrder> orderList,String from) {
        this.mContext = mContext;
        this.orderList = orderList;
        this.from = from;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_one_recept, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataOrder recep= orderList.get(position);
        holder.bookingId.setText("BookingID :\n"+recep.getBookingId());
        holder.roomId.setText("RoomID :\n"+recep.getRoomId());
        holder.orderId.setText("OrderID :\n"+recep.getOrderId());
        holder.time_start.setText("Time Start :\n"+recep.getStartTime());
        holder.statusCode.setText("Status :\n"+recep.getStatusCodeName());
        if (recep.getStatusCodeName().equals("PENDING")) {
            holder.checkin.setVisibility(View.VISIBLE);
            holder.cancel_booking.setVisibility(View.VISIBLE);
        }
        if (recep.getStatusCodeName().equals("BOOKED")) {
            holder.cancel_booking.setVisibility(View.VISIBLE);
        }

        holder.checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checin
                Toast.makeText(mContext, "Checkin-BookingID:"+recep.getBookingId(), Toast.LENGTH_SHORT).show();
                RumServices.checkIn(new CallbackResponse() {
                    @Override
                    public void Success(Response<?> response) {
                        Log.e("==checkin", "onSucess");
                        ResCheckin resCheckin = (ResCheckin) response.body();
                        if (resCheckin != null) {
                            if (resCheckin.getStatus().equals("200")) {
                                Log.e("checkin", "success");
                                Toast.makeText(mContext, "Checkin thành công", Toast.LENGTH_SHORT).show();
//                                UIHelper.showAlertDialog(mContext, "Check in succes _", resCheckin.toString(),R.drawable.ic_success_35);
                                if (from.equals("SDT")) {
                                    DialogReceptByPhone.getInstance().loadAllOrderRecept();
                                }
                                if (from.equals("ROOMID")) {
                                    DialogReceptByRoom.getInstance().loadAllOrderRecept();
                                }
                            } else if (resCheckin.getStatus().equals("403")){
                                UIHelper.showAlertDialogV3(mContext, resCheckin.getStatus(), resCheckin.getMessage(), R.drawable.troll_64, new IInterfaceModel.OnBackIInterface() {
                                    @Override
                                    public void onSuccess() {
                                        Intent i = new Intent(mContext, MainActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        mContext.startActivity(i);
                                    }
                                });
                            } else{
                                Log.e("==checkin", resCheckin.getMessage());
                                UIHelper.showAlertDialog(mContext,resCheckin.getStatus(),resCheckin.getMessage(),R.drawable.troll_64);
                            }
                        }
                    }

                    @Override
                    public void Error(String error) {

                    }
                }, recep.getBookingId());

            }
        });

        holder.cancel_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RumServices.cancelBook(new CallbackResponse() {
                    @Override
                    public void Success(Response<?> response) {
                        ResNullData resCancelBooking = (ResNullData) response.body();
                        if (resCancelBooking != null) {
                            if (resCancelBooking.getStatus().equals("200")) {
                                Toast.makeText(mContext, "Hủy đặt phòng thành công", Toast.LENGTH_SHORT).show();
                                DialogReceptByPhone.getInstance().loadAllOrderRecept();
                            }
                        }
                    }

                    @Override
                    public void Error(String error) {

                    }
                }, recep.getBookingId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bookingId,roomId,orderId,time_start,statusCode, checkin,cancel_booking;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookingId = itemView.findViewById(R.id.bookingId);
            roomId = itemView.findViewById(R.id.roomId);
            orderId = itemView.findViewById(R.id.orderId);
            time_start = itemView.findViewById(R.id.time_start);
            statusCode = itemView.findViewById(R.id.statusCode);
            checkin = itemView.findViewById(R.id.checkin);
            cancel_booking = itemView.findViewById(R.id.cancel_booking);
            checkin.setVisibility(View.GONE);
            cancel_booking.setVisibility(View.GONE);


        }
    }
}
