package com.example.phototsharing.net;

import com.example.phototsharing.entity.RegisterBean;

public interface RegisterCallback {
    void onSuccess(RegisterBean registerBean);  // 注册成功时的回调
    void onFailure(Throwable throwable);       // 注册失败时的回调

}
