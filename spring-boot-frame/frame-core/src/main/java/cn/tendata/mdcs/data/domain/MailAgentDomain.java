package cn.tendata.mdcs.data.domain;

import javax.persistence.*;

/**
 * Created by jeashi on 2016/6/23.
 */
@Entity
public class MailAgentDomain extends AbstractEntityAuditable<Integer> {

    private static final long serialVersionUID = 1L;

    private boolean disabled;
    private String mailAgent;
    private String domainInfo;
    private Integer useCount;
    private MailDeliveryChannel channel;

    @Id
    @GeneratedValue
    public Integer getId() {
        return super.getId();
    }

    @Column(name = "disabled")
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Column(name = "mail_agent")
    public String getMailAgent() {
        return mailAgent;
    }

    public void setMailAgent(String mailAgent) {
        this.mailAgent = mailAgent;
    }

    @Column(name = "domain_info")
    public String getDomainInfo() {
        return domainInfo;
    }

    public void setDomainInfo(String domainInfo) {
        this.domainInfo = domainInfo;
    }

    @Column(name = "use_count")
    public Integer getUseCount() {
        return useCount;
    }

    public void setUseCount(Integer useCount) {
        this.useCount = useCount;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "channel_id", nullable = false)
    public MailDeliveryChannel getChannel() {
        return channel;
    }

    public void setChannel(MailDeliveryChannel channel) {
        this.channel = channel;
    }
}
