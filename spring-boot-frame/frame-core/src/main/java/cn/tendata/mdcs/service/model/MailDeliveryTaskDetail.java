package cn.tendata.mdcs.service.model;

import cn.tendata.mdcs.data.domain.MailDeliveryChannel;
import cn.tendata.mdcs.data.domain.MailTemplate;
import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailDeliverySettings;
import cn.tendata.mdcs.data.domain.UserMailRecipientGroup;
import cn.tendata.mdcs.data.domain.UserMailTemplate;
import org.joda.time.DateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

public class MailDeliveryTaskDetail {

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @Size(max = 255)
    private String description;

    private boolean scheduled;
    private DateTime scheduledDate;

    private long parentUserId;
    private String parentUsername;

    @NotNull
    private UserMailDeliverySettings userMailDeliverySettings;

    @NotNull
    private MailDeliveryChannel deliveryChannel;

    @NotNull
    private Collection<UserMailRecipientGroup> userMailRecipientGroups;

    @NotNull
    private UserMailTemplate userMailTemplate;

    @NotNull
    @Valid
    private MailTemplate mailTemplate;

    private User user;

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

    public UserMailDeliverySettings getUserMailDeliverySettings() {
        return userMailDeliverySettings;
    }

    public void setUserMailDeliverySettings(UserMailDeliverySettings userMailDeliverySettings) {
        this.userMailDeliverySettings = userMailDeliverySettings;
    }

    public MailDeliveryChannel getDeliveryChannel() {
        return deliveryChannel;
    }

    public void setDeliveryChannel(MailDeliveryChannel deliveryChannel) {
        this.deliveryChannel = deliveryChannel;
    }

    public Collection<UserMailRecipientGroup> getUserMailRecipientGroups() {
        return userMailRecipientGroups;
    }

    public void setUserMailRecipientGroups(Collection<UserMailRecipientGroup> userMailRecipientGroups) {
        this.userMailRecipientGroups = userMailRecipientGroups;
    }

    public UserMailTemplate getUserMailTemplate() {
        return userMailTemplate;
    }

    public void setUserMailTemplate(UserMailTemplate userMailTemplate) {
        this.userMailTemplate = userMailTemplate;
    }

    public MailTemplate getMailTemplate() {
        return mailTemplate;
    }

    public void setMailTemplate(MailTemplate mailTemplate) {
        this.mailTemplate = mailTemplate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
