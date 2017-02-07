package cn.tendata.mdcs.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by jeashi on 2016/8/26.
 */
@Entity
public class PasswayCampaign extends AbstractEntityAuditable<Long> {
    private static final long serialVersionUID = 1L;

    private boolean disabled; //是否禁用
    private String passwayCampaignKey;
    private boolean deleted; //删除标记
    private int totalUse; //使用总数
    private String useDate; //使用时间

    @Id
    @GeneratedValue
    public Long getId() {
        return super.getId();
    }

    @Column(name = "disabled")
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }


    @Column(name = "passway_campaign_key", length = 100, nullable = false)
    public String getPasswayCampaignKey() {
        return passwayCampaignKey;
    }

    public void setPasswayCampaignKey(String passwayCampaignKey) {
        this.passwayCampaignKey = passwayCampaignKey;
    }

    @Column(name = "deleted")
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Column(name = "total_use")
    public int getTotalUse() {
        return totalUse;
    }

    public void setTotalUse(int totalUse) {
        this.totalUse = totalUse;
    }

    @Column(name = "use_date")
    public String getUseDate() {
        return useDate;
    }

    public void setUseDate(String useDate) {
        this.useDate = useDate;
    }
}
