package cn.tendata.mdcs.data.repository;

import cn.tendata.mdcs.data.domain.MailDeliveryChannel;
import cn.tendata.mdcs.data.domain.PasswayCampaign;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by jeashi on 2016/8/26.
 */
public interface PassWayCampaignRepository extends PagingAndSortingRepository<PasswayCampaign, Long> {

    PasswayCampaign findByDeletedAndDisabledAndUseDate(Boolean deleted, Boolean disabled, String useDate);

    PasswayCampaign findByDeletedAndDisabled(Boolean deleted, Boolean disabled);

    PasswayCampaign findByDeletedAndDisabledAndPasswayCampaignKey(Boolean deleted, Boolean disabled,
            String PasswayCampaignKey);

    Page<PasswayCampaign> findAllByDeletedAndDisabled(Boolean deleted, Boolean disabled, Pageable pageable);

    PasswayCampaign findFirst1ByDeletedAndDisabledAndTotalUseLessThan(Boolean deleted, Boolean disabled, int totalUse);
}
