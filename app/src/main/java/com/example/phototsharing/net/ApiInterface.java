package com.example.phototsharing.net;

import com.example.phototsharing.entity.ShareBean;
import com.example.phototsharing.entity.ShareDetailBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("api/member/photo/share")
    Call<ShareBean> getFindInfo(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("userId") long userId
    );
    @GET("api/member/photo/share/detail")
    Call<ShareDetailBean> getFindDetailInfo(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("shareId") long shareId,
            @Query("userId") long userId
    );
}

