package cn.tendata.mdcs.service;

import static cn.tendata.mdcs.data.repository.MailDeliveryChannelOrderSpecifiers.idDesc;
import static cn.tendata.mdcs.data.repository.MailDeliveryChannelOrderSpecifiers.sequenceDesc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.tendata.mdcs.data.domain.MailDeliveryChannel;
import cn.tendata.mdcs.data.repository.MailDeliveryChannelPredicates;
import cn.tendata.mdcs.data.repository.MailDeliveryChannelRepository;
import cn.tendata.mdcs.util.CollectionUtils;

@Service
public class MailDeliveryChannelServiceImpl extends EntityServiceSupport<MailDeliveryChannel, Integer, MailDeliveryChannelRepository>
    implements MailDeliveryChannelService {

    @Autowired
    protected MailDeliveryChannelServiceImpl(MailDeliveryChannelRepository repository) {
        super(repository);
    }

    @Transactional(readOnly=true)
    public List<MailDeliveryChannel> getAvailableChannels() {
        return CollectionUtils.toList(getRepository().findAll(MailDeliveryChannelPredicates.channels(false), sequenceDesc(), idDesc()));
    }
    
    @Transactional(readOnly=true)
    public Page<MailDeliveryChannel> getAll(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    @Transactional(readOnly=true)
    public Page<MailDeliveryChannel> getAll(String name, Pageable pageable) {
        if(StringUtils.hasText(name)){
            return getRepository().findAllByNameLike(name, pageable);
        }
        return getRepository().findAll(pageable);
    }
}
