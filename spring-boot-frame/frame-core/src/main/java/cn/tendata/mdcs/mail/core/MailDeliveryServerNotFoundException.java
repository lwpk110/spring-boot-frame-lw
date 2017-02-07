package cn.tendata.mdcs.mail.core;

public class MailDeliveryServerNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 9101756562976634529L;

    public MailDeliveryServerNotFoundException() {
        super();
    }

    public MailDeliveryServerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailDeliveryServerNotFoundException(String message) {
        super(message);
    }

    public MailDeliveryServerNotFoundException(Throwable cause) {
        super(cause);
    }
}
