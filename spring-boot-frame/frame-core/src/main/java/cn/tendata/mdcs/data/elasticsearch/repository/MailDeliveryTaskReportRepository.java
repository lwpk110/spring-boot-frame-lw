package cn.tendata.mdcs.data.elasticsearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.tendata.mdcs.data.elasticsearch.domain.MailDeliveryTaskReportDocument;

public interface MailDeliveryTaskReportRepository extends PagingAndSortingRepository<MailDeliveryTaskReportDocument, String> {

    Page<MailDeliveryTaskReportDocument> findAllByUser_UserId(long userId, Pageable pageable);
}
