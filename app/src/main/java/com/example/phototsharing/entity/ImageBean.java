package com.example.phototsharing.entity;

import java.util.List;

public class ImageBean {
    private Long imageCode; // 图片代码
    private List<String> imageUrlList; // 图片 URL 列表
    private String msg; // 新增消息或标题字段
    private Data data; // 假设 Data 是另外的一个类，存储更多详细信息

    // Getter 和 Setter 方法
    public Long getImageCode() {
        return imageCode;
    }

    public void setImageCode(Long imageCode) {
        this.imageCode = imageCode;
    }

    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "imageCode=" + imageCode +
                ", imageUrlList=" + imageUrlList +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    // 假设的 Data 类，用于存储更多信息
    public static class Data {
        private List<String> imageUrlList;

        public List<String> getImageUrlList() {
            return imageUrlList;
        }

        public void setImageUrlList(List<String> imageUrlList) {
            this.imageUrlList = imageUrlList;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "imageUrlList=" + imageUrlList +
                    '}';
        }
    }
}
