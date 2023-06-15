package com.example.karama.services;

import android.content.Context;
import android.util.Log;

import com.example.karama.data.SharedPrefManager;
import com.example.karama.helper.APIHelper;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.UrlConfig;
import com.example.karama.model.person.ResProfile;
import com.example.karama.model.auth.ResToken;
import com.example.karama.model.auth.ResTokenRefresh;
import com.example.karama.model.person.ResUser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIServices {
    public static void getAccessRefreshToken( CallbackResponse callbackResponse, String user, String pass) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", user);
            jsonObject.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("==prGetToken1:", jsonObject.toString());
        RequestBody requestBody = RequestBody.create(APIHelper.JSON, jsonObject.toString());
        APIClient.getClient(UrlConfig.URL).create(APIInterface.class)
                .getToken(user,pass)
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

    public static void checkExam(Context context, CallbackResponse callbackResponse, String employee) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Employcode", employee);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("==prExam:", jsonObject.toString());
        RequestBody requestBody = RequestBody.create(APIHelper.JSON, jsonObject.toString());
        APIClient.getClient(UrlConfig.URL_PHARMA).create(APIInterface.class)
                .checkExam(requestBody)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    public static void getUser(CallbackResponse callbackResponse) {
        APIClient.getClient(UrlConfig.URL_REQRES).create(APIInterface.class)
                .getUser()
                .enqueue(new Callback<ResUser>() {
                    @Override
                    public void onResponse(Call<ResUser> call, Response<ResUser> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResUser> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }
    public static void getAccessToken(CallbackResponse callbackResponse,String token) {
        APIClient.getClient(UrlConfig.URL).create(APIInterface.class)
                .getRefreshToken(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
                .enqueue(new Callback<ResTokenRefresh>() {
                    @Override
                    public void onResponse(Call<ResTokenRefresh> call, Response<ResTokenRefresh> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResTokenRefresh> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }
//if token het han, show dialog v3 -> login
    public static void seeProfile(CallbackResponse callbackResponse) {
        APIClient.getClient(UrlConfig.URL).create(APIInterface.class)
                .getProfile(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()))
                .enqueue(new Callback<ResProfile>() {
                    @Override
                    public void onResponse(Call<ResProfile> call, Response<ResProfile> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResProfile> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    public static void editProfile(CallbackResponse callbackResponse, String bodyUpdate) {
        RequestBody requestBody = RequestBody.create(APIHelper.JSON, bodyUpdate);
        APIClient.getClient(UrlConfig.URL).create(APIInterface.class)
                .updateProfile(APIHelper.API_HEADER(SharedPrefManager.getAccessToken()),requestBody)
                .enqueue(new Callback<ResProfile>() {
                    @Override
                    public void onResponse(Call<ResProfile> call, Response<ResProfile> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResProfile> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    public static void regis(CallbackResponse callbackResponse, String bodyReg) {
        RequestBody requestBody = RequestBody.create(APIHelper.JSON, bodyReg);
        APIClient.getClient(UrlConfig.URL).create(APIInterface.class)
                .regisUser(requestBody)
                .enqueue(new Callback<ResProfile>() {
                    @Override
                    public void onResponse(Call<ResProfile> call, Response<ResProfile> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        } else {
                            if (response.code() == 400) {
                                try {
                                    Gson gson = new Gson();
                                    ResProfile errorResponse = gson.fromJson(response.errorBody().charStream(), ResProfile.class);
                                    callbackResponse.Error("Code: " + response.code() + "\nMessage: " + errorResponse.getMessage());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    callbackResponse.Error("Code: " + response.code() + " Message: " + response.message());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResProfile> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    public static void confirm(CallbackResponse callbackResponse, String bodyConf) {
        RequestBody requestBody = RequestBody.create(APIHelper.JSON, bodyConf);
        APIClient.getClient(UrlConfig.URL).create(APIInterface.class)
                .confirmOTP(requestBody)
                .enqueue(new Callback<ResProfile>() {
                    @Override
                    public void onResponse(Call<ResProfile> call, Response<ResProfile> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResProfile> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }

    public static void resend(CallbackResponse callbackResponse,String username) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("username", username);
        APIClient.getClient(UrlConfig.URL).create(APIInterface.class)
                .resendOtp(hashMap)
                .enqueue(new Callback<ResProfile>() {
                    @Override
                    public void onResponse(Call<ResProfile> call, Response<ResProfile> response) {
                        if (response.isSuccessful()) {
                            callbackResponse.Success(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResProfile> call, Throwable t) {
                        callbackResponse.Error(t.getMessage());
                    }
                });
    }
}
