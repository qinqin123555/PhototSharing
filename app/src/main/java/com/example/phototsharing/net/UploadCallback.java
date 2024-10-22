package com.example.phototsharing.net;

import com.example.phototsharing.entity.ImageBean;

public interface UploadCallback {
    void onSuccess(ImageBean imageBean);
    void onFailure(Throwable throwable);
}


