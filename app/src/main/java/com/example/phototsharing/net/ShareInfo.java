package com.example.phototsharing.net;

public class ShareInfo {
    private Long imageCode;

    public Long getImageCode() {
        return imageCode;
    }

    public void setImageCode(Long imageCode) {
        this.imageCode = imageCode;
    }

    @Override
    public String toString() {
        return "ShareInfo{" +
                "imageCode=" + imageCode +
                '}';
    }
}
