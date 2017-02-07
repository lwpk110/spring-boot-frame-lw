package cn.tendata.mdcs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import cn.tendata.mdcs.core.DefaultMessageSource;
import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserDepositOrderDetail;
import cn.tendata.mdcs.data.domain.UserDepositOrderDetail.DepositOrderStatus;
import cn.tendata.mdcs.data.domain.UserTransactionDetail;
import cn.tendata.mdcs.data.domain.UserTransactionDetail.UserTransactionType;
import cn.tendata.mdcs.data.repository.UserDepositOrderDetailRepository;
import cn.tendata.mdcs.data.repository.UserRepository;
import cn.tendata.mdcs.data.repository.UserTransactionDetailRepository;
import cn.tendata.mdcs.service.model.DirectDepositDetail;

@Service
public class UserDepositServiceImpl implements UserDepositService, MessageSourceAware {

    private final UserDepositOrderDetailRepository userDepositOrderDetailRepository;
    private final UserRepository userRepository;
    private final UserTransactionDetailRepository transactionDetailRepository;
    
    protected MessageSourceAccessor messages = DefaultMessageSource.getAccessor();
    
    @Autowired
    public UserDepositServiceImpl(UserDepositOrderDetailRepository userDepositOrderDetailRepository, UserRepository userRepository,
            UserTransactionDetailRepository transactionDetailRepository) {
        this.userDepositOrderDetailRepository = userDepositOrderDetailRepository;
        this.userRepository = userRepository;
        this.transactionDetailRepository = transactionDetailRepository;
    }

    @Transactional(isolation=Isolation.SERIALIZABLE)
    public void deposit(DirectDepositDetail detail) {
        User user = detail.getUser();
        user.deposit(detail.getCredits());
        userRepository.save(user);
        UserTransactionDetail transactionDetail = new UserTransactionDetail(UserTransactionType.DEPOSIT, 
                detail.getCredits(), messages.getMessage("UserTransactionDetail.directDeposit", "Manual recharge"), user);
        transactionDetailRepository.save(transactionDetail);
    }

    @Transactional(isolation=Isolation.SERIALIZABLE)
    public UserDepositOrderDetail depositStart(UserDepositOrderDetail detail) {
        return userDepositOrderDetailRepository.save(detail);
    }

    @Transactional(isolation=Isolation.SERIALIZABLE)
    public UserDepositOrderDetail depositEnd(String tradeNo, boolean success) {
        UserDepositOrderDetail detail = userDepositOrderDetailRepository.findByTradeNo(tradeNo);
        if(detail != null && detail.getStatus() == DepositOrderStatus.NONE){
            if(success){
                User user = detail.getUser();
                user.deposit(detail.getCredits());
                userRepository.save(user);
                UserTransactionDetail transactionDetail = new UserTransactionDetail(UserTransactionType.DEPOSIT, 
                        detail.getCredits(), null, user);
                transactionDetailRepository.save(transactionDetail);
                
                detail.setStatus(DepositOrderStatus.SUCCESS);
            }
            else{
                detail.setStatus(DepositOrderStatus.FAILED);
            }
            detail = userDepositOrderDetailRepository.save(detail);
        }
        return detail;
    }
    
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }
}
