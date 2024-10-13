package com.example.phototsharing.fragment;

import java.util.List;

public class Image {
    Long imageCode;
    List<String> imageUrlList;

    public Long getImageCode() {
        return imageCode;
    }

    public void setImageCode(Long imageCode) {
        this.imageCode = imageCode;
    }

    @Override
    public String toString() {
        return "Image{" +
                "imageCode=" + imageCode +
                ", imageUrlList=" + imageUrlList +
                '}';
    }
}
