package cn.tendata.mdcs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tendata.mdcs.data.domain.MailDeliveryChannel;
import cn.tendata.mdcs.data.domain.MailDeliveryChannelNode;
import cn.tendata.mdcs.data.repository.MailDeliveryChannelNodeRepository;

@Service
public class MailDeliveryChannelNodeServiceImpl extends EntityServiceSupport<MailDeliveryChannelNode, Integer, MailDeliveryChannelNodeRepository>
    implements MailDeliveryChannelNodeService {

    @Autowired
    protected MailDeliveryChannelNodeServiceImpl(MailDeliveryChannelNodeRepository repository) {
        super(repository);
    }

    @Transactional(readOnly=true)
    public List<MailDeliveryChannelNode> getAll(MailDeliveryChannel channel) {
        return getRepository().findAllByChannel(channel);
    }

    @Transactional(readOnly=true)
    public Page<MailDeliveryChannelNode> getAll(MailDeliveryChannel channel, Pageable pageable) {
        return getRepository().findAllByChannel(channel, pageable);
    }

}
