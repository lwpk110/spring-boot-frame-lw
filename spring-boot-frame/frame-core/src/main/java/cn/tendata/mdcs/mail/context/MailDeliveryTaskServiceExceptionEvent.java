package cn.tendata.mdcs.mail.context;

import cn.tendata.mdcs.mail.core.MailDeliveryTaskData;

public class MailDeliveryTaskServiceExceptionEvent extends AbstractMailDeliveryTaskEvent {

    private static final long serialVersionUID = 1L;

    private final Exception exception;
    
    public MailDeliveryTaskServiceExceptionEvent(Object source, MailDeliveryTaskData taskData, Exception exception) {
        super(source, taskData);
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
