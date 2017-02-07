package cn.tendata.mdcs.mail.context;

import org.springframework.context.ApplicationListener;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.data.domain.UserMailDeliveryTask.DeliveryStatus;
import cn.tendata.mdcs.data.repository.UserMailDeliveryTaskRepository;

public class MailDeliveryTaskServiceExceptionNotifyListener implements ApplicationListener<MailDeliveryTaskServiceExceptionEvent> {

    private final UserMailDeliveryTaskRepository repository;
    
    public MailDeliveryTaskServiceExceptionNotifyListener(UserMailDeliveryTaskRepository repository) {
        this.repository = repository;
    }
    
    @Transactional(isolation=Isolation.SERIALIZABLE)
    public void onApplicationEvent(MailDeliveryTaskServiceExceptionEvent event) {
        UserMailDeliveryTask task = repository.findOne(event.getTaskData().getId());
        task.setDeliveryStatus(DeliveryStatus.FAILED);
        repository.save(task);
    }
}
