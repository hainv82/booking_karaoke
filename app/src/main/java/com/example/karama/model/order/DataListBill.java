package com.example.karama.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataListBill {
    @SerializedName("data")
    @Expose
    private List<DataBill> data = null;
    @SerializedName("currentPage")
    @Expose
    private String currentPage;
    @SerializedName("totalPages")
    @Expose
    private String totalPages;

    public List<DataBill> getData() {
        return data;
    }

    public void setData(List<DataBill> data) {
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
