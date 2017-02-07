package cn.tendata.mdcs.mail.core;

import cn.tendata.mdcs.data.domain.MailDeliveryChannelNode;

public interface MailDeliveryChannelNodeChooser {

    MailDeliveryChannelNode choose();
    
    void restore(MailDeliveryChannelNode channelNode);
}
