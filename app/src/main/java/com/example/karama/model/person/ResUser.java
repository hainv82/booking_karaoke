package com.example.karama.model.person;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResUser {
    @SerializedName("data")
    @Expose
    private DataUser data;
    @SerializedName("support")
    @Expose
    private Support support;

    public DataUser getData() {
        return data;
    }

    public void setData(DataUser data) {
        this.data = data;
    }

    public Support getSupport() {
        return support;
    }

    public void setSupport(Support support) {
        this.support = support;
    }
}
