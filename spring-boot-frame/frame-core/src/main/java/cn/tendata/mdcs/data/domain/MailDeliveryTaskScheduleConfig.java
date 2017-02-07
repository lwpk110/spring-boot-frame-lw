package cn.tendata.mdcs.data.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by ernest on 2016/8/1.
 */
@Entity
public class MailDeliveryTaskScheduleConfig extends AbstractEntityAuditable<Integer> {

    private static final long serialVersionUID = 1L;

    private String timeArea;

    private Integer jobType;

    private String cronExpression;

    private int status;

    private boolean syncState;

    private long version;

    private MailDeliveryChannelNode mailDeliveryChannelNode;

    @Id
    @GeneratedValue
    public Integer getId() {
        return super.getId();
    }

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "time_area", length = 200, nullable = false)
    public String getTimeArea() {
        return timeArea;
    }

    public void setTimeArea(String timeArea) {
        this.timeArea = timeArea;
    }

    @NotNull
    @Column(name = "job_type", nullable = false)
    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "cron_expression", length = 200, nullable = false)
    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    @NotNull
    @Column(name = "status", nullable = false)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "sync_state", nullable = false)
    public boolean isSyncState() {
        return syncState;
    }

    public void setSyncState(boolean syncState) {
        this.syncState = syncState;
    }

    @OneToOne(optional = false)
    @JoinColumn(name = "channel_node_id", nullable = false)
    public MailDeliveryChannelNode getMailDeliveryChannelNode() {
        return mailDeliveryChannelNode;
    }

    public void setMailDeliveryChannelNode(MailDeliveryChannelNode mailDeliveryChannelNode) {
        this.mailDeliveryChannelNode = mailDeliveryChannelNode;
    }

    @Version
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
