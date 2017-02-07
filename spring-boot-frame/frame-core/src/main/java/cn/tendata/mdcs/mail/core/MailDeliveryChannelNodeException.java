package cn.tendata.mdcs.mail.core;

public class MailDeliveryChannelNodeException extends RuntimeException {

    private static final long serialVersionUID = 2185879687816062862L;

    public MailDeliveryChannelNodeException() {
        super();
    }

    public MailDeliveryChannelNodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailDeliveryChannelNodeException(String message) {
        super(message);
    }

    public MailDeliveryChannelNodeException(Throwable cause) {
        super(cause);
    }
}
