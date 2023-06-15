package com.example.karama.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataBill {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("bookingId")
    @Expose
    private String bookingId;
    @SerializedName("guestPhoneNumber")
    @Expose
    private String guestPhoneNumber;
    @SerializedName("numberHoursBooked")
    @Expose
    private String numberHoursBooked;
    @SerializedName("discountPercent")
    @Expose
    private String discountPercent;
    @SerializedName("discountMoney")
    @Expose
    private String discountMoney;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("products")
    @Expose
    private List<ProductInBill> products = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getNumberHoursBooked() {
        return numberHoursBooked;
    }

    public void setNumberHoursBooked(String numberHoursBooked) {
        this.numberHoursBooked = numberHoursBooked;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(String discountMoney) {
        this.discountMoney = discountMoney;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ProductInBill> getProducts() {
        return products;
    }

    public void setProducts(List<ProductInBill> products) {
        this.products = products;
    }

    public String getGuestPhoneNumber() {
        return guestPhoneNumber;
    }

    public void setGuestPhoneNumber(String guestPhoneNumber) {
        this.guestPhoneNumber = guestPhoneNumber;
    }
}
