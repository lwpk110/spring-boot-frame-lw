package cn.tendata.mdcs.service;

import java.util.List;
import java.util.UUID;

import cn.tendata.mdcs.data.domain.MailDeliveryChannelNode;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.service.model.MailDeliveryTaskDetail;
import cn.tendata.mdcs.service.model.SearchKeywordType;
import cn.tendata.mdcs.util.DateTimeQuery;
import cn.tendata.mdcs.util.UserQuery;

public interface UserMailDeliveryTaskService extends EntityService<UserMailDeliveryTask, UUID> {

    void submit(MailDeliveryTaskDetail taskDetail);

    void retry(UserMailDeliveryTask task);

    void cancel(UserMailDeliveryTask task);

    long getCount(User user);

    List<UserMailDeliveryTask> getAll(User user, int number);

    Page<UserMailDeliveryTask> getAll(User user, DateTimeQuery dateTimeQuery, String taskName, UserQuery userQuery, Pageable pageable);

    Page<UserMailDeliveryTask> getAll(User user, Pageable pageable);

    Page<UserMailDeliveryTask> getAll(SearchKeywordType type, String keyword, DateTime start, DateTime end, Pageable pageable);

    List<UserMailDeliveryTask> getAll(DateTime start, DateTime end);
    
    Page<UserMailDeliveryTask> getAll(DateTime start, DateTime end, Pageable pageable);

    List<UserMailDeliveryTask> getAll(DateTime start, DateTime end, MailDeliveryChannelNode mailDeliveryChannelNode);
}
