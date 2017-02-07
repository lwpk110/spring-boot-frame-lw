package cn.tendata.mdcs.data.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cn.tendata.mdcs.util.TradeUtils;

@Entity
public class UserDepositOrderDetail extends AbstractEntityAuditable<Long> {

    private static final long serialVersionUID = 1L;

    public static class DepositOrderStatus {
        public static final int NONE = 0;
        public static final int FAILED = 1;
        public static final int SUCCESS = 100;
    }
    
    private String depositMethod;
    private String tradeNo;
    private int credits;
    private BigDecimal amount;
    private int status;
    
    private User user;
    
    private long version;
    
    public UserDepositOrderDetail(){}
    
    public UserDepositOrderDetail(int credits, BigDecimal amount, User user){
        this("BL", credits, amount, user);
    }
    
    public UserDepositOrderDetail(String depositMethod, int credits, BigDecimal amount, User user){
        this(depositMethod, TradeUtils.getTradeNo(), credits, amount, user);
    }
    
    public UserDepositOrderDetail(String depositMethod, String tradeNo, int credits, BigDecimal amount, User user) {
        this.depositMethod = depositMethod;
        this.tradeNo = tradeNo;
        this.credits = credits;
        this.amount = amount;
        this.status = DepositOrderStatus.NONE;
        this.user = user;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return super.getId();
    }
    
    @NotNull
    @Size(min=1, max=20)
    @Column(name="deposit_method", length=20)
    public String getDepositMethod() {
        return depositMethod;
    }

    public void setDepositMethod(String depositMethod) {
        this.depositMethod = depositMethod;
    }
    
    @NotNull
    @Size(min=1, max=50)
    @Column(name="trade_no", length=50, nullable=false, unique=true)
    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    @Min(1)
    @Column(name="credits", nullable=false)
    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Column(name="status", nullable=false)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @DecimalMin(value="0.01")
    @Column(name="amount", nullable=false)
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
