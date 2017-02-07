package cn.tendata.mdcs.data.repository;

import cn.tendata.mdcs.data.domain.MailDeliveryChannelNode;
import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;


public interface UserMailDeliveryTaskRepository extends PagingAndSortingRepository<UserMailDeliveryTask, UUID>, QueryDslPredicateExecutor<UserMailDeliveryTask> {

    List<UserMailDeliveryTask> findAllByUserOrderByCreatedDateDesc(User user, Pageable pageable);

    Page<UserMailDeliveryTask> findAllByUser(User user, Pageable pageable);


    /**
     * 根据创建时间和channelNodeid来查询list
     *
     * @param start
     * @param end
     * @param deliveryChannelNode
     * @return
     */
    List<UserMailDeliveryTask> findByCreatedDateBetweenAndDeliveryChannelNode(DateTime start, DateTime end, MailDeliveryChannelNode deliveryChannelNode);

}
