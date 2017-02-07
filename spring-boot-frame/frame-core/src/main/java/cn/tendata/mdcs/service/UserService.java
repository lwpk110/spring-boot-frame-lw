package cn.tendata.mdcs.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.service.model.SearchKeywordType;

public interface UserService extends EntityService<User, Long>{

    void assignBalance(User parent, User child, int balance);
    
    User findByUsername(String username);
    
    Page<User> getAll(String username, Pageable pageable);
    
    Page<User> search(SearchKeywordType type, String keyword, Pageable pageable);
}
