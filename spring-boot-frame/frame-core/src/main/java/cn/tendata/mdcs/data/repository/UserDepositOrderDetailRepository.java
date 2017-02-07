package cn.tendata.mdcs.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserDepositOrderDetail;

public interface UserDepositOrderDetailRepository extends PagingAndSortingRepository<UserDepositOrderDetail, Long> {

    UserDepositOrderDetail findByTradeNo(String tradeNo);
    
    Page<UserDepositOrderDetail> findAllByUser(User user, Pageable pageable);
}
