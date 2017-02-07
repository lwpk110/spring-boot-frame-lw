package cn.tendata.mdcs.data.domain;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import cn.tendata.mdcs.data.jackson.DataView;
import cn.tendata.mdcs.util.JsonUtils;

@Embeddable
public class MailRecipientCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonView(DataView.Basic.class)
    private int recipientCount;
    private String recipientsContent;
    private Collection<MailRecipient> recipients = null;

    protected MailRecipientCollection() {
    }

    public MailRecipientCollection(Collection<MailRecipient> recipients) {
        Assert.notEmpty(recipients, "'recipients' must have elements");
        
        this.recipients = recipients;
        this.recipientCount = recipients.size();
        this.recipientsContent = JsonUtils.serialize(recipients);
    }

    @Column(name = "recipient_count", nullable = false)
    public int getRecipientCount() {
        return recipientCount;
    }

    public void setRecipientCount(int recipientCount) {
        this.recipientCount = recipientCount;
    }
    
    @NotNull
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "recipients", nullable = false)
    protected String getRecipientsContent() {
        return recipientsContent;
    }

    protected void setRecipientsContent(String recipientsContent) {
        this.recipientsContent = recipientsContent;
        this.recipients = JsonUtils.deserialize(recipientsContent, Collection.class, MailRecipient.class);
    }

    @JsonIgnore
    @Transient
    public Collection<MailRecipient> getRecipients() {
        return recipients;
    }
}
