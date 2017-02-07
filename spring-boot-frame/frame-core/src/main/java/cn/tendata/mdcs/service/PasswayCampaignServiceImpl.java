package cn.tendata.mdcs.service;

import cn.tendata.mdcs.data.domain.PasswayCampaign;
import cn.tendata.mdcs.data.repository.PassWayCampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jeashi on 2016/8/30.
 */
public class PasswayCampaignServiceImpl extends EntityServiceSupport<PasswayCampaign, Long, PassWayCampaignRepository> implements PasswayCampaignService {

    @Autowired
    protected PasswayCampaignServiceImpl(PassWayCampaignRepository repository) {
        super(repository);
    }

    @Transactional(readOnly = true)
    public Page<PasswayCampaign> findAllByDeletedAndDisabled(Boolean deleted, Boolean disabled, Pageable pageable) {
        return getRepository().findAllByDeletedAndDisabled(deleted, disabled, pageable);
    }

}