package cn.tendata.ftp.webpower.util;

import cn.tendata.mdcs.mail.MailRecipientAction;

/**
 * Created by ernest on 2016/9/1.
 */
public class WebpowerChannelActionStatusTransUtil {
    /**
     * webpower 邮件状态字典 v1.0
     */
    /*
    public static final String SENT_SUCCESS = "SENT";
    public static final String OPEN = "OPEN";
    public static final String CLICK = "CLICK";
    public static final String UN_SUBSCRIBE = "UNSUBSCRIBE";
    public static final String SOFT_BOUNCE_1X = "SOFTBOUNCE 1X";
    public static final String SOFT_BOUNCE_2X = "SOFTBOUNCE 2x";
    public static final String SOFT_BOUNCE_FINAL = "SOFTBOUNCE FINAL";
    public static final String HARD_BOUNCE_FINAL = "HARDBOUNCE FINAL";
    public static final String SPAM_COMPLAINT = "SPAMCOMPLAINT";
    */
    /**
     * webpower 邮件状态字典 v1.1
     */
    public static final String SENT_SUCCESS = "SENDOUT";
    public static final String OPEN = "OPEN";
    public static final String CLICK = "CLICK";
    public static final String UN_SUBSCRIBE = "UNSUBSCRIBER";
    public static final String SOFT_BOUNCE_1X = "SOFTBOUNCE1X";
    public static final String SOFT_BOUNCE_2X = "SOFTBOUNCE2X";
    public static final String SOFT_BOUNCE_FINAL = "SOFTBOUNCE3X";
    public static final String HARD_BOUNCE_FINAL = "HARDBOUNCE3X";
    public static final String SPAM_COMPLAINT = "SPAMCOMPLAINT";

    /**
     * 状态转换
     */
    public static MailRecipientAction.MailRecipientActionStatus transToCommonStatus(String status) {
        final String status_up = status.toUpperCase();

        switch (status_up) {
            case SENT_SUCCESS:
                return MailRecipientAction.MailRecipientActionStatus.SENT_SUCCESS;
            case OPEN:
                return MailRecipientAction.MailRecipientActionStatus.OPEN;
            case CLICK:
                return MailRecipientAction.MailRecipientActionStatus.CLICK;
            case SOFT_BOUNCE_1X:
                return MailRecipientAction.MailRecipientActionStatus.SOFT_BOUNCE;
            case SOFT_BOUNCE_2X:
                return MailRecipientAction.MailRecipientActionStatus.SOFT_BOUNCE;
            case SOFT_BOUNCE_FINAL:
                return MailRecipientAction.MailRecipientActionStatus.SOFT_BOUNCE;
            case HARD_BOUNCE_FINAL:
                return MailRecipientAction.MailRecipientActionStatus.HARD_BOUNCE;
            case UN_SUBSCRIBE:
                return MailRecipientAction.MailRecipientActionStatus.UNSUBSCRIBE;
            case SPAM_COMPLAINT:
                return MailRecipientAction.MailRecipientActionStatus.SPAM_COMPLAINT;
            default:
                return null;
        }
    }
}
