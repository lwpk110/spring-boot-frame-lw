package cn.tendata.mdcs.service;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserTransactionDetail;
import cn.tendata.mdcs.service.model.SearchKeywordType;

public interface UserTransactionDetailService {

    List<UserTransactionDetail> getAll(User user, int number);
    
    Page<UserTransactionDetail> getAll(User user, Pageable pageable);
    
    Page<UserTransactionDetail> getAll(User user, DateTime start, DateTime end, Pageable pageable);
    
    Page<UserTransactionDetail> getAll(SearchKeywordType type, String keyword, DateTime start, DateTime end, Pageable pageable);
}
