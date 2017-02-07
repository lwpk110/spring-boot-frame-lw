package cn.tendata.mdcs.mail.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import cn.tendata.mdcs.data.elasticsearch.repository.MailDeliveryTaskReportRepository;
import cn.tendata.mdcs.data.elasticsearch.repository.MailRecipientActionRepository;
import cn.tendata.mdcs.mail.aspect.MailDeliveryTaskReportAspect;
import cn.tendata.mdcs.mail.core.MailDeliveryTaskReportManager;
import cn.tendata.mdcs.mail.core.elasticsearch.ElasticsearchMailDeliveryTaskReportManager;

@Configuration
@EnableAspectJAutoProxy
@EnableAsync
public class MailDeliveryAspectConfig {

    public static final String TIME_TO_LIVE_IN_SECONDS = "mail.delivery.task.report.timeToLiveInSeconds";
    public static final String MAX_TIME_TO_SYNC_IN_DAYS = "mail.delivery.task.report.maxTimeToSyncInDays";

    @Autowired Environment env;

    @Lazy
    @Bean
    public MailDeliveryTaskReportManager mailDeliveryTaskReportManager(MailDeliveryTaskReportRepository taskReportRepository,
            MailRecipientActionRepository recipientActionRepository){
        int timeToLiveInSeconds = env.getProperty(TIME_TO_LIVE_IN_SECONDS, Integer.class, 7200);
        int maxTimeToSyncInDays = env.getProperty(MAX_TIME_TO_SYNC_IN_DAYS, Integer.class, 30);
        ElasticsearchMailDeliveryTaskReportManager taskReportManager =
                new ElasticsearchMailDeliveryTaskReportManager(taskReportRepository, recipientActionRepository);
        taskReportManager.setTimeToLiveInMillis(timeToLiveInSeconds * 1000L);
        taskReportManager.setMaxTimeToSyncInMillis(maxTimeToSyncInDays * 24 * 60 * 60 * 1000L);
        return taskReportManager;
    }

    @Lazy
    @Bean
    public MailDeliveryTaskReportAspect mailDeliveryTaskReportAspect(){
        return new MailDeliveryTaskReportAspect();
    }
}
