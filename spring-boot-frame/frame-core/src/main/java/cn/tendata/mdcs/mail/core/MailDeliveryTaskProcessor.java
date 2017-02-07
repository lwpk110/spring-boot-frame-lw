package cn.tendata.mdcs.mail.core;

public interface MailDeliveryTaskProcessor {
    
    void process(MailDeliveryTaskData taskData);
}
