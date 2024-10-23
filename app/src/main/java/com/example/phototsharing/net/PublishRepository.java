package com.example.phototsharing.net;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.phototsharing.entity.AddLikeBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublishRepository {

    private Context context;
    private ApiInterface apiInterface;

    public PublishRepository(Context context) {
        this.context = context;
        this.apiInterface = MyRequest.request();
    }

    public void publishContent(String title, String content, String imageCode, long userId, PublishCallback callback) {
        Call<AddLikeBean> call = apiInterface.publishContent(
                MyHeaders.getAppId(),
                MyHeaders.getAppSecret(),
                title,
                content,
                imageCode,
                userId
        );

        call.enqueue(new Callback<AddLikeBean>() {
            @Override
            public void onResponse(@NonNull Call<AddLikeBean> call, @NonNull Response<AddLikeBean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == 200) {
                        callback.onSuccess("发布成功");
                    } else {
                        callback.onError("发布失败: " + response.body().getMsg());
                    }
                } else {
                    callback.onError("HTTP错误: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddLikeBean> call, @NonNull Throwable t) {
                callback.onError("请求失败: " + t.getMessage());
            }
        });
    }

    public void saveDraft(String title, String content, String imageCode, long userId, PublishCallback callback) {
        Call<ResponseBody> call = apiInterface.saveDraft(
                MyHeaders.getAppId(),
                MyHeaders.getAppSecret(),
                title,
                content,
                imageCode,
                userId
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess("草稿保存成功");
                } else {
                    callback.onError("HTTP错误: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onError("请求失败: " + t.getMessage());
            }
        });
    }

    public interface UploadCallback {
        void onSuccess(ResponseBody imageBean);
        void onFailure(Throwable t);
    }

    public interface PublishCallback {
        void onSuccess(String message);
        void onError(String errorMessage);
    }
}
