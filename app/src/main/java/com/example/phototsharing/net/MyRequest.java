package com.example.phototsharing.net;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

    public static void login(String username, String password, LoginCallback callback) {
        ApiInterface myApi = MyRequest.request();

        Call<PersonBean> call = myApi.loginUser(
                MyHeaders.getAppId(),
                MyHeaders.getAppSecret(),
                username,
                password
        );

        // 打印 appId 和 appSecret
        String appId = MyHeaders.getAppId();
        String appSecret = MyHeaders.getAppSecret();

        Log.d("MyRequest", "appId: " + appId);
        Log.d("MyRequest", "appSecret: " + appSecret);

//        Log.d("MyRequest", "LoginRequestBody: " + requestBody.toString());
        Log.d("MyRequest", "Request Headers: " + MyHeaders.getHeaders().toString());
        call.enqueue(new Callback<PersonBean>() {
            @Override
            public void onResponse(@NonNull Call<PersonBean> call, @NonNull Response<PersonBean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 检查返回的代码
                    if (response.body().getCode() == 200) {
                        callback.onSuccess(response.body()); // 登录成功，返回 PersonBean
                    } else {
                        callback.onFailure(new Throwable("登录失败: " + response.body().getMsg()));
                    }
                } else {
                    callback.onFailure(new Throwable("HTTP错误: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PersonBean> call, @NonNull Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public static void register(String username, String password, RegisterCallback callback) {
        ApiInterface myApi = MyRequest.request();

        // 准备请求参数
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailure(new Throwable("请求参数构造失败"));
            return; // 退出方法以防止继续执行
        }

        // 创建 RequestBody
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonObject.toString() // 使用 jsonObject 而不是 requestBody
        );

        // 打印请求地址
        Log.d("MyRequest", "请求地址: https://api-store.openguet.cn/api/member/photo/user/register");

        // 打印请求参数
        Log.d("MyRequest", "请求参数: " + jsonObject.toString());

        // 打印请求头
        Log.d("MyRequest", "Request Header: ");
        Log.d("MyRequest", "Accept: application/json, text/plain, */*");
        Log.d("MyRequest", "Content-Type: application/json");
        Log.d("MyRequest", "appId: " + MyHeaders.getAppId());
        Log.d("MyRequest", "appSecret: " + MyHeaders.getAppSecret());

        // 调用接口时只需将请求体传递给 Retrofit
        Call<RegisterBean> call = myApi.registerUser(
                MyHeaders.getAppId(),
                MyHeaders.getAppSecret(),
                requestBody.toString() // 将创建的 requestBody 直接传递
        );

        // 执行异步请求
        call.enqueue(new Callback<RegisterBean>() {
            @Override
            public void onResponse(@NonNull Call<RegisterBean> call, @NonNull Response<RegisterBean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == 200) {
                        callback.onSuccess(response.body());
                    } else {
                        callback.onFailure(new Throwable("注册失败: " + response.body().getMsg()));
                    }
                } else {
                    callback.onFailure(new Throwable("HTTP错误: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterBean> call, @NonNull Throwable t) {
                callback.onFailure(t);
            }
        });
    }



    public static  void getShareBeanData(long userId,GetShareBeanCallback callback) {
        ApiInterface myApiInterface = MyRequest.request();
        Call<ShareBean> call = myApiInterface.getFindInfo(MyHeaders.getAppId(),MyHeaders.getAppSecret(),1,30,userId);
        call.enqueue(new Callback<ShareBean>() {
            @Override
            public void onResponse(@NonNull Call<ShareBean> call, @NonNull Response<ShareBean> response) {
                if (response.isSuccessful()) {
                    ShareBean shareBean = response.body();
                    if (shareBean != null) {
                        callback.onSuccess(shareBean);
                    } else {
                        // 可以选择在这里处理空响应体的情况，或者通过回调通知调用者
                        callback.onFailure(new NullPointerException("Response body is null"));
                    }
                } else {
                    // 可以从response.errorBody()中获取更多错误信息，但这里为了简单起见只传递了Throwable
                    callback.onFailure(new Exception("HTTP error: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ShareBean> call, @NonNull Throwable t) {

            }
        });

    }

    //获取分享详情
    public static void getShareDetailData(long shareId, long userId, ShareDetailCallback callback) {
        ApiInterface myApiInterface = MyRequest.request(); // 假设request()方法返回你的ApiInterface实例
        Call<ShareDetailBean> call = myApiInterface.getFindDetailInfo(MyHeaders.getAppId(), MyHeaders.getAppSecret(), shareId, userId);
        call.enqueue(new Callback<ShareDetailBean>() {
            @Override
            public void onResponse(@NonNull Call<ShareDetailBean> call, @NonNull Response<ShareDetailBean> response) {
                if (response.isSuccessful()) {
                    ShareDetailBean shareDetailBean = response.body();
                    if (shareDetailBean != null) {
                        Log.d("msg",shareDetailBean.getMsg());
                        if (shareDetailBean.getData()!= null){
                            callback.onSuccess(shareDetailBean);
                        }
                    } else {
                        // 可以选择在这里处理空响应体的情况，或者通过回调通知调用者
                        callback.onFailure(new NullPointerException("Response body is null"));
                    }
                } else {
                    // 可以从response.errorBody()中获取更多错误信息，但这里为了简单起见只传递了Throwable
                    callback.onFailure(new Exception("HTTP error: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ShareDetailBean> call, @NonNull Throwable t) {
                callback.onFailure(t);
            }
        });
    }


    //    获取一级评论
    // 注意：这里假设loadSecondCommentData已经被修改为接受一个回调接口
    public static void getFirstCommentData(long shareId, CommentCallback callback) {
        ApiInterface myApi = MyRequest.request();
        Call<CommentBean> call = myApi.getFirstComment(MyHeaders.getAppId(), MyHeaders.getAppSecret(), 1, shareId, 30);
        call.enqueue(new Callback<CommentBean>() {
            @Override
            public void onResponse(@NonNull Call<CommentBean> call, @NonNull Response<CommentBean> response) {
                if (response.isSuccessful()) {
                    CommentBean firstCommentBean = response.body();
                    if (firstCommentBean != null && firstCommentBean.getData() != null) {
                        // 这里我们需要异步加载每个评论的回复评论
                        callback.onSuccess(firstCommentBean);

                    } else {
                        // 没有数据或数据格式错误
                        callback.onFailure(new Exception("No data or data format error"));
                    }
                } else {
                    // 请求失败
                    callback.onFailure(new Exception("HTTP error: " + response.code()));
                }
            }
            @Override
            public void onFailure(@NonNull Call<CommentBean> call, @NonNull Throwable t) {
                // 网络请求本身失败
                callback.onFailure(t);
            }
        });
    }



    //    获取二级评论
    public static void getSecondCommentData(long shareId, long firstCommentId, CommentCallback callback) {
        ApiInterface myApi = MyRequest.request(); // 假设request()是你的Retrofit API接口的实例创建方法
        Call<CommentBean> call = myApi.getSecondComment(MyHeaders.getAppId(), MyHeaders.getAppSecret(), firstCommentId, 1, shareId, 30);
        call.enqueue(new Callback<CommentBean>() {
            @Override
            public void onResponse(@NonNull Call<CommentBean> call, @NonNull Response<CommentBean> response) {
                if (response.isSuccessful()) {
                    CommentBean commentBean = response.body();
                    if (commentBean != null && commentBean.getData() != null) {
                        callback.onSuccess(commentBean);
                    } else {
                        // 可以在这里处理body为null的情况，例如通过callback返回错误信息
                        callback.onFailure(new Throwable("Received null response body"));
                    }
                } else {
                    // 可以在这里添加更详细的错误处理
                    callback.onFailure(new Throwable("HTTP error: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommentBean> call, @NonNull Throwable t) {
                callback.onFailure(t);
            }
        });
    }


//    通过用户名字获取用户信息
    public static void getUserByName(String username, GetUserInfoCallback callback){
        ApiInterface myApi = MyRequest.request();
        Call<PersonBean> call = myApi.getUserByName(MyHeaders.getAppId(),MyHeaders.getAppSecret(),username);
        call.enqueue(new Callback<PersonBean>() {
            @Override
            public void onResponse(@NonNull Call<PersonBean> call, @NonNull Response<PersonBean> response) {
                if (response.isSuccessful()) {
                    PersonBean personBean = response.body();
                    if (personBean != null && personBean.getData() != null) {
                        callback.onSuccess(personBean);
                    }
                } else {
                    callback.onFailure(new Throwable("Received null response body"));

                }
            }

            @Override
            public void onFailure(@NonNull Call<PersonBean> call, @NonNull Throwable t) {
                callback.onFailure(new Throwable(t));
            }
        });
    }

//    新增一条一级评论
public static void addFirstComment(Context context,String content, long shareId, long userId, String userName) {
    ApiInterface myApi = MyRequest.request();
    Call<AddCommentBean> call = myApi.setFirstComment(MyHeaders.getAppId(), MyHeaders.getAppSecret(), content, shareId, userId, userName);
    call.enqueue(new Callback<AddCommentBean>() {
        @Override
        public void onResponse(@NonNull Call<AddCommentBean> call, @NonNull Response<AddCommentBean> response) {
            if (response.isSuccessful() && response.body() != null) {
                if (response.body().getCode() == 200) {
                    Log.d("TAG","发送了一条评论");
                    Toast.makeText(context,"评论成功",Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG","code不等于200");
                    Log.d("Msg",response.body().getMsg());
                    Log.d("Code",String.valueOf(response.body().getCode()));

                    Toast.makeText(context,"评论失败",Toast.LENGTH_SHORT).show();

                }
            } else {
                Log.d("TAG","response失败");
                Toast.makeText(context,"评论失败",Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onFailure(@NonNull Call<AddCommentBean> call, @NonNull Throwable t) {
            Log.d("onFailure", String.valueOf(t));
            Toast.makeText(context,"评论失败",Toast.LENGTH_SHORT).show();
        }
    });
}


    //    新增一条二级评论
    public static void addSecondComment (String content,long parentCommentId,long parentCommentUserId,long replyCommentId,long replyCommentUserId,long shareId,long userId,String userName) {
        ApiInterface myApi = MyRequest.request();
        Call<AddCommentBean> call = myApi.setSecondComment(MyHeaders.getAppId(),MyHeaders.getAppSecret(),
                        content,parentCommentId,parentCommentUserId, replyCommentId,
                replyCommentUserId,shareId,userId,userName);
        call.enqueue(new Callback<AddCommentBean>() {
            @Override
            public void onResponse(@NonNull Call<AddCommentBean> call, @NonNull Response<AddCommentBean> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getCode() == 200) {

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddCommentBean> call, @NonNull Throwable t) {

            }
        });
    }

//   获取当前登录用户的已关注列表
    public static void getFocusInfo(long current,long size,long userId, GetFocusBeanCallback callback) {
        ApiInterface myApi = MyRequest.request();
        Call<HasFocusBean> call = myApi.getFocusInfo(MyHeaders.getAppId(),MyHeaders.getAppSecret(),current,size,userId);
        call.enqueue(new Callback<HasFocusBean>() {
            @Override
            public void onResponse(@NonNull Call<HasFocusBean> call, @NonNull Response<HasFocusBean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Throwable("response is null"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<HasFocusBean> call, @NonNull Throwable t) {
                callback.onFailure(new Throwable("获取已关注用户的图文列表失败"));
            }
        });

    }

    //    添加关注的网络请求
    public static void setFocusRequest(long focusUserId, long userId, TrueOrFalseCallback callback) {
        ApiInterface myApi = MyRequest.request();
        Call<AddFocusBean> call = myApi.addFocus(MyHeaders.getAppId(),MyHeaders.getAppSecret(),focusUserId,userId);
        call.enqueue(new Callback<AddFocusBean>() {
            @Override
            public void onResponse(@NonNull Call<AddFocusBean> call, @NonNull Response<AddFocusBean> response) {
                if (response.isSuccessful()) {
                    if (response.body()!= null && response.body().getCode() == 200) {
//
                        callback.onSuccess(true);
                    } else {
//                        Toast.makeText(context, "无法关注", Toast.LENGTH_SHORT).show();
                        callback.onFailure(new Throwable("无法关注"));
                    }
                } else {
//                    Toast.makeText(context, "关注失败", Toast.LENGTH_SHORT).show();
                    callback.onFailure(new Throwable("无法关注"));

                }
            }

            @Override
            public void onFailure(@NonNull Call<AddFocusBean> call, @NonNull Throwable t) {
//                Toast.makeText(context, "关注失败", Toast.LENGTH_SHORT).show();
                callback.onFailure(new Throwable("关注失败"));

            }
        });
    }

    //    取消关注的网络请求
    public static void chancelFocusRequest(long focusUserId, long userId, TrueOrFalseCallback callback) {
        ApiInterface myApi = MyRequest.request();
        Call<AddFocusBean> call = myApi.cancelFocus(MyHeaders.getAppId(),MyHeaders.getAppSecret(),focusUserId,userId);
        call.enqueue(new Callback<AddFocusBean>() {
            @Override
            public void onResponse(@NonNull Call<AddFocusBean> call, @NonNull Response<AddFocusBean> response) {
                if (response.isSuccessful()) {
                    if (response.body()!= null && response.body().getCode() == 200) {
                        callback.onSuccess(false);
                    } else {
                        callback.onFailure(new Throwable("无法取消关注"));
                    }
                } else {
                    callback.onFailure(new Throwable("无法取消关注"));

                }
            }
            @Override
            public void onFailure(@NonNull Call<AddFocusBean> call, @NonNull Throwable t) {
                callback.onFailure(new Throwable("无法取消关注"));
            }
        });
    }

//       添加点赞的网络请求
    public static void setLikeRequest(long shareId, long userId,TrueOrFalseCallback callback){
        ApiInterface myApi = MyRequest.request();
        Call<AddLikeBean> call = myApi.getLikeInfo(MyHeaders.getAppId(),MyHeaders.getAppSecret(),shareId,userId);
        call.enqueue(new Callback<AddLikeBean>() {
            @Override
            public void onResponse(@NonNull Call<AddLikeBean> call, @NonNull Response<AddLikeBean> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getCode() == 200) {
                        callback.onSuccess(true);

                    } else {
                        callback.onFailure(new Throwable("无法点赞"));
                    }
                } else {
                    callback.onFailure(new Throwable("无法点赞"));

                }
            }
            @Override
            public void onFailure(@NonNull Call<AddLikeBean> call, @NonNull Throwable t) {
                callback.onFailure(new Throwable("无法点赞"));
            }
        });
    }

    //    取消点赞的网络请求
    public static void cancelLikeRequest(long shareId, long userId, TrueOrFalseCallback callback){
        MyRequest.getShareDetailData(shareId, userId,new ShareDetailCallback() {
            @Override
            public void onSuccess(ShareDetailBean shareDetailBean) {
                long likeId = shareDetailBean.getData().getLikeId();
                ApiInterface myApi = MyRequest.request();
                Call<AddLikeBean> call = myApi.getLikeCancelInfo(MyHeaders.getAppId(),MyHeaders.getAppSecret(),likeId);
                call.enqueue(new Callback<AddLikeBean>() {
                    @Override
                    public void onResponse(@NonNull Call<AddLikeBean> call, @NonNull Response<AddLikeBean> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getCode() == 200) {
                                callback.onSuccess(false);
                            } else {
                                callback.onFailure(new Throwable("无法取消点赞"));
                            }
                        } else {
                            callback.onFailure(new Throwable("无法取消点赞"));
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<AddLikeBean> call, @NonNull Throwable t) {
                        callback.onFailure(new Throwable("无法取消点赞"));
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(new Throwable("无法取消点赞"));
            }
        });
    }

    //    添加收藏的网络请求
    public static void setCollectRequest(long shareId,long userId, TrueOrFalseCallback callback) {
        ApiInterface myApi = MyRequest.request();
        Call<AddCollectBean> call = myApi.getCollectInfo(MyHeaders.getAppId(),MyHeaders.getAppSecret(),shareId,userId);
        call.enqueue(new Callback<AddCollectBean>() {
            @Override
            public void onResponse(@NonNull Call<AddCollectBean> call, @NonNull Response<AddCollectBean> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getCode() == 200) {
                        callback.onSuccess(true);
                    } else {
                        callback.onFailure(new Throwable("无法加入收藏列表"));
                    }
                } else {
                    callback.onFailure(new Throwable("无法加入收藏列表"));

                }
            }
            @Override
            public void onFailure(@NonNull Call<AddCollectBean> call, @NonNull Throwable t) {
                callback.onFailure(new Throwable("无法加入收藏列表"));

            }
        });
    }
    //    取消收藏的网络请求
    public static void cancelCollectRequest(long shareId,long userId, TrueOrFalseCallback callback) {

//        网络请求单个分享详情，获得collectId,再进行取消收藏的请求
        MyRequest.getShareDetailData(shareId, userId,new ShareDetailCallback() {
            @Override
            public void onSuccess(ShareDetailBean shareDetailBean) {
                long collectId = shareDetailBean.getData().getCollectId();
                ApiInterface myApi = MyRequest.request();
                Call<AddCollectBean> call = myApi.getCollectCancelInfo(MyHeaders.getAppId(),MyHeaders.getAppSecret(),collectId);
                call.enqueue(new Callback<AddCollectBean>() {
                    @Override
                    public void onResponse(@NonNull Call<AddCollectBean> call, @NonNull Response<AddCollectBean> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getCode() == 200) {
                                callback.onSuccess(false);
                            } else {
                                callback.onFailure(new Throwable("无法取消加入收藏列表"));
                            }
                        } else {
                            callback.onFailure(new Throwable("无法取消加入收藏列表"));
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<AddCollectBean> call, @NonNull Throwable t) {
                        callback.onFailure(new Throwable("无法取消加入收藏列表"));
                    }
                });
            }
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(new Throwable("无法取消加入收藏列表"));
            }
        });
    }


}
