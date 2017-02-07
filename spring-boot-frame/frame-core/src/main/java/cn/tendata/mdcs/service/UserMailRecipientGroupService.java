package cn.tendata.mdcs.service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailRecipientGroup;

public interface UserMailRecipientGroupService extends EntityService<UserMailRecipientGroup, UUID> {

    List<UserMailRecipientGroup> getAll(User user);
    
    List<UserMailRecipientGroup> getAll(User user, Collection<UUID> ids);
    
    Page<UserMailRecipientGroup> getAll(User user, Pageable pageable);
}
