package cn.tendata.mdcs.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.tendata.mdcs.data.domain.MailDeliveryChannel;

public interface MailDeliveryChannelService extends EntityService<MailDeliveryChannel, Integer> {

    List<MailDeliveryChannel> getAvailableChannels();
    
    Page<MailDeliveryChannel> getAll(Pageable pageable);
    
    Page<MailDeliveryChannel> getAll(String name, Pageable pageable);
}
