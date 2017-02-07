package cn.tendata.mdcs.mail.context;

import cn.tendata.mdcs.data.domain.MailDeliveryChannelNode;
import cn.tendata.mdcs.data.domain.PasswayCampaign;
import cn.tendata.mdcs.mail.core.MailDeliveryTaskData;

public class MailDeliveryTaskCompletedEvent extends AbstractMailDeliveryTaskEvent {

    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final MailDeliveryChannelNode channelNode;
    private PasswayCampaign passwayCampaign;

    public static final int MAX_LIMIT_TOTAL_USE = 10000;

    public MailDeliveryTaskCompletedEvent(Object source, MailDeliveryTaskData taskData,
                                          MailDeliveryChannelNode channelNode, boolean success, PasswayCampaign passwayCampaign) {
        super(source, taskData);
        this.channelNode = channelNode;
        this.passwayCampaign = passwayCampaign;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public MailDeliveryChannelNode getChannelNode() {
        return channelNode;
    }

    public PasswayCampaign getPasswayCampaign() {
        return passwayCampaign;
    }
}
