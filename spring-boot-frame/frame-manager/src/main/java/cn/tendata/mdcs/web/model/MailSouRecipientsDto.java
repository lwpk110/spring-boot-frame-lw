package cn.tendata.mdcs.web.model;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import cn.tendata.mdcs.data.domain.MailRecipient;

public class MailSouRecipientsDto {

    @NotNull
    @Size(min = 1, max = 100)
    private String name;
    @NotNull
    @Size(min = 1, max = 5000)
    private List<MailRecipient> recipients;
    
    @Min(1)
    private long userId;
    @NotEmpty
    private String username;
    @NotEmpty
    private String token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MailRecipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<MailRecipient> recipients) {
        this.recipients = recipients;
    }
    
    public long getUserId() {
        return userId;
    }
    
    public void setUserId(long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
