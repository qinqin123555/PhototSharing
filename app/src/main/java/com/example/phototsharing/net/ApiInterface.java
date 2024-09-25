package com.example.phototsharing.net;

import com.example.phototsharing.entity.PersonBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("api/member/photo/user/login")
    Call<PersonBean> login(@Header("appId") String appId, @Header("appSecret") String appSecret, @Query("username") String username, @Query("password") String password);

    public class LoginRequest {
        private String username; // 用户名
        private String password; // 密码

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

}
