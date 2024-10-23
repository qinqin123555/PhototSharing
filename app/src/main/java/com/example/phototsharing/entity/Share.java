package com.example.phototsharing.entity;

public class Share {
    private String  content;
    private Integer id = null;
    private Long imageCode;
    private Long pUserId;
    private String title;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getImageCode() {
        return imageCode;
    }

    public void setImageCode(Long imageCode) {
        this.imageCode = imageCode;
    }

    public Long getpUserId() {
        return pUserId;
    }

    public void setpUserId(Long pUserId) {
        this.pUserId = pUserId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Share{" +
                "content='" + content + '\'' +
                ", id=" + id +
                ", imageCode=" + imageCode +
                ", pUserId=" + pUserId +
                ", title='" + title + '\'' +
                '}';
    }
}
