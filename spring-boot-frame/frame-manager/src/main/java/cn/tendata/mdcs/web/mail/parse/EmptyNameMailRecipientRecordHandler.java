package cn.tendata.mdcs.web.mail.parse;

import org.apache.commons.lang3.StringUtils;

public class EmptyNameMailRecipientRecordHandler implements MailRecipientRecordHandler {
    @Override
    public MailRecipientRecord handler(MailRecipientRecord record) {
        long recordNumber = record.getRecordNumber();
        String email = record.getEmail();
        String name = record.getFullName();
        if (StringUtils.isEmpty(name)) {
            name = email.substring(0, email.indexOf("@"));
        }
        return new MailRecipientRecord(recordNumber, name, email);
    }
}
