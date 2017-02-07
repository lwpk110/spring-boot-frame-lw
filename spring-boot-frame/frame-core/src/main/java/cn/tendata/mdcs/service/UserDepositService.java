package cn.tendata.mdcs.service;

import cn.tendata.mdcs.data.domain.UserDepositOrderDetail;
import cn.tendata.mdcs.service.model.DirectDepositDetail;

public interface UserDepositService {

    void deposit(DirectDepositDetail detail);
    
    UserDepositOrderDetail depositStart(UserDepositOrderDetail detail);
    
    UserDepositOrderDetail depositEnd(String tradeNo, boolean success);
}
