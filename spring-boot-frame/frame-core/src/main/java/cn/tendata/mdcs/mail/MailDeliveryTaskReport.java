package cn.tendata.mdcs.mail;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class MailDeliveryTaskReport implements Serializable {

    private static final long serialVersionUID = 1L;

    private String taskId;
    private String subject;
    private String senderName;
    private String senderEmail;
    private String replyName;
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

    private DateTime sendDate;
    private DateTime finishDate;

    public MailDeliveryTaskReport(){}
    
    public MailDeliveryTaskReport(String taskId){
        this.taskId = taskId;
    }
    
    public String getTaskId() {
        return taskId;
    }
    
    public void setTaskId(String taskId) {
        this.taskId = taskId;
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
        if (sendDate.getZone() == DateTimeZone.UTC) {
            sendDate = new DateTime(sendDate.getMillis());
        }
        this.sendDate = sendDate;
    }
    
    public DateTime getFinishDate() {
        return finishDate;
    }
    
    public void setFinishDate(DateTime finishDate) {
        this.finishDate = finishDate;
    }
}
