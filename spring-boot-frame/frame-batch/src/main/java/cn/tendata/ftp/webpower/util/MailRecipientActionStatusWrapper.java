package cn.tendata.ftp.webpower.util;

import cn.tendata.mdcs.mail.MailRecipientAction;
import java.util.Arrays;

/**
 * Created by ernest on 2016/10/13.
 */
public class MailRecipientActionStatusWrapper {
    private int index;
    private MailRecipientAction.MailRecipientActionStatus actionStatus;

    public MailRecipientActionStatusWrapper(MailRecipientAction.MailRecipientActionStatus[] resolveArr,
            MailRecipientAction.MailRecipientActionStatus actionStatus) {
        this.actionStatus = actionStatus;
        this.index = getIndex(resolveArr, actionStatus);
    }

    private int getIndex(MailRecipientAction.MailRecipientActionStatus[] resolveArr,
            MailRecipientAction.MailRecipientActionStatus actionStatus) {
        return Arrays.binarySearch(resolveArr, actionStatus);
    }

    public MailRecipientAction.MailRecipientActionStatus getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(MailRecipientAction.MailRecipientActionStatus actionStatus) {
        this.actionStatus = actionStatus;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
