package com.madx.cherry.core.wechat.bean;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by A-mdx on 2017/6/26.
 */
public class MongoDataPO implements Serializable{
    
    @Id
    private String id;
    
    private String name;  // 名称，与保存在本地里的文件有关联。
    private String type;  // 类型
    private String dataId;  // 对应 微信 media id
    private Date creationTime;
    private String creator;
    private Integer status; // 状态码
    private Long mysqId;   // 保存至 mysql中的 id
    private String path;  //  要保存到本地的地址
    private Boolean saveLocal;  // 是否已经保存到本地了
    private Date modiftTime;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getSaveLocal() {
        return saveLocal;
    }

    public void setSaveLocal(Boolean saveLocal) {
        this.saveLocal = saveLocal;
    }

    public Long getMysqId() {
        return mysqId;
    }

    public void setMysqId(Long mysqId) {
        this.mysqId = mysqId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MongoDataPO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", dataId='" + dataId + '\'' +
                ", creationTime=" + creationTime +
                ", creator='" + creator + '\'' +
                ", status=" + status +
                ", mysqId=" + mysqId +
                ", path='" + path + '\'' +
                ", saveLocal=" + saveLocal +
                '}';
    }
}