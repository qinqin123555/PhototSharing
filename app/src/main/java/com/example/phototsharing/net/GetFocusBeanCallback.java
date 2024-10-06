package com.example.phototsharing.net;

import com.example.phototsharing.entity.CommentBean;
import com.example.phototsharing.entity.HasFocusBean;

public interface GetFocusBeanCallback {
    void onSuccess(HasFocusBean hasFocusBean);
    void onFailure(Throwable throwable);
}
