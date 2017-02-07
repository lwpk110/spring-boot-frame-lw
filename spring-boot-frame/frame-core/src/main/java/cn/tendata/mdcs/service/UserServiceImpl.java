package cn.tendata.mdcs.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.tendata.mdcs.core.DefaultMessageSource;
import cn.tendata.mdcs.core.UserBalanceNotEnoughException;
import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserTransactionDetail;
import cn.tendata.mdcs.data.domain.UserTransactionDetail.UserTransactionType;
import cn.tendata.mdcs.data.repository.UserPredicates;
import cn.tendata.mdcs.data.repository.UserRepository;
import cn.tendata.mdcs.data.repository.UserTransactionDetailRepository;
import cn.tendata.mdcs.service.model.SearchKeywordType;

@Service
public class UserServiceImpl extends EntityServiceSupport<User, Long, UserRepository> implements UserService, MessageSourceAware {

    private final UserTransactionDetailRepository transactionDetailRepository;
    
    protected MessageSourceAccessor messages = DefaultMessageSource.getAccessor();
    
    @Autowired
    protected UserServiceImpl(UserRepository repository, UserTransactionDetailRepository transactionDetailRepository) {
        super(repository);
        this.transactionDetailRepository = transactionDetailRepository;
    }
    
    @Transactional(isolation=Isolation.SERIALIZABLE)
    public void assignBalance(User parent, User child, int balance) {
        if(balance == child.getBalance()){
            return;
        }
        int totalBalance = parent.getBalance() + child.getBalance();
        if(totalBalance < balance){
            throw new UserBalanceNotEnoughException();
        }
        int currentParentUserBalance = totalBalance - balance;
        UserTransactionDetail parentUserTransactionDetail = new UserTransactionDetail(UserTransactionType.BALANCE_ASSIGN, 
                currentParentUserBalance - parent.getBalance(), 
                messages.getMessage("UserTransactionDetail.parentAssignBalance", new Object[] { child.getUsername() }, "Child account: {0}"),  
                parent);
        UserTransactionDetail childUserTransactionDetail = new UserTransactionDetail(UserTransactionType.BALANCE_ASSIGN, 
                balance - child.getBalance(), 
                messages.getMessage("UserTransactionDetail.childAssignBalance", new Object[] { parent.getUsername() }, "Parent account: {0}"), 
                child);
        transactionDetailRepository.save(Arrays.asList(parentUserTransactionDetail, childUserTransactionDetail));
        parent.setBalance(currentParentUserBalance);
        child.setBalance(balance);
        save(Arrays.asList(parent, child));
    }
    
    @Transactional(readOnly=true)
    public User findByUsername(String username) {
        return getRepository().findByUsername(username);
    }

    @Transactional(readOnly=true)
    public Page<User> getAll(String username, Pageable pageable) {
        if(StringUtils.hasText(username)){
            return getRepository().findAllByUsernameLike(username, pageable);
        }
        return getRepository().findAll(pageable);
    }
    
    @Override
    public Page<User> search(SearchKeywordType type, String keyword, Pageable pageable) {
        return getRepository().findAll(UserPredicates.search(type, keyword), pageable);
    }
    
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }
}
