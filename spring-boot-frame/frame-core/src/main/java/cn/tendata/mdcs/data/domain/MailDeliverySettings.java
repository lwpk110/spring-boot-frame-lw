package cn.tendata.mdcs.data.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

@Embeddable
public class MailDeliverySettings implements Serializable{

    private static final long serialVersionUID = 1L;

    private String senderName;
    private String senderEmail;
    private String replyName;
    private String replyEmail;
    
    private boolean agentSend;
    
    public MailDeliverySettings() {}
    
    public MailDeliverySettings(String senderName, String senderEmail){
        this(senderName, senderEmail, null, null);
    }
    
    public MailDeliverySettings(String senderName, String senderEmail, String replyName, String replayEmail) {
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.replyName = replyName;
        this.replyEmail = replayEmail;
    }


    @Size(min=1, max=50)
    @Column(name="sender_name", length=50, nullable=false)
    public String getSenderName() {
        return senderName;
    }
    
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    

    @Email
    @Size(min=1, max=255)
    @Column(name="sender_email", length=255)
    public String getSenderEmail() {
        return senderEmail;
    }
    
    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }
    
    @Size(max=50)
    @Column(name="reply_name", length=50)
    public String getReplyName() {
        return replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }

    @Email
    @Size(max=255)
    @Column(name="reply_email", length=255)
    public String getReplyEmail() {
        return replyEmail;
    }
    
    public void setReplyEmail(String replyEmail) {
        this.replyEmail = replyEmail;
    }

    @Column(name="is_agent_send", nullable=false)
    public boolean isAgentSend() {
        return agentSend;
    }

    public void setAgentSend(boolean agentSend) {
        this.agentSend = agentSend;
    }
}
