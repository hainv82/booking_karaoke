package com.example.karama.services;

import android.graphics.Shader;

import com.example.karama.data.SharedPrefManager;
import com.example.karama.helper.APIHelper;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.UrlConfig;
import com.example.karama.model.ResNullData;
import com.example.karama.model.ResReport;
import com.example.karama.model.product.ResAllProducts;
import com.example.karama.model.product.ResProduct;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdServices {
    public static void getAllProduct(CallbackResponse callbackResponse) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .getAllProduct(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
                .enqueue(new Callback<ResAllProducts>() {
                    @Override
                    public void onResponse(Call<ResAllProducts> call, Response<ResAllProducts> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResAllProducts> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 43 . Them san pham moi
    public static void addProduct(CallbackResponse callbackResponse, String bodyAdd) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .addProduct(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),APIHelper.parseReq(bodyAdd))
                .enqueue(new Callback<ResProduct>() {
                    @Override
                    public void onResponse(Call<ResProduct> call, Response<ResProduct> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResProduct> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 44.Sửa thông tin sản phẩm theo mã sản phẩm
    public static void updateProductById(CallbackResponse callbackResponse, String productId, String bodyUpdate) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .updateProductById(productId,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),APIHelper.parseReq(bodyUpdate))
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

    //api 45.Vô hiệu hoá sản phẩm theo mã sản phẩm
    public static void lockProducrt(CallbackResponse callbackResponse, String productId) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .lockProducrt(productId,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
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

    //api 46.Kích hoạt lại sản phẩm đã bị vô hiệu hoá bằng mã sản phẩm
    public static void unlockProduct(CallbackResponse callbackResponse, String productId) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .unlockProduct(productId,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
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

    //api 48.Xem sản phẩm theo mã sản phẩm
    public static void getProductByID(CallbackResponse callbackResponse, String productId) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .getProductByID(productId,APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
                .enqueue(new Callback<ResProduct>() {
                    @Override
                    public void onResponse(Call<ResProduct> call, Response<ResProduct> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResProduct> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    //api 49.Xem doanh thu theo khoảng thời gian
    public static void getReport(CallbackResponse callbackResponse,String body) {
        APIClient.getClient(UrlConfig.KARA).create(KaraInterface.class)
                .getReport(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),APIHelper.parseReq(body))
                .enqueue(new Callback<ResReport>() {
                    @Override
                    public void onResponse(Call<ResReport> call, Response<ResReport> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResReport> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }
}
