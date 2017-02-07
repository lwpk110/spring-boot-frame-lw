package cn.tendata.mdcs.service;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserTransactionDetail;
import cn.tendata.mdcs.data.repository.UserTransactionDetailPredicates;
import cn.tendata.mdcs.data.repository.UserTransactionDetailRepository;
import cn.tendata.mdcs.service.model.SearchKeywordType;

@Service
public class UserTransactionDetailServiceImpl implements UserTransactionDetailService {

    private final UserTransactionDetailRepository repository;
    
    @Autowired
    public UserTransactionDetailServiceImpl(UserTransactionDetailRepository repository) {
        this.repository = repository;
    }
    
    @Transactional(readOnly=true)
    public List<UserTransactionDetail> getAll(User user, int number) {
        Pageable pageable = new PageRequest(0, number);
        return repository.findAllByUserOrderByCreatedDateDesc(user, pageable);
    }

    @Transactional(readOnly=true)
    public Page<UserTransactionDetail> getAll(User user, Pageable pageable) {
        return repository.findAllByUser(user, pageable);
    }

    @Transactional(readOnly=true)
    public Page<UserTransactionDetail> getAll(User user, DateTime start, DateTime end, Pageable pageable) {
        if(start != null && end != null){
            return repository.findAllByUserAndCreatedDateBetween(user, start, end, pageable);
        }
        return repository.findAllByUser(user, pageable);
    }

    @Transactional(readOnly=true)
    public Page<UserTransactionDetail> getAll(SearchKeywordType type, String keyword, DateTime start, DateTime end, Pageable pageable) {
        return repository.findAll(UserTransactionDetailPredicates.list(type, keyword, start, end), pageable);
    }
}
