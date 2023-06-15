package com.example.karama.services;

import com.example.karama.data.SharedPrefManager;
import com.example.karama.helper.APIHelper;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.UrlConfig;
import com.example.karama.model.ResNullData;
import com.example.karama.model.order.ResBill;
import com.example.karama.model.order.ResLitBill;

import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderServices {
    //api 27.Thêm sản phẩm vào hoá đơn của phiếu đặt phòng
    public static void addSPtoBill(CallbackResponse callbackResponse, String strAdd) {
        RequestBody requestBody = RequestBody.create(APIHelper.JSON, strAdd);
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .addSPtoBill(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),requestBody)
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

    //api 28.Thêm/sửa giảm giá cho hóa đơn theo mã hoá đơn
    public static void discountBillByRoomID(CallbackResponse callbackResponse, String roomId, String strDiscont) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .discountBillByRoomID(roomId, APIHelper.API_HEADER(SharedPrefManager.getAccessToken()), APIHelper.parseReq(strDiscont))
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

    //api 29.Thêm/sửa giảm giá cho hóa đơn theo mã phiếu đặt phòng
    public static void discountBillByBookingID(CallbackResponse callbackResponse, String bookId, String bodyDiscount) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .discountBillByBookingID(bookId,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),APIHelper.parseReq(bodyDiscount))
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

    //api 30.Xem hoá đơn theo mã phiếu đặt phòng
    public static void getBillByBookingID(CallbackResponse callbackResponse, String bookingId) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .getBillByBookingID(bookingId,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
                .enqueue(new Callback<ResBill>() {
                    @Override
                    public void onResponse(Call<ResBill> call, Response<ResBill> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResBill> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 31.Xem hóa đơn theo id
    public static void getBillByID(CallbackResponse callbackResponse, String id) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .getBillByID(id,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
                .enqueue(new Callback<ResBill>() {
                    @Override
                    public void onResponse(Call<ResBill> call, Response<ResBill> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResBill> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 32.Xem hoa don theo ngay
    public static void getBillByDate(CallbackResponse callbackResponse, String bodyGetBill) {
        RequestBody requestBody = RequestBody.create(APIHelper.JSON, bodyGetBill);

        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .getBillByDate(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),requestBody)
//                .getBillByDate(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),APIHelper.parseReq(bodyGetBill))
                .enqueue(new Callback<ResLitBill>() {
                    @Override
                    public void onResponse(Call<ResLitBill> call, Response<ResLitBill> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResLitBill> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 33.Thanh toan hoa don
    public static void payment(CallbackResponse callbackResponse, String orderId) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .payment(orderId,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
                .enqueue(new Callback<ResBill>() {
                    @Override
                    public void onResponse(Call<ResBill> call, Response<ResBill> response) {
                        if (response.isSuccessful()) {
                            if (response.isSuccessful()) {
                                callbackResponse.Success(response);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<ResBill> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 34.xuat hoa don pdf
    public static void resPdfBill(CallbackResponse callbackResponse, String orderId) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .resPdfBill(orderId,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
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

    //api 35 Huy hoa don
    public static void detroyBill(CallbackResponse callbackResponse, String orderId) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .detroyBill(orderId,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
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
}
