package com.example.karama.model.room;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataListOrder {
    @SerializedName("data")
    @Expose
    private List<DataOrder> data = null;
    @SerializedName("currentPage")
    @Expose
    private String currentPage;
    @SerializedName("totalPages")
    @Expose
    private String totalPages;

    @Override
    public String toString() {
        return "DataListOrder{" +
                "data=" + data +
                ", currentPage='" + currentPage + '\'' +
                ", totalPages='" + totalPages + '\'' +
                '}';
    }

    public List<DataOrder> getData() {
        return data;
    }

    public void setData(List<DataOrder> data) {
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
