package cn.tendata.mdcs.data.domain;

import cn.tendata.mdcs.data.jackson.DataView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@SecondaryTables({
        @SecondaryTable(name = UserMailDeliveryTask.USER_MAIL_DELIVERY_TASKS_TEMPLATES, pkJoinColumns = @PrimaryKeyJoinColumn(name = "id")),
        @SecondaryTable(name = UserMailDeliveryTask.USER_MAIL_DELIVERY_TASKS_RECIPIENTS, pkJoinColumns = @PrimaryKeyJoinColumn(name = "id"))
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserMailDeliveryTask extends AbstractEntityAuditable<UUID> {

    private static final long serialVersionUID = 1L;

    public static final String USER_MAIL_DELIVERY_TASKS_TEMPLATES = "user_mail_delivery_tasks_templates";
    public static final String USER_MAIL_DELIVERY_TASKS_RECIPIENTS = "user_mail_delivery_tasks_recipients";

    public static class DeliveryStatus {
        public static final int NONE = 0;
        public static final int FAILED = 1;
        public static final int CANCEL = 2;
        public static final int SUCCESS = 100;
    }

    @JsonView(DataView.Basic.class)
    private String name;
    private String description;
    private MailDeliverySettings deliverySettings;
    private MailTemplate template;
    @JsonView(DataView.Basic.class)
    private int totalFee;
    //    @JsonView(DataView.Basic.class)
    private MailRecipientCollection recipientCollection;
    @JsonView(DataView.Basic.class)
    private boolean scheduled;
    @JsonView(DataView.Basic.class)
    private DateTime scheduledDate;
    @JsonView(DataView.Basic.class)
    private int deliveryStatus;
    @JsonView(DataView.Basic.class)
    private DateTime deliveryDate;

    @JsonView(DataView.Basic.class)
    private MailDeliveryChannel deliveryChannel;
    @JsonIgnore
    private MailDeliveryChannelNode deliveryChannelNode;

    @JsonView(DataView.Basic.class)
    private User user;

    private long parentUserId;
    private String parentUsername;

    private String extraTaskId;

    private long version;

    private String passwayCampaignKey;

    private Integer mailingId;

    @Column(name = "mailing_id")
    public Integer getMailingId() {
        return mailingId;
    }

    public void setMailingId(Integer mailingId) {
        this.mailingId = mailingId;
    }

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    public UUID getId() {
        return super.getId();
    }

    @Transient
    public String getTaskId() {
        return getId().toString();
    }

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name", length = 100, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Size(max = 255)
    @Column(name = "description", length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull
    @Embedded
    public MailDeliverySettings getDeliverySettings() {
        return deliverySettings;
    }

    public void setDeliverySettings(MailDeliverySettings deliverySettings) {
        this.deliverySettings = deliverySettings;
    }

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "subject", column = @Column(name = "subject", length = 255, nullable = false,
                    table = USER_MAIL_DELIVERY_TASKS_TEMPLATES)),
            @AttributeOverride(name = "body", column = @Column(name = "body", nullable = false,
                    table = USER_MAIL_DELIVERY_TASKS_TEMPLATES)),
            @AttributeOverride(name = "isHtml", column = @Column(name = "is_html", nullable = false,
                    table = USER_MAIL_DELIVERY_TASKS_TEMPLATES))
    })
    public MailTemplate getTemplate() {
        return template;
    }

    public void setTemplate(MailTemplate template) {
        this.template = template;
    }

    @Column(name = "total_fee", nullable = false)
    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "recipientCount", column = @Column(name = "recipient_count", length = 255, nullable = false,
                    table = USER_MAIL_DELIVERY_TASKS_RECIPIENTS)),
            @AttributeOverride(name = "recipientsContent", column = @Column(name = "recipients", nullable = false,
                    table = USER_MAIL_DELIVERY_TASKS_RECIPIENTS))
    })
    public MailRecipientCollection getRecipientCollection() {
        return recipientCollection;
    }

    public void setRecipientCollection(MailRecipientCollection recipientCollection) {
        this.recipientCollection = recipientCollection;
    }

    @Column(name = "scheduled", nullable = false)
    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    @Column(name = "scheduled_date")
    public DateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(DateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    @Column(name = "delivery_status", nullable = false)
    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(int deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    @Column(name = "delivery_date")
    public DateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(DateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "channel_id", nullable = false)
    public MailDeliveryChannel getDeliveryChannel() {
        return deliveryChannel;
    }

    public void setDeliveryChannel(MailDeliveryChannel deliveryChannel) {
        this.deliveryChannel = deliveryChannel;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_node_id")
    public MailDeliveryChannelNode getDeliveryChannelNode() {
        return deliveryChannelNode;
    }

    public void setDeliveryChannelNode(MailDeliveryChannelNode deliveryChannelNode) {
        this.deliveryChannelNode = deliveryChannelNode;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "parent_user_id", nullable = false)
    public long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(long parentUserId) {
        this.parentUserId = parentUserId;
    }

    @Size(max = 255)
    @Column(name = "parent_username", length = 255)
    public String getParentUsername() {
        return parentUsername;
    }

    public void setParentUsername(String parentUsername) {
        this.parentUsername = parentUsername;
    }

    @Column(name = "extra_task_id", length = 100)
    public String getExtraTaskId() {
        return extraTaskId;
    }

    public void setExtraTaskId(String extraTaskId) {
        this.extraTaskId = extraTaskId;
    }

    @Version
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    protected UserMailDeliveryTask() {
    }

    @Column(name = "passway_campaign_key", length = 20)
    public String getPasswayCampaignKey() {
        return passwayCampaignKey;
    }

    public void setPasswayCampaignKey(String passwayCampaignKey) {
        this.passwayCampaignKey = passwayCampaignKey;
    }

    public UserMailDeliveryTask(UserMailDeliveryTaskBuilder builder) {
        Assert.notNull(builder.deliverySettings, "'deliverySettings' must not be null");
        Assert.notNull(builder.mailTemplate, "'mailTemplate' must not be null");
        Assert.notNull(builder.mailRecipientCollection, "'mailRecipientCollection' must not be null");
        Assert.notNull(builder.deliveryChannel, "'deliveryChannel' must not be null");
        Assert.notNull(builder.user, "'user' must not be null");
        this.name = builder.name;
        this.description = builder.description;
        this.deliverySettings = builder.deliverySettings;
        this.template = builder.mailTemplate;
        this.recipientCollection = builder.mailRecipientCollection;
        this.deliveryChannel = builder.deliveryChannel;
        this.totalFee = this.deliveryChannel.getFee() * this.recipientCollection.getRecipientCount();
        this.scheduled = builder.scheduled;
        this.scheduledDate = builder.scheduledDate;
        this.deliveryStatus = DeliveryStatus.NONE;
        this.deliveryDate = DateTime.now();
        this.user = builder.user;
        this.parentUserId = builder.parentUserId;
        this.parentUsername = builder.parentUsername;
    }

    public static class UserMailDeliveryTaskBuilder {

        private String name;
        private String description;
        private MailDeliverySettings deliverySettings;
        private MailTemplate mailTemplate;
        private MailRecipientCollection mailRecipientCollection;
        private MailDeliveryChannel deliveryChannel;
        private boolean scheduled;
        private DateTime scheduledDate;
        private User user;
        private long parentUserId;
        private String parentUsername;

        public UserMailDeliveryTaskBuilder(String name) {
            Assert.hasText(name, "'name' must not be empty");
            this.name = name;
        }

        public UserMailDeliveryTaskBuilder description(String description) {
            this.description = description;
            return this;
        }

        public UserMailDeliveryTaskBuilder mailDeliverySettings(MailDeliverySettings deliverySettings) {
            Assert.notNull(deliverySettings, "'deliverySettings' must not be null");
            this.deliverySettings = deliverySettings;
            return this;
        }

        public UserMailDeliveryTaskBuilder mailTemplate(MailTemplate mailTemplate) {
            Assert.notNull(mailTemplate, "'mailTemplate' must not be null");
            this.mailTemplate = mailTemplate;
            return this;
        }

        public UserMailDeliveryTaskBuilder mailRecipientCollection(MailRecipientCollection mailRecipientCollection) {
            Assert.notNull(mailRecipientCollection, "'mailRecipientCollection' must not be null");
            this.mailRecipientCollection = mailRecipientCollection;
            return this;
        }

        public UserMailDeliveryTaskBuilder deliveryChannel(MailDeliveryChannel deliveryChannel) {
            Assert.notNull(deliveryChannel, "'deliveryChannel' must not be null");
            this.deliveryChannel = deliveryChannel;
            return this;
        }

        public UserMailDeliveryTaskBuilder scheduled(boolean scheduled) {
            this.scheduled = scheduled;
            return this;
        }

        public UserMailDeliveryTaskBuilder scheduledDate(DateTime scheduledDate) {
            this.scheduledDate = scheduledDate;
            return this;
        }

        public UserMailDeliveryTaskBuilder user(User user) {
            Assert.notNull(user, "'user' must not be null");
            this.user = user;
            return this;
        }

        public UserMailDeliveryTaskBuilder parentUserId(long parentUserId) {
            this.parentUserId = parentUserId;
            return this;
        }

        public UserMailDeliveryTaskBuilder parentUsername(String parentUsername) {
            this.parentUsername = parentUsername;
            return this;
        }

        public UserMailDeliveryTask build() {
            return new UserMailDeliveryTask(this);
        }


    }
}
