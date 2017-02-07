package cn.tendata.mdcs.mail;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MailLink implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal allClickPercent;
    private int allClickTimes;
    private String href;
    private BigDecimal mailClickPercent;
    private int mailClickTimes;
    private String outerHtml;
    
    public BigDecimal getAllClickPercent() {
        return allClickPercent;
    }
    
    public void setAllClickPercent(BigDecimal allClickPercent) {
        this.allClickPercent = allClickPercent;
    }
    
    public int getAllClickTimes() {
        return allClickTimes;
    }
    
    public void setAllClickTimes(int allClickTimes) {
        this.allClickTimes = allClickTimes;
    }
    
    public String getHref() {
        return href;
    }
    
    public void setHref(String href) {
        this.href = href;
    }
    
    public BigDecimal getMailClickPercent() {
        return mailClickPercent;
    }
    
    public void setMailClickPercent(BigDecimal mailClickPercent) {
        this.mailClickPercent = mailClickPercent;
    }
    
    public int getMailClickTimes() {
        return mailClickTimes;
    }
    
    public void setMailClickTimes(int mailClickTimes) {
        this.mailClickTimes = mailClickTimes;
    }
    
    public String getOuterHtml() {
        return outerHtml;
    }
    
    public void setOuterHtml(String outerHtml) {
        this.outerHtml = outerHtml;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
          return false;
        }
        final MailLink rhs = (MailLink) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(href, rhs.href)
                .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(href)
                .toHashCode();
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(href)
                .toString();
    }
}
