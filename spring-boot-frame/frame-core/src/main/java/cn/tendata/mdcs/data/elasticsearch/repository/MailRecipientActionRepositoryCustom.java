package cn.tendata.mdcs.data.elasticsearch.repository;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.elasticsearch.domain.MailRecipientActionDocument;
import cn.tendata.mdcs.mail.MailDeliveryTaskReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MailRecipientActionRepositoryCustom {

    Page<MailRecipientActionDocument> search(User user, String email, Pageable pageable);

    List<MailRecipientActionDocument> search(String taskId, String email);

    List countReport(String filed, String taskId);


}
