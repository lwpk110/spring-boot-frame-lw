package cn.tendata.mdcs.service;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.data.elasticsearch.domain.MailDeliveryTaskReportDocument;
import cn.tendata.mdcs.data.elasticsearch.domain.MailRecipientActionDocument;
import cn.tendata.mdcs.mail.MailDeliveryTaskReport;
import cn.tendata.mdcs.mail.MailRecipientAction;
import cn.tendata.mdcs.mail.MailRecipientAction.MailRecipientActionStatus;
import cn.tendata.mdcs.service.model.MailDeliveryBounceReport;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserMailDeliveryTaskReportService {

    MailDeliveryTaskReport getReport(UserMailDeliveryTask task);

    List<MailRecipientAction> getRecipientActions(UserMailDeliveryTask task);

    List<MailRecipientAction> getRecipientActions(UserMailDeliveryTask task, int startIdx, int endIdx);

    List<MailRecipientAction> getRecipientActions(UserMailDeliveryTask task, MailRecipientActionStatus actionStatus,
            int startIdx, int endIdx);

    void export(OutputStream stream, Collection<? extends MailRecipientAction> recipientActions) throws IOException;

    File export(Collection<? extends MailRecipientAction> recipientActions) throws IOException;

    Page<MailRecipientActionDocument> search(User user, String email, Pageable pageable);

    List<MailRecipientAction> getAllBounceByRecipientsMailSuffix(String recipientsMailSuffix, DateTime start,
            DateTime end);

    List<MailDeliveryBounceReport> getRecipientBounceSummaryReport(DateTime start, DateTime end);

    List<MailRecipientActionDocument> search(String taskId, String email);

    MailDeliveryTaskReport countReport(String filed, String taskId);

    MailRecipientActionDocument getMailRecipientActionDocument(String taskId, String email);

    Page<MailRecipientActionDocument> getDetails(String taskId, Pageable pageable);
}
