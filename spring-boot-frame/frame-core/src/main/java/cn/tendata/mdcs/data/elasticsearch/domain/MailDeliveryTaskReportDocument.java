package cn.tendata.mdcs.data.elasticsearch.domain;

import org.joda.time.DateTime;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import cn.tendata.mdcs.data.domain.AbstractEntity;

@Document(indexName=MailDeliveryTaskReportDocument.INDEX, type=MailDeliveryTaskReportDocument.TYPE)
@Setting(settingPath="/elasticsearch/settings/mailDeliveryTaskReport.json")
public class MailDeliveryTaskReportDocument extends AbstractEntity<String> {

    private static final long serialVersionUID = 1L;
    
    public static final String INDEX = "tendata_mdcs";
    public static final String TYPE = "mail_delivery_task_report";
    
    @Field(type=FieldType.String, index=FieldIndex.analyzed, store=true)
    private String taskName;
    @Field(type=FieldType.String, index=FieldIndex.analyzed, store=true)
    private String subject;
    @Field(type=FieldType.String, index=FieldIndex.not_analyzed)
    private String senderName;
    @Field(type=FieldType.String, index=FieldIndex.not_analyzed)
    private String senderEmail;
    @Field(type=FieldType.String, index=FieldIndex.not_analyzed)
    private String replyName;
    @Field(type=FieldType.String, index=FieldIndex.not_analyzed)
    private String replyEmail;
    private int total;
    private int sent;
    private int hardBounce;
    private int softBounce;
    private int openCount;
    private int uniqueOpenCount;
    private int allClicked;
    private int linkClicked;
    private int mailClicked;
    private int unsubscribe;
    @Field(type=FieldType.Date, index=FieldIndex.not_analyzed)
    private DateTime sendDate;
    @Field(type=FieldType.Date, index=FieldIndex.not_analyzed)
    private DateTime finishDate;
    
    @Field(type=FieldType.Date, index=FieldIndex.not_analyzed)
    private DateTime createdDate;
    @Field(type=FieldType.Date, index=FieldIndex.not_analyzed)
    private DateTime lastModifiedDate;
    
    private UserDocument user;
    private MailDeliveryChannelDocument deliveryChannel;
    
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getSenderName() {
        return senderName;
    }
    
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    
    public String getSenderEmail() {
        return senderEmail;
    }
    
    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }
    
    public String getReplyName() {
        return replyName;
    }
    
    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }
    
    public String getReplyEmail() {
        return replyEmail;
    }
    
    public void setReplyEmail(String replyEmail) {
        this.replyEmail = replyEmail;
    }
    
    public int getTotal() {
        return total;
    }
    
    public void setTotal(int total) {
        this.total = total;
    }
    
    public int getSent() {
        return sent;
    }
    
    public void setSent(int sent) {
        this.sent = sent;
    }
    
    public int getHardBounce() {
        return hardBounce;
    }
    
    public void setHardBounce(int hardBounce) {
        this.hardBounce = hardBounce;
    }
    
    public int getSoftBounce() {
        return softBounce;
    }
    
    public void setSoftBounce(int softBounce) {
        this.softBounce = softBounce;
    }
    
    public int getOpenCount() {
        return openCount;
    }
    
    public void setOpenCount(int openCount) {
        this.openCount = openCount;
    }
    
    public int getUniqueOpenCount() {
        return uniqueOpenCount;
    }
    
    public void setUniqueOpenCount(int uniqueOpenCount) {
        this.uniqueOpenCount = uniqueOpenCount;
    }
    
    public int getAllClicked() {
        return allClicked;
    }
    
    public void setAllClicked(int allClicked) {
        this.allClicked = allClicked;
    }
    
    public int getLinkClicked() {
        return linkClicked;
    }
    
    public void setLinkClicked(int linkClicked) {
        this.linkClicked = linkClicked;
    }
    
    public int getMailClicked() {
        return mailClicked;
    }
    
    public void setMailClicked(int mailClicked) {
        this.mailClicked = mailClicked;
    }
    
    public int getUnsubscribe() {
        return unsubscribe;
    }
    
    public void setUnsubscribe(int unsubscribe) {
        this.unsubscribe = unsubscribe;
    }
    
    public DateTime getSendDate() {
        return sendDate;
    }

    public void setSendDate(DateTime sendDate) {
        this.sendDate = sendDate;
    }

    public DateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(DateTime finishDate) {
        this.finishDate = finishDate;
    } 

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public DateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    public UserDocument getUser() {
        return user;
    }

    public void setUser(UserDocument user) {
        this.user = user;
    }

    public MailDeliveryChannelDocument getDeliveryChannel() {
        return deliveryChannel;
    }

    public void setDeliveryChannel(MailDeliveryChannelDocument deliveryChannel) {
        this.deliveryChannel = deliveryChannel;
    }
}
