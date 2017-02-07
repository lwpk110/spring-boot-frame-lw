package cn.tendata.mdcs.data.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.Valid;

@Entity
public class UserMailDeliverySettings extends AbstractEntityAuditable<Long>{

    private static final long serialVersionUID = 1L;

    private MailDeliverySettings deliverySettings;
    private boolean checked;
    
    private User user;
    
    private long version;

    @Id
    @GeneratedValue
    public Long getId() {
        return super.getId();
    }

    @Valid
    @Embedded
    public MailDeliverySettings getDeliverySettings() {
        return deliverySettings;
    }

    public void setDeliverySettings(MailDeliverySettings deliverySettings) {
        this.deliverySettings = deliverySettings;
    }

    @Column(name="checked", nullable=false)
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="user_id", nullable=false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Version
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
