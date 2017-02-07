package cn.tendata.mdcs.data.repository;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailTemplate;
import cn.tendata.mdcs.util.StatusEnum;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface UserMailTemplateRepository extends PagingAndSortingRepository<UserMailTemplate, Long>, QueryDslPredicateExecutor<UserMailTemplate> {

    List<UserMailTemplate> findAllByApproveStatusInAndUserOrderByCreatedDateDesc(Collection<Integer> approveStatusCollection, User user);

    Page<UserMailTemplate> findAllByApproveStatusOrderByCreatedDate(int approveStatus, Pageable pageable);

    Page<UserMailTemplate> findAllByUser(User user, Pageable pageable);

    @Modifying
    @Query("update UserMailTemplate set approveStatus = ?1 where id = ?2 and approveStatus =?3")
    void approveTemplate(int approveStatus, Long id, int initStatus);
    
    @Modifying
    @Query(value="update user_mail_templates set approve_status = :approveStatus where id  in :ids and approve_status =:initStatus",nativeQuery=true)
    void batchApproveTemplate(@Param("approveStatus")int approveStatus, @Param("ids")Long[] ids, @Param("initStatus")int initStatus);

    @Query("select u from UserMailTemplate u where u.approveStatus =?1 and u.lastModifiedDate <?2")
    List<UserMailTemplate> getAllWaittingApprove(int approveStatus, DateTime lastModifiedDate);
}
