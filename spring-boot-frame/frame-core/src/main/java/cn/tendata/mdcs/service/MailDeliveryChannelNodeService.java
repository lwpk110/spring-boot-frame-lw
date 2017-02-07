package cn.tendata.mdcs.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.tendata.mdcs.data.domain.MailDeliveryChannel;
import cn.tendata.mdcs.data.domain.MailDeliveryChannelNode;

public interface MailDeliveryChannelNodeService extends EntityService<MailDeliveryChannelNode, Integer> {

    List<MailDeliveryChannelNode> getAll(MailDeliveryChannel channel);
    
    Page<MailDeliveryChannelNode> getAll(MailDeliveryChannel channel, Pageable pageable);
}
