package com.example.phototsharing.utilis;

import androidx.annotation.NonNull;

public class BaseResponse<T> {
    private int code;
    private String msg;
    private T data;
    @NonNull
    @Override
    public String toString() {
        return "ResponseBody{" +
                "code=" + getCode() +
                ", msg='" + getMsg() + '\'' +
                ", data=" + getData() +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
