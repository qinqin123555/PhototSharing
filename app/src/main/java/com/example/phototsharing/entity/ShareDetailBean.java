// ShareDetailBean.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.example.phototsharing.entity;
import java.util.List;

public class ShareDetailBean {
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
        private Long collectId;
        private Long imageCode;
        private Long collectNum;
        private boolean hasCollect;
        private String title;
        private String content;
        private Long likeNum;
        private List<String> imageUrlList;
        private Long createTime;
        private boolean hasLike;
        private Long likeId;
        private Long id;
        private boolean hasFocus;
        private Long pUserId;
        private String username;

        public Long getCollectId() { return collectId; }
        public void setCollectId(Long value) { this.collectId = value; }

        public Long getImageCode() { return imageCode; }
        public void setImageCode(Long value) { this.imageCode = value; }

        public Long getCollectNum() { return collectNum; }
        public void setCollectNum(Long value) { this.collectNum = value; }

        public boolean getHasCollect() { return hasCollect; }
        public void setHasCollect(boolean value) { this.hasCollect = value; }

        public String getTitle() { return title; }
        public void setTitle(String value) { this.title = value; }

        public String getContent() { return content; }
        public void setContent(String value) { this.content = value; }

        public Long getLikeNum() { return likeNum; }
        public void setLikeNum(Long value) { this.likeNum = value; }

        public List<String> getImageUrlList() { return imageUrlList; }
        public void setImageUrlList(List<String> value) { this.imageUrlList = value; }

        public Long getCreateTime() { return createTime; }
        public void setCreateTime(Long value) { this.createTime = value; }

        public boolean getHasLike() { return hasLike; }
        public void setHasLike(boolean value) { this.hasLike = value; }

        public Long getLikeId() { return likeId; }
        public void setLikeId(Long value) { this.likeId = value; }

        public Long getid() { return id; }
        public void setid(Long value) { this.id = value; }

        public boolean getHasFocus() { return hasFocus; }
        public void setHasFocus(boolean value) { this.hasFocus = value; }

        public Long getPUserId() { return pUserId; }
        public void setPUserId(Long value) { this.pUserId = value; }

        public String getUsername() { return username; }
        public void setUsername(String value) { this.username = value; }
    }

}

