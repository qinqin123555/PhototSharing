package com.example.phototsharing.net;

import com.example.phototsharing.entity.PersonBean;
import com.example.phototsharing.entity.ResponseBody;
import com.example.phototsharing.entity.ShareBean;
import com.example.phototsharing.entity.ShareDetailBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    // 登录接口
    @POST("api/member/photo/user/login")
    Call<PersonBean> login(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Body LoginRequest request
    );

    // 注册接口
    @POST("api/member/photo/user/register")
    Call<ResponseBody> registerUser(@Body RegisterRequest request);

    // 获取分享信息接口
    @GET("api/member/photo/share")
    Call<ShareBean> getFindInfo(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("userId") long userId
    );

    // 获取分享详情信息接口
    @GET("api/member/photo/share/detail")
    Call<ShareDetailBean> getFindDetailInfo(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("shareId") long shareId,
            @Query("userId") long userId
    );

    // 登录请求类
    public class LoginRequest {
        private String username;
        private String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    // 注册请求类
    public class RegisterRequest {
        private String username;
        private String password;

        public RegisterRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
