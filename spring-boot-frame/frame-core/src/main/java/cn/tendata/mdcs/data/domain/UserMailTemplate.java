package cn.tendata.mdcs.data.domain;

import cn.tendata.mdcs.data.jackson.DataView;
import cn.tendata.mdcs.util.StatusEnum;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class UserMailTemplate extends AbstractEntityAuditable<Long> {

    private static final long serialVersionUID = 1L;

    @JsonView(DataView.Basic.class)
    private String name;
    @JsonView(DataView.Basic.class)
    private MailTemplate template;
    @JsonView(DataView.Basic.class)
    private long useCount;

    private User user;

    private long version;

    @JsonView(DataView.Basic.class)
    private int approveStatus;

    @JsonView(DataView.Basic.class)
    private String approveStatusName;

    @Id
    @GeneratedValue
    public Long getId() {
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

    @NotNull
    @Valid
    @Embedded
    public MailTemplate getTemplate() {
        return template;
    }

    public void setTemplate(MailTemplate template) {
        this.template = template;
    }

    @Column(name = "use_count", nullable = false)
    public long getUseCount() {
        return useCount;
    }

    public void setUseCount(long useCount) {
        this.useCount = useCount;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
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

    public void increaseUseCount() {
        this.useCount += 1;
    }

    @Column(name = "approve_status")
    public int getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(int approveStatus) {
        this.approveStatus = approveStatus;
        if (null != StatusEnum.getStatusNameByStatus(this.approveStatus)) {
            this.approveStatusName = StatusEnum.getStatusNameByStatus(this.approveStatus).getName();
        }
    }

    @Transient
    public String getApproveStatusName() {
        return approveStatusName;
    }
}
