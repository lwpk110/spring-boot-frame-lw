package cn.tendata.mdcs.data.repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailRecipientGroup;

public interface UserMailRecipientGroupRepository extends PagingAndSortingRepository<UserMailRecipientGroup, UUID> {

    List<UserMailRecipientGroup> findAllByUserAndIdIn(User user, Collection<UUID> ids);
    
    List<UserMailRecipientGroup> findAllByUserOrderByCreatedDateDesc(User user);
    
    Page<UserMailRecipientGroup> findAllByUser(User user, Pageable pageable);
}
