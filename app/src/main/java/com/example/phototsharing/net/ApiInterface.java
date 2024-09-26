package com.example.phototsharing.net;

import com.example.phototsharing.entity.PersonBean;
import com.example.phototsharing.entity.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("api/member/photo/user/login")
    Call<PersonBean> login(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Body LoginRequest request
    );

    @POST("api/member/photo/user/register")
    Call<ResponseBody> registerUser(@Body RegisterRequest request);

    public class LoginRequest {
        private String username;
        private String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        // 添加 getter 方法
        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    public class RegisterRequest {
        private String username;
        private String password;

        public RegisterRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

}
