package com.example.karama.services;

import com.example.karama.model.ResReport;
import com.example.karama.model.order.ResLitBill;
import com.example.karama.model.person.Res3ListStaff;
import com.example.karama.model.person.Res4AddStaff;
import com.example.karama.model.person.ResAllCustomer;
import com.example.karama.model.person.ResCustomer;
import com.example.karama.model.ResNullData;
import com.example.karama.model.product.ResAllProducts;
import com.example.karama.model.person.ResProfile;
import com.example.karama.model.auth.ResToken;
import com.example.karama.model.auth.ResTokenRefresh;
import com.example.karama.model.order.ResBill;
import com.example.karama.model.product.ResProduct;
import com.example.karama.model.room.Res13ListOrder;
import com.example.karama.model.room.Res9Reserve;
import com.example.karama.model.room.ResAddRoom;
import com.example.karama.model.room.ResCheckRoomBooked;
import com.example.karama.model.room.ResCheckin;
import com.example.karama.model.room.ResListRoom;
import com.example.karama.model.room.ResListRoomEmpty;
import com.example.karama.model.room.ResOrder;
import com.example.karama.model.room.ResRoom;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface KaraInterface {
    //===pack Auth===
    //api1  1.Lấy Refresh token và Access token
    @POST("auth")
    @FormUrlEncoded
    Call<ResToken> get2Token(@Field("username") String username,
                            @Field("password") String password);

    //api2  2.Lấy Access Token từ Fresh Token
    @GET("auth/refresh")
    Call<ResTokenRefresh> getAccessToken(@HeaderMap Map<String, String> headers);


    //====== PACK STAFF ===
    //api3.Lấy danh sách nhân viên
    @GET("staffs")
    Call<Res3ListStaff> getListStaff(@HeaderMap Map<String, String> headers,
                                     @QueryMap HashMap<String, String> body);

    //api 4.Thêm nhân viên
    @POST("staffs")
    Call<Res4AddStaff> addStaff(@HeaderMap Map<String, String> headers,
                                @Body RequestBody body);

    //api 5.Cập nhật thông tin nhân viên
    @POST("staffs/update/{username}")
    Call<Res4AddStaff> updateStaff(@Path(value = "username", encoded = true) String username,
                                   @HeaderMap Map<String, String> headers,
                                   @Body RequestBody body);

    //api 6.Xem thông tin nhân viên theo username
    @GET("staffs/{username}")
    Call<ResProfile> getDataUser(@Path(value = "username", encoded = true) String username,@HeaderMap Map<String, String> headers);
    //api 7.Đổi mật khẩu dành của người dùng hiện tại
    @POST("staffs/password")
    Call<ResNullData> changePassUser(@HeaderMap Map<String, String> headers,
                                 @Body RequestBody body);
    //api 8.Thay đổi mật khẩu của nhân viên bất kì dành cho quản lý
    @POST("staffs/password/{usernamestaff}")
    Call<ResNullData> changPassStaff(@Path(value = "usernamestaff", encoded = true) String usernamestaff,
                                     @HeaderMap Map<String, String> headers,
                                     @Body RequestBody body);
    //pack ====BOKING ROOOM =====
    //api 9.Đặt phòng trước
    @POST("room-booking/reserve")
    Call<Res9Reserve> booking(@HeaderMap Map<String, String> headers,
                              @Body RequestBody body);

    //api 10.Check-in phòng đã đặt trước
    @GET("room-booking/reserve/check-in/{bookingid}")
    Call<ResCheckin> checkin(@Path(value = "bookingid", encoded = true) String bookingid,
                             @HeaderMap Map<String, String> headers);
    //api 11.Đặt phòng trực tiếp
    @POST("room-booking/booking")
    Call<Res9Reserve> booklive(@HeaderMap Map<String, String> headers,
                               @Body RequestBody body);

    //api 12.Huỷ đặt phòng trước
    @GET("room-booking/reserve/cancel/{bookingID}")
    Call<ResNullData> cancelBooking(@Path(value = "bookingID", encoded = true) String bookingID,
                                    @HeaderMap Map<String, String> headers);

    //api 13.Xem danh sách phiếu đặt phòng theo mã phòng và trạng thái
    @GET("room-booking/room/{roomId}?page=0&size=50&status=ALL")
    Call<Res13ListOrder> getListOrder(@Path(value = "roomId", encoded = true) String roomId,
                                      @HeaderMap Map<String, String> headers);

    //api 14.Xem phiếu đặt phòng theo mã phiếu đặt phòng
    @GET("room-booking/booking/{bookingID}")
    Call<ResOrder> getOrderByBookingID(@Path(value = "bookingID", encoded = true) String bookingID,
                                       @HeaderMap Map<String, String> headers);
    //api 15.Xem danh sách phòng trống trong khoảng thời gian
    @POST("rooms-manager/empty-room?page=0&size=1000")
    Call<ResListRoomEmpty> getListRoomEmpty(@HeaderMap Map<String, String> headers,
                                            @Body RequestBody body);

    //api 16.Xem danh sách phiếu đặt phòng theo số điện thoại khách hàng ==res13
    @GET("room-booking/phone-number/{numberPhone}?page=0&size=1000&sort=ASC")
    Call<Res13ListOrder> getListOrderBySDT(@HeaderMap Map<String, String> headers,
                                           @Path(value = "numberPhone", encoded = true) String numberPhone);
    //api 17.Xem danh sách phiếu đặt phòng theo mã khách hàng-guestID
    @GET("room-booking/guest-id/{guestId}?page=0&size=100&sort=ASC")
    Call<Res13ListOrder> getListOrderByGuestID(@HeaderMap Map<String, String> headers,
                                               @Path(value = "guestId", encoded = true) String guestId);
    //========PACK ROOM MANAGER============
    //api 18.Thêm phòng mới
    @POST("rooms-manager/add")
    Call<ResAddRoom> addRoom(@HeaderMap Map<String, String> headers,
                             @Body RequestBody body);
    //api 19.Xem thông tin phòng theo mã phòng
    @GET("rooms-manager/{roomId}")
    Call<ResRoom> getRoomByRoomID(@HeaderMap Map<String, String> headers,
                                  @Path(value = "roomId", encoded = true) String roomId);

    //api 20. Lấy danh sách tất cả các phòng
    @GET("rooms-manager?page=0&size=150&sort=DES")
    Call<ResListRoom> getAllRoom(@HeaderMap Map<String, String> headers);

    //api 21.Vô hiệu hoá phòng theo mã phòng
    @GET("rooms-manager/disable/{roomId}")
    Call<ResNullData> lockRoom(@HeaderMap Map<String, String> headers,
                               @Path(value = "roomId", encoded = true) String roomId);
    //api 22.Sửa thông tin phòng
    @POST("rooms-manager/update/{roomId}")
    Call<ResNullData> updateRoom(@Path(value = "roomId", encoded = true) String roomId,
                                 @HeaderMap Map<String, String> headers,
                                 @Body RequestBody body);

    //api 23.Danh sách phòng khả dụng
    @GET("rooms-manager/enable?page=0&size=100&sort=ASC")
    Call<ResListRoom> getListRoomEmpty(@HeaderMap Map<String, String> headers);

    //api  24.Kích hoạt lại phòng đã bị vô hiệu hoá
    @GET("rooms-manager/enable/{roomId}")
    Call<ResNullData> unlockRoom(@HeaderMap Map<String, String> headers,
                                 @Path(value = "roomId", encoded = true) String roomId);

    //api 25.Xem danh sách các phòng đang được sử dụng
    @GET("rooms-manager/booked?page=0&size=50")
    Call<ResListRoom> getListRoomUsed(@HeaderMap Map<String, String> headers);
    //api 26.Kiểm tra xem phòng có đang được sử dụng
    @GET("rooms-manager/is-booked/{roomId}")
    Call<ResCheckRoomBooked> checkRoomBooked(@HeaderMap Map<String, String> headers,
                                             @Path(value = "roomId", encoded = true) String roomId);

    //===========PACK ORDER===========
    //api 27.Thêm sản phẩm vào hoá đơn của phiếu đặt phòng
    @POST("orders/product")
    Call<ResNullData> addSPtoBill(@HeaderMap Map<String, String> headers,
                                  @Body RequestBody body);
    //api 28.Thêm/sửa giảm giá cho hóa đơn theo mã hoá đơn
    @POST("orders/discount/order-id/{orderId}")
    Call<ResNullData> discountBillByRoomID(@Path(value = "roomId", encoded = true) String roomId,
                                   @HeaderMap Map<String, String> headers,
                                   @Body RequestBody body);

    //api 29.Thêm/sửa giảm giá cho hóa đơn theo mã phiếu đặt phòng
    @POST("orders/discount/booking-id/{bookingId}")
    Call<ResNullData> discountBillByBookingID(@Path(value = "bookingId", encoded = true) String bookingId,
                                              @HeaderMap Map<String, String> headers,
                                              @Body RequestBody body);

    //api 30.Xem hoá đơn theo mã phiếu đặt phòng
    @GET("orders/booking/{bookingId}")
    Call<ResBill> getBillByBookingID(@Path(value = "bookingId", encoded = true) String bookingId,
                                     @HeaderMap Map<String, String> headers);
    //api 31.Xem hóa đơn theo id
    @GET("orders/{id}")
    Call<ResBill> getBillByID(@Path(value = "id", encoded = true) String id,
                              @HeaderMap Map<String, String> headers);
    //api 32.Xem hoa don theo ngay
    /*@GET("orders/day?page=0&size=50&sort=ASC")
    * */
    @POST("orders/day?page=0&size=50&sort=ASC")
    Call<ResLitBill> getBillByDate(@HeaderMap Map<String, String> headers,
                                   @Body RequestBody body);
    //api 33.Thanh toan hoa don
    @GET("orders/pay/{orderId}")
    Call<ResBill> payment(@Path(value = "orderId", encoded = true) String orderId,
                          @HeaderMap Map<String, String> headers);
    //api 34.xuat hoa don pdf
    @GET("orders/pdf/{orderId}")
    Call<ResNullData> resPdfBill(@Path(value = "orderId", encoded = true) String orderId,
                                 @HeaderMap Map<String, String> headers);
    //api 35 Huy hoa don
    @GET("orders/cancel/{orderId}")
    Call<ResNullData> detroyBill(@Path(value = "orderId", encoded = true) String orderId,
                                 @HeaderMap Map<String, String> headers);




    //==========PACK CUSTOMER==========

    //api 36.Thêm khách hàng mới
    @POST("guests-manager/add")
    Call<ResCustomer> addCustome(@HeaderMap Map<String, String> headers,
                                 @Body RequestBody body);

    //api 37.Sửa thông tin khách hàng theo số điện thoại
    @POST("guests-manager/update/{phone}")
    Call<ResNullData> updateCustomer(@Path(value = "phone", encoded = true) String phone,
                                     @HeaderMap Map<String, String> headers,
                                     @Body RequestBody body);

    //api 38.Vô hiệu hoá khách hàng theo số điện thoại
    @GET("guests-manager/disable/{phone}")
    Call<ResNullData> lockCustomer(@Path(value = "phone", encoded = true) String phone,
                                   @HeaderMap Map<String, String> headers);

    //api 39.Kích hoạt lại khách hàng theo số điện thoại
    @GET("guests-manager/enable/{phone}")
    Call<ResNullData> unlockCustomer(@Path(value = "phone", encoded = true) String phone,
                                     @HeaderMap Map<String, String> headers);

    //api 40.Xem danh sách khách hàng
    @GET("guests-manager")
    Call<ResAllCustomer> allCustomer(@HeaderMap Map<String, String> headers,
                                     @QueryMap HashMap<String, String> body);

    //============PACK PRODUCT MANAGER==========
    //api 47.Xem danh sách sản phẩm CHECK TOKEN
    @GET("products-manager")
    Call<ResAllProducts> checkTokenSeeProduct(@HeaderMap Map<String, String> headers,
                                              @QueryMap HashMap<String, String> body);
    //47
    @GET("products-manager/?page=0&size=300&sort=ASC")
    Call<ResAllProducts> getAllProduct(@HeaderMap Map<String, String> headers);


    //api 43 . Them san pham moi
    @POST("products-manager/add")
    Call<ResProduct> addProduct(@HeaderMap Map<String, String> headers,
                                @Body RequestBody body);
    //api 44.Sửa thông tin sản phẩm theo mã sản phẩm
    @POST("products-manager/update/{productId}")
    Call<ResNullData> updateProductById(@Path(value = "productId", encoded = true) String productId,
                                        @HeaderMap Map<String, String> headers,
                                        @Body RequestBody body);

    //api 45.Vô hiệu hoá sản phẩm theo mã sản phẩm
    @GET("products-manager/disable/{productId}")
    Call<ResNullData> lockProducrt(@Path(value = "productId", encoded = true) String productId,
                                   @HeaderMap Map<String, String> headers);
    //api 46.Kích hoạt lại sản phẩm đã bị vô hiệu hoá bằng mã sản phẩm
    @GET("products-manager/enable/{productId}")
    Call<ResNullData> unlockProduct(@Path(value = "productId", encoded = true) String productId,
                                    @HeaderMap Map<String, String> headers);
    //api 48.Xem sản phẩm theo mã sản phẩm
    @GET("products-manager/{productId}")
    Call<ResProduct> getProductByID(@Path(value = "productId", encoded = true) String productId,
                                    @HeaderMap Map<String, String> headers);
    //api 49.Xem doanh thu theo khoảng thời gian
    @GET("reports")
    Call<ResReport> getReport(@HeaderMap Map<String, String> headers,
                              @Body RequestBody body);


















}
