package com.example.phototsharing.net;

import com.example.phototsharing.entity.AddCommentBean;
import com.example.phototsharing.entity.AddCollectBean;
import com.example.phototsharing.entity.CommentBean;
import com.example.phototsharing.entity.AddFocusBean;
import com.example.phototsharing.entity.AddLikeBean;
import com.example.phototsharing.entity.HasFocusBean;
import com.example.phototsharing.entity.PersonBean;
import com.example.phototsharing.entity.RegisterBean;
import com.example.phototsharing.entity.ShareBean;
import com.example.phototsharing.entity.ShareDetailBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    // 登录接口
    @FormUrlEncoded
    @POST("api/member/photo/user/login")
    Call<PersonBean> loginUser(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Field("username") String username,
            @Field("password") String password
    );

    // 注册接口
    @POST("api/member/photo/user/register")
    Call<RegisterBean> registerUser(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Body String requestBody // 这里使用 RequestBody 接收 JSON
    );


    // 获取分享信息接口
    @GET("api/member/photo/share")
    Call<ShareBean> getFindInfo(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("current") long current,
            @Query("size") long size,
            @Query("userId") long userId
    );

    // 获取分享详情信息接口
    @GET("api/member/photo/share/detail")
    Call<ShareDetailBean> getFindDetailInfo(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("shareId") long shareId,
            @Query("userId") Long userId
    );

    //获取一级评论接口
    @GET("api/member/photo/comment/first")
    Call<CommentBean> getFirstComment(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("current") long current,
            @Query("shareId") long shareId,
            @Query("size") long size
    );

    //新增一级评论接口
    @POST("api/member/photo/comment/first")
    Call<AddCommentBean> setFirstComment(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("content") String content,
            @Query("shareId") long shareId,
            @Query("userId") long myUserId,
            @Query("userName") String myUserName
    );



    //获取二级评论
    @GET("api/member/photo/comment/second")
    Call<CommentBean> getSecondComment(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("commentId") long commentId,
            @Query("current") long current,
            @Query("shareId") long shareId,
            @Query("size") long size
    );


    //新增二级评论
    @POST("api/member/photo/comment/second")
    Call<AddCommentBean> setSecondComment(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("content") String content,
            @Query("parentCommentId") long parentCommentId,
            @Query("parentCommentUserId") long parentCommentUserId,
            @Query("replyCommentId") long replyCommentId,
            @Query("replyCommentUserId") long replyCommentUserId,
            @Query("shareId") long shareId,
            @Query("userId") long userId,
            @Query("userName") String userName
    );

//    根据用户名获取用户信息
    @GET("api/member/photo/user/getUserByName")
    Call<PersonBean> getUserByName(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("username") String userName
    );


//    获取当前登录用户已关注的图文列表
    @GET("api/member/photo/focus")
    Call<HasFocusBean> getFocusInfo(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("current") long current,
            @Query("size") long size,
            @Query("userId") long userId
    );


//    添加关注
    @POST("api/member/photo/focus")
    Call<AddFocusBean> addFocus(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("focusUserId") long focusUserId,
            @Query("userId") long userId
    );
//        取消关注
    @POST("api/member/photo/focus/cancel")
    Call<AddFocusBean> cancelFocus(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("focusUserId") long focusUserId,
            @Query("userId") long userId
    );
    //    获取已经点赞的列表
    @GET("api/member/photo/collect")
    Call<HasFocusBean> getCollectInfo(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("current") long current,
            @Query("size") long size,
            @Query("userId") long userId
    );

//    添加收藏
    @POST("api/member/photo/collect")
    Call<AddCollectBean> setCollectInfo(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("shareId") long shareId,
            @Query("userId") long userId
    );

//    取消收藏
    @POST("api/member/photo/collect/cancel")
    Call<AddCollectBean> setCollectCancelInfo(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("collectId") long collectId
    );

//    获取已经点赞的列表
    @GET("api/member/photo/like")
    Call<HasFocusBean> getLikeInfo(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("current") long current,
            @Query("size") long size,
            @Query("userId") long userId
    );

    //    添加点赞
    @POST("api/member/photo/like")
    Call<AddLikeBean> setLikeInfo(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("shareId") long shareId,
            @Query("userId") long userId
    );

//    取消点赞

    @POST("api/member/photo/like/cancel")
    Call<AddLikeBean> setLikeCancelInfo(
            @Header("appId") String appId,
            @Header("appSecret") String appSecret,
            @Query("likeId") long likeId
    );

}
