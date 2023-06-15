package com.example.karama.services;

import com.example.karama.model.person.ResProfile;
import com.example.karama.model.auth.ResToken;
import com.example.karama.model.auth.ResTokenRefresh;
import com.example.karama.model.person.ResUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface APIInterface {
    @GET("inside/GetInformationExam")
    Call<List<String>> getInformationExam(@HeaderMap Map<String, String> headers,
                                                   @QueryMap Map<String, String> params);

    @POST("mPharmacy/Service.svc/GetInformationExam")
    Call<String> getInforExam(@Body RequestBody body);


    @POST("mPharmacy/Service.svc/GetInformationExam")
    Call<String> checkExam(@Body RequestBody body);

    @GET("users/2")
    Call<ResUser> getUser();
//middle
    //Lấy access token và refresh token
    @POST("auth")
    @FormUrlEncoded
    Call<ResToken> getToken(@Field("username") String username,
                            @Field("password") String password);

    //Lấy acccess token từ refresh token
    @POST("auth/refresh")
    Call<ResTokenRefresh> getRefreshToken(@HeaderMap Map<String, String> headers);

    //xem hồ sơ cá nhân
    @GET("user/profile")
    Call<ResProfile> getProfile(@HeaderMap Map<String, String> headers);

    ///sửa hồ sơ cá nhân
    @POST("user/profile")
    Call<ResProfile> updateProfile(@HeaderMap Map<String, String> headers, @Body RequestBody body);

    //đăng kí
    @POST("auth/register")
    Call<ResProfile> regisUser(@Body RequestBody body);

    //xác thực
    @POST("auth/validate")
    Call<ResProfile> confirmOTP(@Body RequestBody body);

    //re send otp=ServZaloPay.getDetail
    @GET("auth/validate/resend")
    Call<ResProfile> resendOtp(@QueryMap HashMap<String, String> body);


}
