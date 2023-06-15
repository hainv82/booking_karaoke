package com.example.karama.services;

import com.example.karama.data.SharedPrefManager;
import com.example.karama.helper.APIHelper;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.UrlConfig;
import com.example.karama.model.ResNullData;
import com.example.karama.model.room.Res13ListOrder;
import com.example.karama.model.room.Res9Reserve;
import com.example.karama.model.room.ResAddRoom;
import com.example.karama.model.room.ResCheckRoomBooked;
import com.example.karama.model.room.ResCheckin;
import com.example.karama.model.room.ResListRoom;
import com.example.karama.model.room.ResListRoomEmpty;
import com.example.karama.model.room.ResOrder;
import com.example.karama.model.room.ResRoom;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RumServices {
    //9.Đặt phòng trước
    public static void bookingRoom(CallbackResponse callbackResponse, String strBook) {
        RequestBody requestBody = RequestBody.create(APIHelper.JSON, strBook);
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .booking(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()), requestBody)
                .enqueue(new Callback<Res9Reserve>() {
                    @Override
                    public void onResponse(Call<Res9Reserve> call, Response<Res9Reserve> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<Res9Reserve> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 10.Check-in phòng đã đặt trước
    public static void checkIn(CallbackResponse callbackResponse, String bookID) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .checkin(bookID,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
                .enqueue(new Callback<ResCheckin>() {
                    @Override
                    public void onResponse(Call<ResCheckin> call, Response<ResCheckin> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResCheckin> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 11.Đặt phòng trực tiếp
    public static void bookLive(CallbackResponse callbackResponse, String strBook) {
        RequestBody requestBody = RequestBody.create(APIHelper.JSON, strBook);
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .booklive(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),requestBody)
                .enqueue(new Callback<Res9Reserve>() {
                    @Override
                    public void onResponse(Call<Res9Reserve> call, Response<Res9Reserve> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<Res9Reserve> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 12.Huỷ đặt phòng trước
    public static void cancelBook(CallbackResponse callbackResponse, String id) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .cancelBooking(id,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
                .enqueue(new Callback<ResNullData>() {
                    @Override
                    public void onResponse(Call<ResNullData> call, Response<ResNullData> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResNullData> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 13.Xem danh sách phiếu đặt phòng theo mã phòng và trạng thái
    public static void getListOrder(CallbackResponse callbackResponse, String id) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .getListOrder(id,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
                .enqueue(new Callback<Res13ListOrder>() {
                    @Override
                    public void onResponse(Call<Res13ListOrder> call, Response<Res13ListOrder> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<Res13ListOrder> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 14.Xem phiếu đặt phòng theo mã phiếu đặt phòng
    public static void getOrderByBookingID(CallbackResponse callbackResponse, String id) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .getOrderByBookingID(id,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
                .enqueue(new Callback<ResOrder>() {
                    @Override
                    public void onResponse(Call<ResOrder> call, Response<ResOrder> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResOrder> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 15 Xem danh sách phòng trống trong khoảng thời gian
    public static void getListRoomEmpty(CallbackResponse callbackResponse, String stringTime) {
        RequestBody requestBody = RequestBody.create(APIHelper.JSON, stringTime);
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .getListRoomEmpty(APIClient.API_HEADER(SharedPrefManager.getAccessToken()),requestBody)
                .enqueue(new Callback<ResListRoomEmpty>() {
                    @Override
                    public void onResponse(Call<ResListRoomEmpty> call, Response<ResListRoomEmpty> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResListRoomEmpty> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 16.Xem danh sách phiếu đặt phòng theo số điện thoại khách hàng ==res13
    public static void getListOrderBySDT(CallbackResponse callbackResponse, String phone) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .getListOrderBySDT(APIClient.API_HEADER(SharedPrefManager.getAccessToken()),phone)
                .enqueue(new Callback<Res13ListOrder>() {
                    @Override
                    public void onResponse(Call<Res13ListOrder> call, Response<Res13ListOrder> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<Res13ListOrder> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 17.Xem danh sách phiếu đặt phòng theo mã khách hàng-guestID
    public static void getListOrderByGuestID(CallbackResponse callbackResponse, String id) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .getListOrderByGuestID(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),id)
                .enqueue(new Callback<Res13ListOrder>() {
                    @Override
                    public void onResponse(Call<Res13ListOrder> call, Response<Res13ListOrder> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<Res13ListOrder> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 18.Thêm phòng mới
    public static void addRoom(CallbackResponse callbackResponse, String strAdd) {
        RequestBody requestBody = RequestBody.create(APIHelper.JSON, strAdd);
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .addRoom(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),requestBody)
                .enqueue(new Callback<ResAddRoom>() {
                    @Override
                    public void onResponse(Call<ResAddRoom> call, Response<ResAddRoom> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResAddRoom> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 19.Xem thông tin phòng theo mã phòng
    public static void getRoomByRoomID(CallbackResponse callbackResponse, String roomId) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .getRoomByRoomID(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),roomId)
                .enqueue(new Callback<ResRoom>() {
                    @Override
                    public void onResponse(Call<ResRoom> call, Response<ResRoom> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResRoom> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }
    //api 20. Lấy danh sách tất cả các phòng
    public static void getAllRoom(CallbackResponse callbackResponse) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .getAllRoom(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
                .enqueue(new Callback<ResListRoom>() {
                    @Override
                    public void onResponse(Call<ResListRoom> call, Response<ResListRoom> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResListRoom> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 21.Vô hiệu hoá phòng theo mã phòng
    public static void lockRoom(CallbackResponse callbackResponse, String roomID) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .lockRoom(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),roomID)
                .enqueue(new Callback<ResNullData>() {
                    @Override
                    public void onResponse(Call<ResNullData> call, Response<ResNullData> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResNullData> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 22.Sửa thông tin phòng
    public static void updateRoom(CallbackResponse callbackResponse, String roomId, String strUpdate) {
        RequestBody requestBody = RequestBody.create(APIHelper.JSON, strUpdate);
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .updateRoom(roomId,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),requestBody)
                .enqueue(new Callback<ResNullData>() {
                    @Override
                    public void onResponse(Call<ResNullData> call, Response<ResNullData> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResNullData> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 23.Danh sách phòng khả dụng
    public static void getListRoomEmpty(CallbackResponse callbackResponse) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .getListRoomEmpty(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
                .enqueue(new Callback<ResListRoom>() {
                    @Override
                    public void onResponse(Call<ResListRoom> call, Response<ResListRoom> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResListRoom> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api  24.Kích hoạt lại phòng đã bị vô hiệu hoá
    public static void unlockRoom(CallbackResponse callbackResponse, String roomId) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .unlockRoom(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),roomId)
                .enqueue(new Callback<ResNullData>() {
                    @Override
                    public void onResponse(Call<ResNullData> call, Response<ResNullData> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResNullData> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 25.Xem danh sách các phòng đang được sử dụng
    public static void getListRoomUsed(CallbackResponse callbackResponse) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .getListRoomUsed(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
                .enqueue(new Callback<ResListRoom>() {
                    @Override
                    public void onResponse(Call<ResListRoom> call, Response<ResListRoom> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResListRoom> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 26.Kiểm tra xem phòng có đang được sử dụng
    public static void checkRoomBooked(CallbackResponse callbackResponse, String roomId) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .checkRoomBooked(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()), roomId)
                .enqueue(new Callback<ResCheckRoomBooked>() {
                    @Override
                    public void onResponse(Call<ResCheckRoomBooked> call, Response<ResCheckRoomBooked> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResCheckRoomBooked> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }















}
