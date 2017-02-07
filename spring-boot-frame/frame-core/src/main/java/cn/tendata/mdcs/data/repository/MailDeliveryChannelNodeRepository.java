package cn.tendata.mdcs.data.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.tendata.mdcs.data.domain.MailDeliveryChannel;
import cn.tendata.mdcs.data.domain.MailDeliveryChannelNode;

public interface MailDeliveryChannelNodeRepository extends PagingAndSortingRepository<MailDeliveryChannelNode, Integer> {

    List<MailDeliveryChannelNode> findAllByChannel(MailDeliveryChannel channel);
    
    Page<MailDeliveryChannelNode> findAllByChannel(MailDeliveryChannel channel, Pageable pageable);

    List<MailDeliveryChannelNode> findAll();
}
