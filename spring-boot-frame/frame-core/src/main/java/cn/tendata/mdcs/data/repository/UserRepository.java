package cn.tendata.mdcs.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.tendata.mdcs.data.domain.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, QueryDslPredicateExecutor<User> {

    User findByUsername(String username);
    
    Page<User> findAllByUsernameLike(String username, Pageable pageable);
}
