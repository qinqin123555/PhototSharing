package com.example.phototsharing.net;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.phototsharing.activity.MainActivity;
import com.example.phototsharing.utilis.BaseResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ShareCallback implements Callback {

    private Activity activity;

    public ShareCallback(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onFailure(@NonNull Call call, IOException e) {
        e.printStackTrace();
    }

    @Override
    public void onResponse(@NonNull Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
            Type jsonType = new TypeToken<BaseResponse<Object>>(){}.getType();
            String body = response.body().string();
            Log.d("ShareCallback", body);

            Gson gson = new Gson();
            BaseResponse<Object> dataResponseBody = gson.fromJson(body, jsonType);
            Log.d("ShareCallback", dataResponseBody.toString());

            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
        }
    }
}
