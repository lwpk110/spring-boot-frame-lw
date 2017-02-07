package cn.tendata.mdcs.service;

import cn.tendata.mdcs.data.domain.MailAgentDomain;
import cn.tendata.mdcs.data.domain.MailDeliveryChannel;
import cn.tendata.mdcs.data.repository.MailAgentDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by jeashi on 2016/6/23.
 */
@Service
public class MailAgentDomainServiceImpl extends EntityServiceSupport<MailAgentDomain, Integer, MailAgentDomainRepository> implements MailAgentDomainService{
    
    @Autowired
    protected MailAgentDomainServiceImpl(MailAgentDomainRepository repository) {
        super(repository);
    }

    @Transactional(readOnly=true)
    public List<MailAgentDomain> findAllByChannelAndDisabledOrderByUseCount(Boolean  bl, MailDeliveryChannel channel) {
        return getRepository().findAllByChannelIdAndDisabledOrderByUseCount(channel.getId(),bl);
    }

    @Transactional(isolation= Isolation.SERIALIZABLE)
    public void addUseCount(MailAgentDomain mailAgentDomain) {
        mailAgentDomain.setUseCount(mailAgentDomain.getUseCount()+1);
        getRepository().save(mailAgentDomain);
    }

    @Transactional(readOnly=true)
    public Page<MailAgentDomain> getAll(String mailAgent, Pageable pageable) {
        if(!StringUtils.isEmpty(mailAgent)){
            return getRepository().findAllByMailAgent(mailAgent, pageable);
        }
        return getRepository().findAll(pageable);
    }
}
