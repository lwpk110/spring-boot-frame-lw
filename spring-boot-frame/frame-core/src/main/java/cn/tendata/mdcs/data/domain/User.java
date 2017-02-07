package cn.tendata.mdcs.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import cn.tendata.mdcs.core.UserBalanceNotEnoughException;
import cn.tendata.mdcs.data.jackson.DataView;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User extends AbstractEntityAuditable<Long>{

    private static final long serialVersionUID = 1L;

    @JsonView(DataView.Basic.class)
    private String username;
    @JsonView(DataView.Login.class)
    private int loginCount;
    @JsonView(DataView.Login.class)
    private DateTime lastLoginAt;
    
    @JsonView(DataView.Account.class)
    private int balance;
    @JsonView(DataView.Account.class)
    private long totalDeposit;
    @JsonView(DataView.Account.class)
    private long totalCost;
    
    private long version;
    
    protected User() {}
    
    public User(long id, String username){
        Assert.isTrue(id > 0, "The value must be greater than zero");
        Assert.hasText(username, "'username' must not be empty");
        this.id = id;
        this.username = username;
    }
    
    @Id
    public Long getId() {
        return super.getId();
    }
    
    @NotNull
    @Size(min=1, max=255)
    @Column(name="username", length=255, nullable=false, unique=true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    @Column(name="login_count", nullable=false)
    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    @Column(name="last_login_at")
    public DateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(DateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    @Min(0)
    @Column(name="balance", nullable=false)
    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
    
    @Min(0)
    @Column(name="total_deposit", nullable=false)
    public long getTotalDeposit() {
        return totalDeposit;
    }

    public void setTotalDeposit(long totalDeposit) {
        this.totalDeposit = totalDeposit;
    }
    
    @Min(0)
    @Column(name="total_cost", nullable=false)
    public long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(long totalCost) {
        this.totalCost = totalCost;
    }

    public void pay(int credits) {
        if(this.balance < credits){
            throw new UserBalanceNotEnoughException();
        }
        this.balance -= credits;
        this.totalCost += credits;
    }
    
    public void deposit(int credits){
        this.balance += credits;
        this.totalDeposit += credits;
    }
    
    public void refound(int credits){
        this.balance += credits;
        this.totalCost -= credits;
    }

    @Version
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString())
                .append("id", id).append("username", username).toString();
    }
}
