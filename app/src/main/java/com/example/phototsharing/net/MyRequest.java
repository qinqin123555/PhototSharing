package com.example.phototsharing.net;

import java.util.Map;

import okhttp3.Callback;
import okhttp3.Headers;

public class MyRequest implements MyHttps{

    @Override
    public void doGet(String url, Headers headers, Callback callback) {

    }

    @Override
    public void doGet(String url, Headers headers, Map<String, String> params, Callback callback) {

    }

    @Override
    public void doPost(String url, Headers headers, Map<String, Object> params, Callback callback) {

    }
}
