package cn.tendata.mdcs.data.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.Assert;

public class MailRecipient implements Serializable{

    private static final long serialVersionUID = 1L;

    private String fullName;
    private String email;
    
    protected MailRecipient(){}
    
    public MailRecipient(String email){
        this(null, email);
    }
    
    public MailRecipient(String fullName, String email) {
        Assert.notNull(email, "'email' must not be null");
        
        this.fullName = fullName;
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    @Email
    @NotEmpty
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
          return false;
        }
        final MailRecipient rhs = (MailRecipient) obj;
        return new EqualsBuilder()
                .append(email, rhs.email)
                .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(email)
                .toHashCode();
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(fullName)
                .append(email)
                .toString();
    }
}
