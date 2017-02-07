package cn.tendata.mdcs.service;

import java.util.List;

import cn.tendata.mdcs.data.domain.MailDeliveryChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.tendata.mdcs.data.domain.MailAgentDomain;

/**
 * Created by jeashi on 2016/6/23.
 */
public interface MailAgentDomainService  extends EntityService<MailAgentDomain, Integer> {

    List<MailAgentDomain> findAllByChannelAndDisabledOrderByUseCount(Boolean  bl,MailDeliveryChannel channel);

    public Page<MailAgentDomain> getAll(String mailAgent, Pageable pageable);
}
