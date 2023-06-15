package com.example.karama.services;

import android.content.Context;
import android.content.Intent;

import com.example.karama.MainActivity;
import com.example.karama.R;
import com.example.karama.data.SharedPrefManager;
import com.example.karama.helper.APIHelper;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.IInterfaceModel;
import com.example.karama.helper.UIHelper;
import com.example.karama.helper.UrlConfig;
import com.example.karama.model.person.Res3ListStaff;
import com.example.karama.model.person.Res4AddStaff;
import com.example.karama.model.person.ResAllCustomer;
import com.example.karama.model.person.ResCustomer;
import com.example.karama.model.ResNullData;
import com.example.karama.model.product.ResAllProducts;
import com.example.karama.model.person.ResProfile;
import com.example.karama.model.auth.ResToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class KaraServices {
    //service 1.Lấy Refresh token và Access token
    public static void getAccessRefreshToken(CallbackResponse callbackResponse, String username, String password) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .get2Token(username,password)
                .enqueue(new Callback<ResToken>() {
                    @Override
                    public void onResponse(Call<ResToken> call, Response<ResToken> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResToken> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });

    }
    //service3.Lấy danh sách nhân viên
    public static void getAllStaff(CallbackResponse callbackResponse, String page, String size, String sort) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("page", page);
        hashMap.put("size", size);
        hashMap.put("sort", sort);
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .getListStaff(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),hashMap)
                .enqueue(new Callback<Res3ListStaff>() {
                    @Override
                    public void onResponse(Call<Res3ListStaff> call, Response<Res3ListStaff> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<Res3ListStaff> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }
    //service 4.Thêm nhân viên
    public static void addUser(CallbackResponse callbackResponse, String stringAdd) {
        RequestBody requestBody = RequestBody.create(APIHelper.JSON, stringAdd);
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .addStaff(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),requestBody)
                .enqueue(new Callback<Res4AddStaff>() {
                    @Override
                    public void onResponse(Call<Res4AddStaff> call, Response<Res4AddStaff> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<Res4AddStaff> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }
    //service5.Cập nhật thông tin nhân viên
    public static void updateUser(CallbackResponse callbackResponse, String reqUpdate) {
        RequestBody requestBody = RequestBody.create(APIHelper.JSON, reqUpdate);
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .updateStaff(SharedPrefManager.getUsername(),APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),requestBody)
                .enqueue(new Callback<Res4AddStaff>() {
                    @Override
                    public void onResponse(Call<Res4AddStaff> call, Response<Res4AddStaff> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<Res4AddStaff> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }
    //service 6.Xem thông tin nhân viên theo username
    public static void seeProfile(Context context,CallbackResponse callbackResponse, String username) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .getDataUser(username,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
                .enqueue(new Callback<ResProfile>() {
                    @Override
                    public void onResponse(Call<ResProfile> call, Response<ResProfile> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        } else {
                            if (response.code() == 403) {
                                UIHelper.showAlertDialogConfirm(context, "403", "Token hết hiệu lực, vui lòng đăng nhập lại", R.drawable.bad_face_35, new IInterfaceModel.OnBackIInterface() {
                                    @Override
                                    public void onSuccess() {
                                        Intent intent = new Intent(context, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        context.startActivity(intent);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResProfile> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }
    //service 8.Thay đổi mật khẩu của nhân viên bất kì dành cho quản lý
    public static void renewPass(CallbackResponse callbackResponse, String user, String newpass) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password", newpass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(APIHelper.JSON, jsonObject.toString());
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .changPassStaff(user,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),requestBody)
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

    // service 36.Thêm khách hàng mới
    public static void addCustomer(CallbackResponse callbackResponse, String stringAdd) {
        RequestBody requestBody = RequestBody.create(APIHelper.JSON,stringAdd);
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .addCustome(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),requestBody)
                .enqueue(new Callback<ResCustomer>() {
                    @Override
                    public void onResponse(Call<ResCustomer> call, Response<ResCustomer> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResCustomer> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //service 37.Sửa thông tin khách hàng theo số điện thoại
    public static void updateCustomer(CallbackResponse callbackResponse, String stringUpdate,String numberphone) {
        RequestBody requestBody = RequestBody.create(APIHelper.JSON, stringUpdate);
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .updateCustomer(numberphone,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),requestBody)
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

    //service 38.Vô hiệu hoá khách hàng theo số điện thoại
    public static void lockCustomer(CallbackResponse callbackResponse, String numberphone) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .lockCustomer(numberphone,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
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

    //service 39.Kích hoạt lại khách hàng theo số điện thoại
    public static void unlockCustomer(CallbackResponse callbackResponse, String numberphone) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .unlockCustomer(numberphone,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
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
    //service 40.Xem danh sách khách hàng
    public static void getAllCustomer(CallbackResponse callbackResponse, String page, String size, String sort) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("page", page);
        hashMap.put("size", size);
        hashMap.put("sort", sort);
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .allCustomer(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),hashMap)
                .enqueue(new Callback<ResAllCustomer>() {
                    @Override
                    public void onResponse(Call<ResAllCustomer> call, Response<ResAllCustomer> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResAllCustomer> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });

    }

    //service 47.Xem danh sách sản phẩm CHECK TOKEN
    public static void checkToken(Context context, CallbackResponse callbackResponse, String page, String size, String sort) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("page", page);
        hashMap.put("size", size);
        hashMap.put("sort", sort);
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .checkTokenSeeProduct(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),hashMap)
                .enqueue(new Callback<ResAllProducts>() {
                    @Override
                    public void onResponse(Call<ResAllProducts> call, Response<ResAllProducts> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        } else {
                            if (response.code() == 403) {
                                UIHelper.showAlertDialogConfirm(context, "403", "Token hết hiệu lực, vui lòng đăng nhập lại", R.drawable.bad_face_35, new IInterfaceModel.OnBackIInterface() {
                                    @Override
                                    public void onSuccess() {
                                        Intent intent = new Intent(context, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        context.startActivity(intent);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResAllProducts> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }
}
