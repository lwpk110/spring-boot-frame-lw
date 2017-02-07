package cn.tendata.mdcs.data.repository;

import java.util.List;

import cn.tendata.mdcs.data.domain.MailDeliveryChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.tendata.mdcs.data.domain.MailAgentDomain;

/**
 * Created by jeashi on 2016/6/23.
 */
public interface MailAgentDomainRepository extends PagingAndSortingRepository<MailAgentDomain, Integer> {
    
    List<MailAgentDomain> findAllByChannelIdAndDisabledOrderByUseCount(Integer channel_Id,boolean disabled);

    Page<MailAgentDomain> findAllByMailAgent(String mailAgent, Pageable pageable);
}
