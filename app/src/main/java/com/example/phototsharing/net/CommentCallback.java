package com.example.phototsharing.net;

import com.example.phototsharing.entity.CommentBean;

//定义获取一级评论的回调接口
public interface CommentCallback {
    void onSuccess(CommentBean commentBean);
    void onFailure(Throwable throwable);
}
