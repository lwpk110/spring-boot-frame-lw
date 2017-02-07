package cn.tendata.mdcs.service;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailTemplate;
import cn.tendata.mdcs.data.repository.UserMailTemplatePredicates;
import cn.tendata.mdcs.data.repository.UserMailTemplateRepository;
import cn.tendata.mdcs.util.StatusEnum;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserMailTemplateServiceImpl extends EntityServiceSupport<UserMailTemplate, Long, UserMailTemplateRepository>
        implements UserMailTemplateService {

    @Autowired
    protected UserMailTemplateServiceImpl(UserMailTemplateRepository repository) {
        super(repository);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void approveTemplate(StatusEnum statusEnum, Long id) {
        getRepository().approveTemplate(statusEnum.getStatus(), id, StatusEnum.WAITING_APPROVE.getStatus());
    }

    @Transactional(readOnly = true)
    public long getCount(User user) {
        return getRepository().count(UserMailTemplatePredicates.user(user));
    }


    @Transactional(readOnly = true)
    public List<UserMailTemplate> getAllApprovePass(User user) {
        Collection<Integer> collection = new ArrayList<Integer>();
        collection.add(StatusEnum.PASS_APPROVE.getStatus());
        collection.add(StatusEnum.PASS_APPROVE_SYS.getStatus());
        return getRepository().findAllByApproveStatusInAndUserOrderByCreatedDateDesc(collection, user);
    }

    @Transactional(readOnly = true)
    public Page<UserMailTemplate> getAllByApproveStatus(int approveStatus, Pageable pageable) {
        return getRepository().findAllByApproveStatusOrderByCreatedDate(approveStatus, pageable);
    }

    @Transactional(readOnly = true)
    public Page<UserMailTemplate> getAll(User user, Pageable pageable) {
        return getRepository().findAllByUser(user, pageable);
    }

    @Transactional(readOnly = true)
    public List<UserMailTemplate> getAllWaittingApprove(int approveStatus, DateTime createdDate) {
        return getRepository().getAllWaittingApprove(approveStatus, createdDate);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
	public void batchApproveTemplate(StatusEnum statusEnum, Long[] ids) {
		getRepository().batchApproveTemplate(statusEnum.getStatus(), ids, StatusEnum.WAITING_APPROVE.getStatus());
		
	}
}
