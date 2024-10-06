package com.example.phototsharing.net;

import com.example.phototsharing.entity.CommentBean;

public interface TrueOrFalseCallback {
    void onSuccess(Boolean b);
    void onFailure(Throwable throwable);
}
