package cn.tendata.mdcs.mail.core;

import cn.tendata.mdcs.data.domain.MailDeliveryChannelNode;
import cn.tendata.mdcs.data.domain.PasswayCampaign;
import cn.tendata.mdcs.data.repository.PassWayCampaignRepository;
import cn.tendata.mdcs.mail.context.MailDeliveryTaskCompletedEvent;
import cn.tendata.mdcs.mail.context.MailDeliveryTaskServiceExceptionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.util.Assert;

public class DefaultMailDeliveryTaskProcessor implements MailDeliveryTaskProcessor, ApplicationEventPublisherAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMailDeliveryTaskDispatcher.class);

    private final MailDeliveryChannelManager channelManager;
    private final MailDeliveryServerProxyManager serverProxyManager;
    private ApplicationEventPublisher eventPublisher;
    private final PassWayCampaignRepository passWayCampaignKeyRepository;

    public DefaultMailDeliveryTaskProcessor(MailDeliveryChannelManager channelManager,
            MailDeliveryServerProxyManager serverProxyManager
            , PassWayCampaignRepository passWayCampaignKeyRepository) {
        Assert.notNull(channelManager, "'channelManager' must not be null");
        Assert.notNull(serverProxyManager, "'serverProxyManager' must not be null");
        this.channelManager = channelManager;
        this.serverProxyManager = serverProxyManager;
        this.passWayCampaignKeyRepository = passWayCampaignKeyRepository;
    }

    public void process(final MailDeliveryTaskData taskData) {
        MailDeliveryChannelNode channelNode = null;
        PasswayCampaign passwayCampaign = null;
        boolean success = false;
        try {
            channelNode = channelManager.retrieve(taskData.getChannelId());
            if (channelNode != null) {
                if (channelNode.isNeedCampaigns()) {
                    passwayCampaign = this.getValidCampaign();
                    taskData.setPasswayCampaign(passwayCampaign);
                }
                success = serverProxyManager.batchSend(taskData, channelNode);
                LOGGER.debug("Completed batch send, task id:{}, channel:{}, node:{}, status:{}.",
                        taskData.getId(), channelNode.getChannel().getName(), channelNode.getName(), success);
            } else {
                LOGGER.warn("mail delivery channel node is not available.");
            }
            final MailDeliveryTaskCompletedEvent event =
                    new MailDeliveryTaskCompletedEvent(this, taskData, channelNode, success,
                            taskData.getPasswayCampaign());
            eventPublisher.publishEvent(event);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            final MailDeliveryTaskServiceExceptionEvent event =
                    new MailDeliveryTaskServiceExceptionEvent(this, taskData, e);
            eventPublisher.publishEvent(event);
        } finally {
            if (channelNode != null) {
                channelManager.release(channelNode);
            }
        }
    }

    private PasswayCampaign getValidCampaign() {
        return passWayCampaignKeyRepository.findFirst1ByDeletedAndDisabledAndTotalUseLessThan(false, false,
                MailDeliveryTaskCompletedEvent.MAX_LIMIT_TOTAL_USE);
    }

    public void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }
}
