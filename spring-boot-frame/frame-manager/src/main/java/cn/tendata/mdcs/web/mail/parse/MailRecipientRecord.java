package cn.tendata.mdcs.web.mail.parse;

import cn.tendata.mdcs.web.jackson.NullStringSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class MailRecipientRecord implements Comparable<MailRecipientRecord>{
    private final long recordNumber;
    private final String fullName;
    private final String email;

    public MailRecipientRecord(@JsonProperty("recordNumber") long recordNumber, @JsonProperty("fullName") String fullName, @JsonProperty("email") String email) {
        this.recordNumber = recordNumber;
        this.fullName = fullName;
        this.email = email;
    }

    public long getRecordNumber() {
        return recordNumber;
    }

    @JsonSerialize(nullsUsing = NullStringSerializer.class)
    public String getFullName() {
        return fullName;
    }

    @Email
    @NotBlank
    @JsonSerialize(nullsUsing = NullStringSerializer.class)
    public String getEmail() {
        return email;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(email)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        final MailRecipientRecord mailRecipientRecord = (MailRecipientRecord) obj;
        return new EqualsBuilder()
                .append(email, mailRecipientRecord.getEmail())
                .isEquals();
    }

    @Override
    public int compareTo(MailRecipientRecord o) {
        return (int) (this.getRecordNumber()-o.getRecordNumber());
    }
}
