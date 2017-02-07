package cn.tendata.mdcs.mail.context;

import org.springframework.context.ApplicationEvent;

import cn.tendata.mdcs.mail.core.MailDeliveryTaskData;

public abstract class AbstractMailDeliveryTaskEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private final MailDeliveryTaskData taskData;
    
    public AbstractMailDeliveryTaskEvent(Object source, MailDeliveryTaskData taskData) {
        super(source);
        this.taskData = taskData;
    }

    public MailDeliveryTaskData getTaskData() {
        return taskData;
    }
}
