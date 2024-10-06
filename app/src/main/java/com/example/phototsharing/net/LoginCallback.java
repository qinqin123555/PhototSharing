package com.example.phototsharing.net;

import com.example.phototsharing.entity.PersonBean;

public interface LoginCallback {
    void onSuccess(PersonBean loginResponse);
    void onFailure(Throwable throwable);
}
