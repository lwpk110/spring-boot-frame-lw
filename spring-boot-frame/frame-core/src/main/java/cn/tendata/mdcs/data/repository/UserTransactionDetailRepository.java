package cn.tendata.mdcs.data.repository;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserTransactionDetail;

public interface UserTransactionDetailRepository extends PagingAndSortingRepository<UserTransactionDetail, Long>, QueryDslPredicateExecutor<UserTransactionDetail> {

    List<UserTransactionDetail> findAllByUserOrderByCreatedDateDesc(User user, Pageable pageable);
    
    Page<UserTransactionDetail> findAllByUser(User user, Pageable pageable);
    
    Page<UserTransactionDetail> findAllByUserAndCreatedDateBetween(User user, DateTime start, DateTime end, Pageable pageable);
}
