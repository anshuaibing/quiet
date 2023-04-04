package com.example.andemo.ui.mvvmDemo;

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