package cn.tendata.mdcs.data.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import cn.tendata.mdcs.data.jackson.DataView;

@Embeddable
public class MailTemplate implements Serializable{

    private static final long serialVersionUID = 1L;

    @JsonView(DataView.Basic.class)
    private String subject;
    @JsonView(DataView.Basic.class)
    private String body;
    @JsonView(DataView.Basic.class)
    private boolean isHtml;
    
    public MailTemplate(){}
    
    public MailTemplate(String subject, String body, boolean isHtml) {
        this.subject = subject;
        this.body = body;
        this.isHtml = isHtml;
    }

    @NotNull
    @Size(min=1, max=255)
    @Column(name="subject", length=255, nullable=false)
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @JsonIgnore
    @NotEmpty
    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name="body", nullable=false)
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    
    @Column(name="is_html", nullable=false)
    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean isHtml) {
        this.isHtml = isHtml;
    }
}
