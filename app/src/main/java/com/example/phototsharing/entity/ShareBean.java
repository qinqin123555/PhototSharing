// ShareBean.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.example.phototsharing.entity;
import java.util.List;
import java.io.IOException;


public class ShareBean {
    private String msg;
    private long code;
    private Data data;

    public String getMsg() { return msg; }
    public void setMsg(String value) { this.msg = value; }

    public long getCode() { return code; }
    public void setCode(long value) { this.code = value; }

    public Data getData() { return data; }
    public void setData(Data value) { this.data = value; }

    public static class Data {
        private long total;
        private long current;
        private long size;
        private List<Record> records;

        public long getTotal() { return total; }
        public void setTotal(long value) { this.total = value; }

        public long getCurrent() { return current; }
        public void setCurrent(long value) { this.current = value; }

        public long getSize() { return size; }
        public void setSize(long value) { this.size = value; }

        public List<Record> getRecords() { return records; }
        public void setRecords(List<Record> value) { this.records = value; }
    }



    public static class Record {
        private long imageCode;
        private long collectNum;
        private boolean hasCollect;
        private String avatar;
        private String title;
        private String content;
        private long likeNum;
        private List<String> imageUrlList;
        private long createTime;
        private boolean hasLike;
        private long id;
        private long pUserId;
        private boolean hasFocus;
        private String  username;

        public long getImageCode() { return imageCode; }
        public void setImageCode(long value) { this.imageCode = value; }

        public long getCollectNum() { return collectNum; }
        public void setCollectNum(long value) { this.collectNum = value; }

        public boolean getHasCollect() { return hasCollect; }
        public void setHasCollect(boolean value) { this.hasCollect = value; }

        public String getAvatar() { return avatar; }
        public void setAvatar(String value) { this.avatar = value; }

        public String getTitle() { return title; }
        public void setTitle(String value) { this.title = value; }

        public String getContent() { return content; }
        public void setContent(String value) { this.content = value; }

        public long getLikeNum() { return likeNum; }
        public void setLikeNum(long value) { this.likeNum = value; }

        public List<String> getImageUrlList() { return imageUrlList; }
        public void setImageUrlList(List<String> value) { this.imageUrlList = value; }

        public long getCreateTime() { return createTime; }
        public void setCreateTime(long value) { this.createTime = value; }

        public boolean getHasLike() { return hasLike; }
        public void setHasLike(boolean value) { this.hasLike = value; }

        public long getid() { return id; }
        public void setid(Long value) { this.id = value; }

        public long getPUserId() { return pUserId; }
        public void setPUserId(long value) { this.pUserId = value; }

        public boolean getHasFocus() { return hasFocus; }
        public void setHasFocus(boolean value) { this.hasFocus = value; }

        public String getUsername() { return username; }
        public void setUsername(String value) { this.username = value; }
    }



}


