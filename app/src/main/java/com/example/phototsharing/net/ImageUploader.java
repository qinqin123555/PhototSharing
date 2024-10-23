package com.example.phototsharing.net;

import com.example.phototsharing.entity.ImageBean;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ImageUploader {

    public void uploadImage(File imageFile, String fileType, UploadCallback callback) {
        OkHttpClient client = new OkHttpClient();

        // 创建 RequestBody，设置文件类型和文件内容
        RequestBody requestBody = RequestBody.create(MediaType.parse(fileType), imageFile);

        // 构建上传请求
        Request request = new Request.Builder()
                .url(URLS.UPLOAD_IMAGE.getUrl())
                .headers(MyHeaders.getHeaders())
                .post(requestBody)
                .build();

        // 使用 OkHttp 的 Callback，而不是 UploadCallback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onFailure(e);  // 使用你自定义的 UploadCallback 来处理
                }
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    ImageBean imageBean = new Gson().fromJson(responseBody, ImageBean.class);
                    if (callback != null) {
                        callback.onSuccess(imageBean);  // 使用 UploadCallback 的 onSuccess
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure(new IOException("上传失败，响应码：" + response.code()));
                    }
                }
            }
        });
    }
}

