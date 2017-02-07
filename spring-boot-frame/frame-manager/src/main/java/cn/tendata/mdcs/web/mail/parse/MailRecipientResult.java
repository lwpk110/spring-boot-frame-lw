package cn.tendata.mdcs.web.mail.parse;

import cn.tendata.mdcs.data.domain.MailRecipient;
import cn.tendata.mdcs.data.domain.MailRecipientCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class MailRecipientResult implements MailRecipientCollectionEx {
    private Collection<MailRecipientRecord> invalidRecords;
    private Set<MailRecipientRecord> validRecords;
    private  Collection<MailRecipientRecord> notExistRecords;
    private int totality;
    private MailRecipientRecordValidator validator;
    private MailRecipientRecordHandler handler;

    public MailRecipientResult() {
        this.validator = new DefaultMailRecipientRecordValidator();
        this.handler = new EmptyNameMailRecipientRecordHandler();
        this.invalidRecords = new ArrayList<>();
        this.validRecords = new HashSet<>();
        this.notExistRecords=new ArrayList<>();
    }

    public void add(MailRecipientRecord record) {
        if (validator.validate(record)) {
            validRecords.add(handler.handler(record));
        } else {
            invalidRecords.add(record);
        }
        totality++;
    }

    @Override
    public MailRecipientCollection toMailRecipientCollection() {
        Collection<MailRecipient> mailRecipients = new ArrayList<>();
        for (MailRecipientRecord record : validRecords) {
            MailRecipient mailRecipient = new MailRecipient(record.getFullName(), record.getEmail());
            mailRecipients.add(mailRecipient);
        }
        return new MailRecipientCollection(mailRecipients);
    }

    public int getTotality() {
        return totality;
    }

    public Set<MailRecipientRecord> getValidRecords() {
        return validRecords;
    }

    public void setValidRecords(Set<MailRecipientRecord> validRecords) {
        this.validRecords = validRecords;
    }

    public Collection<MailRecipientRecord> getInvalidRecords() {
        return invalidRecords;
    }

    public Collection<MailRecipientRecord> getNotExistRecords() {
        return notExistRecords;
    }

    public void setNotExistRecords(Collection<MailRecipientRecord> notExistRecords) {
        this.notExistRecords = notExistRecords;
    }

    public void setValidator(MailRecipientRecordValidator validator) {
        this.validator = validator;
    }

    public void setHandler(MailRecipientRecordHandler handler) {
        this.handler = handler;
    }
}
