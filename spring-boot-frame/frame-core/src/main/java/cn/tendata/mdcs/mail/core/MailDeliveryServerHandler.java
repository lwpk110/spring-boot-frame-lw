package cn.tendata.mdcs.mail.core;

public interface MailDeliveryServerHandler<T> {

    T handle(MailDeliveryServerRequest request);
}
