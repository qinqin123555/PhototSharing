package com.example.phototsharing.net;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.phototsharing.entity.Share;
import com.example.phototsharing.fragment.Image;
import com.example.phototsharing.utilis.BaseResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ImgCodeCallback implements Callback {

    private FragmentActivity activity;
    private Share share;
    private Bundle args;

    public ImgCodeCallback(FragmentActivity activity, Share share, Bundle args) {
        this.activity = activity;
        this.share = share;
        this.args = args;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.e("ImgCodeCallback", "Failed to connect server!");
        e.printStackTrace();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
            final String body = response.body().string();
            Gson gson = new Gson();
            BaseResponse<Image> res = gson.fromJson(body, new TypeToken<BaseResponse<Image>>() {}.getType());

            if (res.getCode() == 500) {
                activity.runOnUiThread(() -> {
                    Toast.makeText(activity.getApplicationContext(), "服务器错误!", Toast.LENGTH_SHORT).show();
                });
                return;
            } else {
                Image image = res.getData();
                Log.d("ImgCodeCallback", image.getImageCode().toString());

                share.setImageCode(image.getImageCode()); // 假设 Share 类有 setImageCode 方法
                Log.d("ImgCodeCallback", share.toString());

                if (args == null) {
                    addShare("add");
                } else {
                    addShare("change");
                }
            }
        } else {
            Log.d("ImgCodeCallback", response.toString());
        }
    }

    private void addShare(String action) {
        // 执行添加或修改分享的逻辑
        Log.d("ImgCodeCallback", "Action: " + action);
    }
}



