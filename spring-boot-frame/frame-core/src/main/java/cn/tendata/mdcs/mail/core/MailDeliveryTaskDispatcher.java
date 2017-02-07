package cn.tendata.mdcs.mail.core;

public interface MailDeliveryTaskDispatcher {

    void dispatch(MailDeliveryTaskData taskData);
}
