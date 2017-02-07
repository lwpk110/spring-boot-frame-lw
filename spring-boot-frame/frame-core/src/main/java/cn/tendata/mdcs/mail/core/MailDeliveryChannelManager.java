package cn.tendata.mdcs.mail.core;

import cn.tendata.mdcs.data.domain.MailDeliveryChannelNode;

public abstract class MailDeliveryChannelManager {

    public abstract MailDeliveryChannelNode retrieve(int channelId);
    
    public abstract void release(MailDeliveryChannelNode channelNode);
}
