package cn.tendata.mdcs.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.tendata.mdcs.data.domain.MailDeliveryChannel;

public interface MailDeliveryChannelRepository extends PagingAndSortingRepository<MailDeliveryChannel, Integer>, QueryDslPredicateExecutor<MailDeliveryChannel> {

    Page<MailDeliveryChannel> findAllByNameLike(String name, Pageable pageable);
}
