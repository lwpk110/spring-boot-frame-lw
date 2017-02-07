package cn.tendata.mdcs.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import cn.tendata.mdcs.data.jackson.DataView;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MailDeliveryChannel extends AbstractEntityAuditable<Integer> {

    private static final long serialVersionUID = 1L;

    @JsonView(DataView.Basic.class)
    private String name;
    private String description;
    @JsonView(DataView.Basic.class)
    private int fee;
    @JsonView(DataView.Basic.class)
    private int maxNumLimit;
    private boolean disabled;
    private int sequence;
    
    @JsonIgnore
    private Collection<MailDeliveryChannelNode> channelNodes = Collections.emptyList();

    private long version;

    @Id
    @GeneratedValue
    public Integer getId() {
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

    @Size(max = 255)
    @Column(name = "description", length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Min(0)
    @Column(name = "fee", nullable = false)
    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    @Column(name = "max_num_limit", nullable = false)
    public int getMaxNumLimit() {
        return maxNumLimit;
    }

    public void setMaxNumLimit(int maxNumLimit) {
        this.maxNumLimit = maxNumLimit;
    }

    @Column(name = "disabled", nullable = false)
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    
    @Column(name = "sequence", nullable = false)
    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @OneToMany(mappedBy = "channel", fetch = FetchType.LAZY)
    public Collection<MailDeliveryChannelNode> getChannelNodes() {
        return channelNodes;
    }

    public void setChannelNodes(Collection<MailDeliveryChannelNode> channelNodes) {
        this.channelNodes = channelNodes;
    }

    @Version
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
