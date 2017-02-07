package cn.tendata.mdcs.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonView;

import cn.tendata.mdcs.data.jackson.DataView;

@Entity
public class UserTransactionDetail extends AbstractEntity<Long> {

    private static final long serialVersionUID = 1L;

    public enum UserTransactionType {
        DEPOSIT, BALANCE_ASSIGN, SEND, SEND_CANCEL
    }
    
    @JsonView(DataView.Basic.class)
    private UserTransactionType transactionType;
    @JsonView(DataView.Basic.class)
    private int credits;
    @JsonView(DataView.Basic.class)
    private String comment;
    @JsonView(DataView.Basic.class)
    private DateTime createdDate;
    
    private User user;
    
    public UserTransactionDetail() {}
    
    public UserTransactionDetail(UserTransactionType transactionType, int credits, String comment, User user) {
        this.transactionType = transactionType;
        this.credits = credits;
        this.comment = comment;
        this.user = user;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return super.getId();
    }
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name="transaction_type", length=50, nullable=false)
    public UserTransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(UserTransactionType transactionType) {
        this.transactionType = transactionType;
    }
    
    @Column(name="credits", nullable=false)
    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Size(max=255)
    @Column(name="comment", length=255)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @CreatedDate
    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final DateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    @ManyToOne(optional=false)
    @JoinColumn(name="user_id", nullable=false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
