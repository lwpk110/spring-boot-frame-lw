package cn.tendata.mdcs.data.elasticsearch.domain;

import cn.tendata.mdcs.data.domain.AbstractEntity;
import cn.tendata.mdcs.mail.MailRecipientAction.MailRecipientActionStatus;
import javax.persistence.Transient;
import org.joda.time.DateTime;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Parent;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = MailDeliveryTaskReportDocument.INDEX, type = MailRecipientActionDocument.TYPE)
@Setting(settingPath = "/elasticsearch/settings/mailRecipientAction.json")
public class MailRecipientActionDocument extends AbstractEntity<String> {

    private static final long serialVersionUID = 1L;

    public static final String TYPE = "mail_recipient_action";

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String email;
    @Field(type = FieldType.Date, index = FieldIndex.not_analyzed)
    private DateTime actionDate;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private MailRecipientActionStatus actionStatus;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String description;
    @Field(type = FieldType.Ip, index = FieldIndex.not_analyzed)
    private String ip;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String address;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String browser;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String os;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    @Parent(type = MailDeliveryTaskReportDocument.TYPE)
    private String taskId;
    @Field(type = FieldType.String, index = FieldIndex.analyzed, store = true)
    private String taskName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DateTime getActionDate() {
        return actionDate;
    }

    public void setActionDate(DateTime actionDate) {
        this.actionDate = actionDate;
    }

    public MailRecipientActionStatus getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(MailRecipientActionStatus actionStatus) {
        this.actionStatus = actionStatus;
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
