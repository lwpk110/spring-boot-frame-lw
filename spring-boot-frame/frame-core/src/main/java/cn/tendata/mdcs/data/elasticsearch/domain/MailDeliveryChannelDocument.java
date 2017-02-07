package cn.tendata.mdcs.data.elasticsearch.domain;

import java.io.Serializable;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class MailDeliveryChannelDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    @Field(type=FieldType.String, index=FieldIndex.not_analyzed)
    private String name;
    private int nodeId;
    @Field(type=FieldType.String, index=FieldIndex.not_analyzed)
    private String nodeName;
    
    public MailDeliveryChannelDocument() {}
    
    public MailDeliveryChannelDocument(int id, String name, int nodeId, String nodeName){
        this.id = id;
        this.name = name;
        this.nodeId = nodeId;
        this.nodeName = nodeName;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
