package com.example.phototsharing.net;

import com.example.phototsharing.entity.ShareDetailBean;

//    定义获取分享详情的回调接口
public interface ShareDetailCallback {
    void onSuccess(ShareDetailBean shareDetailBean);
    void onFailure(Throwable throwable);
}