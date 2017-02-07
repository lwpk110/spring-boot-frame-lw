package cn.tendata.mdcs.service;


import cn.tendata.mdcs.data.domain.MailDeliveryTaskScheduleConfig;

import java.util.List;

/**
 * Created by ernest on 2016/8/1.
 */
public interface MailChannelNodeConfigService extends EntityService<MailDeliveryTaskScheduleConfig, Integer> {
    List<MailDeliveryTaskScheduleConfig> getAll(int status);
}
