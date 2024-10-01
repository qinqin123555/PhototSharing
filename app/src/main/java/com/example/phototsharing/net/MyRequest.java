package com.example.phototsharing.net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRequest {
    public static ApiInterface request() {
        Retrofit myRetrofit = new Retrofit.Builder()
                .baseUrl("https://api-store.openguet.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return myRetrofit.create(ApiInterface.class);
    }
}
