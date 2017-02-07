package cn.tendata.mdcs.web.model;

import javax.validation.constraints.Min;

public class AssignBalanceDto {

    @Min(0)
    private int credits;
    
    public int getCredits() {
        return credits;
    }
    
    public void setCredits(int credits) {
        this.credits = credits;
    }
}
