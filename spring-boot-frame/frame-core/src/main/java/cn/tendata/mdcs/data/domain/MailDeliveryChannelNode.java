package cn.tendata.mdcs.data.domain;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cn.tendata.mdcs.data.jpa.converter.JsonAttributeConverter;

@Entity
public class MailDeliveryChannelNode extends AbstractEntityAuditable<Integer> {

    private static final long serialVersionUID = 1L;

    private String name;
    private String serverKey;
    private Map<String, Object> configProps;
    private boolean disabled;

    private MailDeliveryChannel channel;

    private long version;

    private boolean needCampaigns;

    @Id
    @GeneratedValue
    public Integer getId() {
        return super.getId();
    }

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "name", length = 50, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "server_key", length = 50, nullable = false)
    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "config_props", length = 2000, nullable = false)
    @Convert(converter = JsonAttributeConverter.class)
    public Map<String, Object> getConfigProps() {
        return configProps;
    }

    public void setConfigProps(Map<String, Object> configProps) {
        this.configProps = configProps;
    }

    @Column(name = "disabled", nullable = false)
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "channel_id", nullable = false)
    public MailDeliveryChannel getChannel() {
        return channel;
    }

    public void setChannel(MailDeliveryChannel channel) {
        this.channel = channel;
    }

    @Version
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Column(name = "need_campaigns")
    public boolean isNeedCampaigns() {
        return needCampaigns;
    }

    public void setNeedCampaigns(boolean needCampaigns) {
        this.needCampaigns = needCampaigns;
    }
}
