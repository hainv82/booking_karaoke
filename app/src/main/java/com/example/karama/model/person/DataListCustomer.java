package com.example.karama.model.person;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataListCustomer {
    @SerializedName("data")
    @Expose
    private List<Customer> data = null;
    @SerializedName("currentPage")
    @Expose
    private String currentPage;
    @SerializedName("totalPages")
    @Expose
    private String totalPages;

    public List<Customer> getData() {
        return data;
    }

    public void setData(List<Customer> data) {
        this.data = data;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }
}
