package cn.tendata.mdcs.data.elasticsearch.domain;

import java.io.Serializable;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class UserDocument implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private long userId;
    @Field(type=FieldType.String, index=FieldIndex.not_analyzed)
    private String username;
    private long parentUserId;
    @Field(type=FieldType.String, index=FieldIndex.not_analyzed)
    private String parentUsername;
    
    public UserDocument() {}
    
    public UserDocument(long userId, String username, long parentUserId, String parentUsername) {
        this.userId = userId;
        this.username = username;
        this.parentUserId = parentUserId;
        this.parentUsername = parentUsername;
    }

    public long getUserId() {
        return userId;
    }
    
    public void setUserId(long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public long getParentUserId() {
        return parentUserId;
    }
    
    public void setParentUserId(long parentUserId) {
        this.parentUserId = parentUserId;
    }
    
    public String getParentUsername() {
        return parentUsername;
    }
    
    public void setParentUsername(String parentUsername) {
        this.parentUsername = parentUsername;
    }
}
