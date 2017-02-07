package cn.tendata.mdcs.data.repository;

import cn.tendata.mdcs.data.domain.MailDeliveryTaskScheduleConfig;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by ernest on 2016/8/1.
 */
public interface MailChannelNodeConfigRepository extends PagingAndSortingRepository<MailDeliveryTaskScheduleConfig, Integer> {
    List<MailDeliveryTaskScheduleConfig> findAllByStatus(int status);
}
