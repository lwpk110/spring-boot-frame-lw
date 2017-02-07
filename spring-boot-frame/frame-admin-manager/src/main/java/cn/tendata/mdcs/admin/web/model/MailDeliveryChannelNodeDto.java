package cn.tendata.mdcs.admin.web.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MailDeliveryChannelNodeDto {

    private String name;
    private String serverKey;
    private String configProps;
    private boolean needCampaigns;

    @NotNull
    @Size(min = 1, max = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Size(min = 1, max = 50)
    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    @NotNull
    @Size(min = 1, max = 2000)
    public String getConfigProps() {
        return configProps;
    }

    public void setConfigProps(String configProps) {
        this.configProps = configProps;
    }

    @NotNull
    public boolean isNeedCampaigns() {
        return needCampaigns;
    }

    public void setNeedCampaigns(boolean needCampaigns) {
        this.needCampaigns = needCampaigns;
    }
}
