package cn.tendata.mdcs.service.model;

import javax.validation.constraints.Min;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import cn.tendata.mdcs.data.domain.User;

public class DirectDepositDetail {

    @Min(1)
    private int credits;
    private User user;
    
    public int getCredits() {
        return credits;
    }
    
    public void setCredits(int credits) {
        this.credits = credits;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("credits", credits)
                .append("user", user).toString();
    }
}
