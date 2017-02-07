package cn.tendata.mdcs.mail;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

public class MailRecipientAction implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum MailRecipientActionStatus {
        SENT_SUCCESS, OPEN, CLICK, UNSUBSCRIBE, HARD_BOUNCE, SOFT_BOUNCE, SPAM_COMPLAINT
    }

    private String email;
    private MailRecipientActionStatus actionStatus;
    private DateTime actionDate;
    private String description;
    private String ip;
    private String address;
    private String browser;
    private String os;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MailRecipientActionStatus getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(MailRecipientActionStatus actionStatus) {
        this.actionStatus = actionStatus;
    }

    public DateTime getActionDate() {
        return actionDate;
    }

    public void setActionDate(DateTime actionDate) {
        this.actionDate = actionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        final MailRecipientAction rhs = (MailRecipientAction) obj;
        return new EqualsBuilder()
                .append(email, rhs.email)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(email)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(email)
                .append(actionStatus)
                .toString();
    }

}
