package cn.tendata.mdcs.mail.config;

import cn.tendata.mdcs.data.repository.MailDeliveryChannelRepository;
import cn.tendata.mdcs.data.repository.PassWayCampaignRepository;
import cn.tendata.mdcs.data.repository.UserMailDeliveryTaskRepository;
import cn.tendata.mdcs.mail.context.MailDeliveryTaskCompletedNotifyListener;
import cn.tendata.mdcs.mail.context.MailDeliveryTaskServiceExceptionNotifyListener;
import cn.tendata.mdcs.mail.core.DefaultMailDeliveryGateway;
import cn.tendata.mdcs.mail.core.DefaultMailDeliveryTaskDispatcher;
import cn.tendata.mdcs.mail.core.DefaultMailDeliveryTaskProcessor;
import cn.tendata.mdcs.mail.core.MailDeliveryChannelManager;
import cn.tendata.mdcs.mail.core.MailDeliveryChannelNodeChooserFactory;
import cn.tendata.mdcs.mail.core.MailDeliveryGateway;
import cn.tendata.mdcs.mail.core.MailDeliveryServerProxyManager;
import cn.tendata.mdcs.mail.core.MailDeliveryTaskDispatcher;
import cn.tendata.mdcs.mail.core.MailDeliveryTaskProcessor;
import cn.tendata.mdcs.mail.core.SmartMailDeliveryChannelManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@Import(MailDeliveryServerProxyConfig.class)
public class MailDeliveryGatewayConfig {

    @Bean
    public MailDeliveryGateway mailDeliveryGateway(MailDeliveryTaskDispatcher taskDispatcher,
            MailDeliveryServerProxyManager serverProxyManager) {
        return new DefaultMailDeliveryGateway(taskDispatcher, serverProxyManager);
    }

    @Bean
    public TaskExecutor mailDeliveryTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setQueueCapacity(30);
        return taskExecutor;
    }

    @Bean
    public MailDeliveryTaskDispatcher mailDeliveryTaskDispatcher(MailDeliveryTaskProcessor taskProcessor) {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setQueueCapacity(30);
        return new DefaultMailDeliveryTaskDispatcher(taskProcessor, mailDeliveryTaskExecutor());
    }

    @Bean
    public MailDeliveryTaskProcessor mailDeliveryTaskProcessor(MailDeliveryChannelManager channelManager,
            MailDeliveryServerProxyManager serverProxyManager, PassWayCampaignRepository passWayCampaignKeyRepository) {
        return new DefaultMailDeliveryTaskProcessor(channelManager, serverProxyManager, passWayCampaignKeyRepository);
    }

    @Bean
    public MailDeliveryChannelNodeChooserFactory mailDeliveryChannelNodeChooserFactory(
            MailDeliveryChannelRepository channelRepository) {
        return new MailDeliveryChannelNodeChooserFactory(channelRepository);
    }

    @Bean
    public MailDeliveryChannelManager mailDeliveryChannelManager(
            MailDeliveryChannelNodeChooserFactory channelNodeChooserFactory) {
        return new SmartMailDeliveryChannelManager(channelNodeChooserFactory);
    }

    @Bean
    public MailDeliveryTaskCompletedNotifyListener mailDeliveryTaskCompletedNotifyListener(
            UserMailDeliveryTaskRepository repository, PassWayCampaignRepository passWayCampaignRepository) {
        return new MailDeliveryTaskCompletedNotifyListener(repository, passWayCampaignRepository);
    }

    @Bean
    public MailDeliveryTaskServiceExceptionNotifyListener mailDeliveryTaskServiceExceptionNotifyListener(
            UserMailDeliveryTaskRepository repository) {
        return new MailDeliveryTaskServiceExceptionNotifyListener(repository);
    }
}
