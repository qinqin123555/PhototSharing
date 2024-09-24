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
    Call<PersonBean> login(@Body LoginRequest loginRequest);

    public class LoginRequest {
        private String account;
        private String password;

        public LoginRequest(String account, String password) {
            this.account = account;
            this.password = password;
        }
    }

}
