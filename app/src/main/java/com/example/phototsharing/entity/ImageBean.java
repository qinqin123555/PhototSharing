package com.example.phototsharing.entity;

import java.util.List;

public class ImageBean {

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String imageCode;  // 改为 String 类型
        private List<ImageUrl> imageUrlList;

        public String getImageCode() {
            return imageCode;
        }

        public void setImageCode(String imageCode) {
            this.imageCode = imageCode;
        }

        public List<ImageUrl> getImageUrlList() {
            return imageUrlList;
        }

        public void setImageUrlList(List<ImageUrl> imageUrlList) {
            this.imageUrlList = imageUrlList;
        }

        public static class ImageUrl {
            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
