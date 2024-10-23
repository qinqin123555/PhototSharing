package com.example.phototsharing.entity;
import java.util.List;

public class ImageBean {
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
