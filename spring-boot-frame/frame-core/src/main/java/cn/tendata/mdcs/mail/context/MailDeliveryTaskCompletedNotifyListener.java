package cn.tendata.mdcs.mail.context;

import cn.tendata.mdcs.data.domain.PasswayCampaign;
import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.data.domain.UserMailDeliveryTask.DeliveryStatus;
import cn.tendata.mdcs.data.repository.PassWayCampaignRepository;
import cn.tendata.mdcs.data.repository.UserMailDeliveryTaskRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public class MailDeliveryTaskCompletedNotifyListener implements ApplicationListener<MailDeliveryTaskCompletedEvent> {

    private final UserMailDeliveryTaskRepository repository;
    private final PassWayCampaignRepository passWayCampaignRepository;

    public MailDeliveryTaskCompletedNotifyListener(UserMailDeliveryTaskRepository repository,
            PassWayCampaignRepository passWayCampaignRepository) {
        this.repository = repository;
        this.passWayCampaignRepository = passWayCampaignRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public final void onApplicationEvent(final MailDeliveryTaskCompletedEvent event) {
        UserMailDeliveryTask task = repository.findOne(event.getTaskData().getId());
        if (event.isSuccess()) {
            task.setDeliveryStatus(DeliveryStatus.SUCCESS);
            // update passwayCamapign
            PasswayCampaign passwayCampaign = event.getPasswayCampaign();
            if (null != passwayCampaign) {

                task.setMailingId(event.getTaskData().getMailingId());
                task.setPasswayCampaignKey(event.getPasswayCampaign().getPasswayCampaignKey());

                passwayCampaign.setTotalUse(passwayCampaign.getTotalUse() + 1);
                if (passwayCampaign.getTotalUse() >= MailDeliveryTaskCompletedEvent.MAX_LIMIT_TOTAL_USE) {
                    passwayCampaign.setDisabled(true);
                }
                passWayCampaignRepository.save(event.getPasswayCampaign());
            }
        } else {
            task.setDeliveryStatus(DeliveryStatus.FAILED);
        }
        task.setDeliveryChannelNode(event.getChannelNode());
        repository.save(task);
    }
}
