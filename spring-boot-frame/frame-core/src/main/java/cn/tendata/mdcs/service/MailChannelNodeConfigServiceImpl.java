package cn.tendata.mdcs.service;

import cn.tendata.mdcs.data.domain.MailDeliveryTaskScheduleConfig;
import cn.tendata.mdcs.data.repository.MailChannelNodeConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ernest on 2016/8/1.
 */
@Service
public class MailChannelNodeConfigServiceImpl extends EntityServiceSupport<MailDeliveryTaskScheduleConfig, Integer, MailChannelNodeConfigRepository> implements MailChannelNodeConfigService {

    @Autowired
    protected MailChannelNodeConfigServiceImpl(MailChannelNodeConfigRepository repository) {
        super(repository);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MailDeliveryTaskScheduleConfig> getAll(int status) {
        return this.getRepository().findAllByStatus(status);
    }
}


