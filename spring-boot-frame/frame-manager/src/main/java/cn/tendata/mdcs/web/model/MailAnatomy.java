package cn.tendata.mdcs.web.model;

import cn.tendata.mdcs.web.mail.parse.MailRecipientRecord;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeashi on 2016/11/21.
 */
public class MailAnatomy {
    public List<MailRecipientRecord> mailRecipientTotel;
    public List<MailRecipientRecord> mailRecipientAvailable;
    public List<MailRecipientRecord> mailRecipientDisable;

    public MailAnatomy() {
    }

    public MailAnatomy(List<MailRecipientRecord> mailRecipientTotel, List<MailRecipientRecord> mailRecipientAvailable, List<MailRecipientRecord> mailRecipientDisable) {
        this.mailRecipientTotel = mailRecipientTotel;
        this.mailRecipientAvailable = mailRecipientAvailable;
        this.mailRecipientDisable = mailRecipientDisable;
    }

    public MailAnatomy(List<MailRecipientRecord> mailRecipientTotel) {
        this.mailRecipientTotel = mailRecipientTotel;
        if (CollectionUtils.isNotEmpty(mailRecipientTotel)) {
            this.mailRecipientAvailable = new ArrayList(mailRecipientTotel.size());
            this.mailRecipientDisable = new ArrayList(mailRecipientTotel.size());
        }
    }

    public List<MailRecipientRecord> getMailRecipientTotel() {
        return mailRecipientTotel;
    }

    public List<MailRecipientRecord> getMailRecipientAvailable() {
        return mailRecipientAvailable;
    }

    public void setMailRecipientAvailable(List<MailRecipientRecord> mailRecipientAvailable) {
        this.mailRecipientAvailable = mailRecipientAvailable;
    }

    public List<MailRecipientRecord> getMailRecipientDisable() {
        return mailRecipientDisable;
    }

    public void setMailRecipientDisable(List<MailRecipientRecord> mailRecipientDisable) {
        this.mailRecipientDisable = mailRecipientDisable;
    }

}


