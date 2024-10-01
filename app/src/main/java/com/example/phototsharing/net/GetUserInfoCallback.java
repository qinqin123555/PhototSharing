package com.example.phototsharing.net;

import com.example.phototsharing.entity.CommentBean;
import com.example.phototsharing.entity.PersonBean;

public interface GetUserInfoCallback {
    void onSuccess(PersonBean personBean);
    void onFailure(Throwable throwable);
}
