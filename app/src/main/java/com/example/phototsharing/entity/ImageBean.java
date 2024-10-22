package com.example.phototsharing.entity;
import java.util.List;

public class ImageBean {
    private String msg;
    private long code;
    private Data data;

    // Getter and Setter
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public static class Data {
        private List<ImageUrlList> imageUrlList;
        private long imageCode;

        // Getter and Setter
        public List<ImageUrlList> getImageUrlList() {
            return imageUrlList;
        }

        public void setImageUrlList(List<ImageUrlList> imageUrlList) {
            this.imageUrlList = imageUrlList;
        }

        public long getImageCode() {
            return imageCode;
        }

        public void setImageCode(long imageCode) {
            this.imageCode = imageCode;
        }
    }

    public static class ImageUrlList {
        private String imageUrl;
        private String imagePath;

        // Getter and Setter
        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }
    }

}
