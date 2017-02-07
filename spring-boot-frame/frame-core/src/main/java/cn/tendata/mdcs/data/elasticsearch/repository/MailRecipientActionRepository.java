package cn.tendata.mdcs.data.elasticsearch.repository;

import cn.tendata.mdcs.data.elasticsearch.domain.MailRecipientActionDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MailRecipientActionRepository extends PagingAndSortingRepository<MailRecipientActionDocument, String>, MailRecipientActionRepositoryCustom {

    List<MailRecipientActionDocument> findAllByTaskId(String taskId);

    Page<MailRecipientActionDocument> findAllByTaskId(String taskId, Pageable pageable);

    long countByTaskId(String taskId);

    long deleteByTaskId(String taskId);

    List<MailRecipientActionDocument> findAllByTaskIdAndEmail(String taskId, String email);
}
