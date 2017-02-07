package cn.tendata.mdcs.mail.core;

import cn.tendata.mdcs.data.domain.PasswayCampaign;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.joda.time.DateTime;

import cn.tendata.mdcs.data.domain.MailRecipient;
import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;

public class MailDeliveryTaskData implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private UUID id;
    private String taskId;
    private String name;
    private String description;
    
    private String senderName;
    private String senderEmail;
    private String replyName;
    private String replyEmail;
    
    private String subject;
    private String body;
    private boolean isHtml;
    
    private Collection<MailRecipient> recipients = Collections.emptyList();
    private boolean scheduled;
    private DateTime scheduledDate;
    
    private int channelId;
    private String passwayCampaignKey;
    private DateTime createDate;

    private PasswayCampaign passwayCampaign;

    private Integer mailingId;

    public Integer getMailingId() {
        return mailingId;
    }

    public void setMailingId(Integer mailingId) {
        this.mailingId = mailingId;
    }

    public MailDeliveryTaskData() {}
    
    public MailDeliveryTaskData(UserMailDeliveryTask task){
        this.id = task.getId();
        this.taskId = task.getTaskId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.senderName = task.getDeliverySettings().getSenderName();
        this.senderEmail = task.getDeliverySettings().getSenderEmail();
        this.replyName = task.getDeliverySettings().getReplyName();
        this.replyEmail = task.getDeliverySettings().getReplyEmail();
        this.subject = task.getTemplate().getSubject();
        this.body = task.getTemplate().getBody();
        this.isHtml = task.getTemplate().isHtml();
        this.recipients = task.getRecipientCollection().getRecipients();
        this.scheduled = task.isScheduled();
        this.scheduledDate = task.getScheduledDate();
        this.channelId = task.getDeliveryChannel().getId();
        this.passwayCampaignKey = task.getPasswayCampaignKey();
        this.createDate = task.getCreatedDate();
        this.mailingId = task.getMailingId();
    }

    public PasswayCampaign getPasswayCampaign() {
        return passwayCampaign;
    }

    public void setPasswayCampaign(PasswayCampaign passwayCampaign) {
        this.passwayCampaign = passwayCampaign;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean isHtml) {
        this.isHtml = isHtml;
    }

    public Collection<MailRecipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(Collection<MailRecipient> recipients) {
        this.recipients = recipients;
    }

    public boolean isScheduled() {
        return scheduled;
    }
    
    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }
    
    public DateTime getScheduledDate() {
        return scheduledDate;
    }
    
    public void setScheduledDate(DateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getPasswayCampaignKey() {
        return passwayCampaignKey;
    }

    public void setPasswayCampaignKey(String passwayCampaignKey) {
        this.passwayCampaignKey = passwayCampaignKey;
    }

    public DateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(DateTime createDate) {
        this.createDate = createDate;
    }
}
