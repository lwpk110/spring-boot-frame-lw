package cn.tendata.mdcs.service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailRecipientGroup;
import cn.tendata.mdcs.data.repository.UserMailRecipientGroupRepository;

@Service
public class UserMailRecipientGroupServiceImpl extends EntityServiceSupport<UserMailRecipientGroup, UUID, UserMailRecipientGroupRepository> 
    implements UserMailRecipientGroupService {

    @Autowired
    protected UserMailRecipientGroupServiceImpl(UserMailRecipientGroupRepository repository) {
        super(repository);
    }

    @Transactional(readOnly=true)
    public List<UserMailRecipientGroup> getAll(User user) {
        return getRepository().findAllByUserOrderByCreatedDateDesc(user);
    }
    
    @Transactional(readOnly=true)
    public List<UserMailRecipientGroup> getAll(User user, Collection<UUID> ids) {
        return getRepository().findAllByUserAndIdIn(user, ids);
    }

    @Transactional(readOnly=true)
    public Page<UserMailRecipientGroup> getAll(User user, Pageable pageable) {
        return getRepository().findAllByUser(user, pageable);
    }
}
