package cn.tendata.mdcs.data.domain;

import cn.tendata.mdcs.util.JsonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.UUID;

@Entity
public class UserMailRecipientGroup extends AbstractEntityAuditable<UUID> {
    
    private static final long serialVersionUID = 1L;

    public static class Sources {
        
        public static final String MAIL_SOU = "MAIL_SOU";
    }
    
    private String name;
    private String source;
    private MailRecipientCollection recipientCollection;
    private int disabledRecipientCount;
    private String disabledRecipientContent;
    private Collection<MailRecipient> disabledRecipients = null;

    private User user;

    public UserMailRecipientGroup() {}
    
    public UserMailRecipientGroup(String name, Collection<MailRecipient> recipients, User user) {
        this(name, null, recipients, user);
    }

    public UserMailRecipientGroup(String name, String source, Collection<MailRecipient> recipients, User user) {
        this(name, source, new MailRecipientCollection(recipients), user);
    }
    
    public UserMailRecipientGroup(String name, MailRecipientCollection recipientCollection, User user){
        this(name, null, recipientCollection, user);
    }
    
    public UserMailRecipientGroup(String name, String source, MailRecipientCollection recipientCollection, User user){
        this.name = name;
        this.source = source;
        this.user = user;
        this.recipientCollection = recipientCollection;
    }

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    public UUID getId() {
        return super.getId();
    }

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name", length = 100, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Size(max = 50)
    @Column(name = "source", length = 50)
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @NotNull
    @Valid
    @Embedded
    public MailRecipientCollection getRecipientCollection() {
        return recipientCollection;
    }

    public void setRecipientCollection(MailRecipientCollection recipientCollection) {
        this.recipientCollection = recipientCollection;
    }

    @Column(name = "disabled_recipient_count")
    public int getDisabledRecipientCount() {
        return disabledRecipientCount;
    }

    public void setDisabledRecipientCount(int disabledRecipientCount) {
        this.disabledRecipientCount = disabledRecipientCount;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "disabled_recipients")
    public String getDisabledRecipientContent() {
        return disabledRecipientContent;
    }

    public void setDisabledRecipientContent(String disabledRecipientContent) {
        this.disabledRecipientContent = disabledRecipientContent;
    }

    @JsonIgnore
    @Transient
    public Collection<MailRecipient> getDisabledRecipients() {
        return disabledRecipients;
    }

    public void setDisabledRecipients(Collection<MailRecipient> disabledRecipients) {
        this.disabledRecipients = JsonUtils.deserialize(disabledRecipientContent);
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
