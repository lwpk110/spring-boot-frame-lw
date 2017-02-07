package cn.tendata.mdcs.service;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailTemplate;
import cn.tendata.mdcs.util.StatusEnum;

public interface UserMailTemplateService extends EntityService<UserMailTemplate, Long> {

    void approveTemplate(StatusEnum statusEnum, Long id);
    
    void batchApproveTemplate(StatusEnum statusEnum, Long[] ids);

    long getCount(User user);

    List<UserMailTemplate> getAllApprovePass(User user);

    Page<UserMailTemplate> getAllByApproveStatus(int approveStatus, Pageable pageable);

    Page<UserMailTemplate> getAll(User user, Pageable pageable);

    List<UserMailTemplate> getAllWaittingApprove(int approveStatus, DateTime createdDate);

}
