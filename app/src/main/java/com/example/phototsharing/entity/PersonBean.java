// PersonBean.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.example.phototsharing.entity;
import androidx.annotation.NonNull;

import java.util.List;

public class PersonBean {
    private String msg;
    private long code;
    private Data data;

    public String getMsg() { return msg; }
    public void setMsg(String value) { this.msg = value; }

    public long getCode() { return code; }
    public void setCode(long value) { this.code = value; }

    public Data getData() { return data; }
    public void setData(Data value) { this.data = value; }

    @NonNull
    @Override
    public String toString() {
        return "PersonBean{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + (data != null ? data.toString() : "null") +
                '}';
    }

}

// Data.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

class Data {
    private String createTime;
    private String introduce;
    private long sex;
    private String appKey;
    private String id;
    private String avatar;
    private String username;
    private String lastUpdateTime;

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String value) { this.createTime = value; }

    public String getIntroduce() { return introduce; }
    public void setIntroduce(String value) { this.introduce = value; }

    public long getSex() { return sex; }
    public void setSex(long value) { this.sex = value; }

    public String getAppKey() { return appKey; }
    public void setAppKey(String value) { this.appKey = value; }

    public String getid() { return id; }
    public void setid(String value) { this.id = value; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String value) { this.avatar = value; }

    public String getUsername() { return username; }
    public void setUsername(String value) { this.username = value; }

    public String getLastUpdateTime() { return lastUpdateTime; }
    public void setLastUpdateTime(String value) { this.lastUpdateTime = value; }
}
