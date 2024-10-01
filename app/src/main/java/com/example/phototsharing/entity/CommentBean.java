// CommentBean.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.example.phototsharing.entity;
import java.util.List;

public class CommentBean {
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
        private long current;
        private long total;
        private long size;
        private List<Record> records;

        public long getCurrent() { return current; }
        public void setCurrent(long value) { this.current = value; }

        public long getTotal() { return total; }
        public void setTotal(long value) { this.total = value; }

        public long getSize() { return size; }
        public void setSize(long value) { this.size = value; }

        public List<Record> getRecords() { return records; }
        public void setRecords(List<Record> value) { this.records = value; }

        public static class Record {
            private String createTime;
            private long replyCommentId;
            private long replyCommentUserId;
            private long parentCommentId;
            private String appKey;
            private long shareId;
            private long id;
            private long commentLevel;
            private long pUserId;
            private String userName;
            private String content;
            private long parentCommentUserId;

            public String getCreateTime() { return createTime; }
            public void setCreateTime(String value) { this.createTime = value; }

            public long getReplyCommentId() { return replyCommentId; }
            public void setReplyCommentId(long value) { this.replyCommentId = value; }

            public long getReplyCommentUserId() { return replyCommentUserId; }
            public void setReplyCommentUserId(long value) { this.replyCommentUserId = value; }

            public long getParentCommentId() { return parentCommentId; }
            public void setParentCommentId(long value) { this.parentCommentId = value; }

            public String getAppKey() { return appKey; }
            public void setAppKey(String value) { this.appKey = value; }

            public long getShareId() { return shareId; }
            public void setShareId(long value) { this.shareId = value; }

            public long getid() { return id; }
            public void setid(long value) { this.id = value; }

            public long getCommentLevel() { return commentLevel; }
            public void setCommentLevel(long value) { this.commentLevel = value; }

            public long getPUserId() { return pUserId; }
            public void setPUserId(long value) { this.pUserId = value; }

            public String getUserName() { return userName; }
            public void setUserName(String value) { this.userName = value; }

            public String getContent() { return content; }
            public void setContent(String value) { this.content = value; }

            public long getParentCommentUserId() { return parentCommentUserId; }
            public void setParentCommentUserId(long value) { this.parentCommentUserId = value; }

        }
    }




}
