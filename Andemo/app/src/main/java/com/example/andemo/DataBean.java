package com.example.andemo;

import androidx.databinding.ObservableField;

public class DataBean {
    public final ObservableField<String> data;

    public DataBean(ObservableField<String> data) {
        this.data = data;
    }

    public ObservableField<String> getData() {
        return data;
    }

}