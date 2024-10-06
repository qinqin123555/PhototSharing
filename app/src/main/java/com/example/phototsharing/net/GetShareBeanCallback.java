package com.example.phototsharing.net;

import com.example.phototsharing.entity.PersonBean;
import com.example.phototsharing.entity.ShareBean;

public interface GetShareBeanCallback {
    void onSuccess(ShareBean shareBean);
    void onFailure(Throwable throwable);
}
