package cn.tendata.mdcs.service;

import cn.tendata.mdcs.data.domain.MailAgentDomain;
import cn.tendata.mdcs.data.domain.PasswayCampaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by jeashi on 2016/8/30.
 */
public interface PasswayCampaignService {
    public Page<PasswayCampaign> findAllByDeletedAndDisabled(Boolean deleted, Boolean disabled, Pageable pageable);
}
