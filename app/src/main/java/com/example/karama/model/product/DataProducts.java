package com.example.karama.model.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataProducts {
    @SerializedName("data")
    @Expose
    private List<Products> data = null;
    @SerializedName("currentPage")
    @Expose
    private String currentPage;
    @SerializedName("totalPages")
    @Expose
    private String totalPages;

    public List<Products> getData() {
        return data;
    }

    public void setData(List<Products> data) {
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
