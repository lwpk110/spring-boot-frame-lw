package cn.tendata.mdcs.service;

import cn.tendata.mdcs.core.MailDeliveryBounceReportManager;
import cn.tendata.mdcs.core.io.export.FileExportManager;
import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.data.elasticsearch.domain.MailRecipientActionDocument;
import cn.tendata.mdcs.data.elasticsearch.repository.MailRecipientActionRepository;
import cn.tendata.mdcs.mail.MailDeliveryTaskReport;
import cn.tendata.mdcs.mail.MailRecipientAction;
import cn.tendata.mdcs.mail.MailRecipientAction.MailRecipientActionStatus;
import cn.tendata.mdcs.mail.core.MailDeliveryGateway;
import cn.tendata.mdcs.service.model.MailDeliveryBounceReport;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.lucene.queryparser.flexible.standard.QueryParserUtil;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class UserMailDeliveryTaskReportServiceImpl implements UserMailDeliveryTaskReportService {

    private final MailDeliveryGateway mailDeliveryGateway;
    private final MailRecipientActionRepository mailRecipientActionRepository;
    private final MailDeliveryBounceReportManager mailDeliveryBounceReportManager;
    private final FileExportManager fileExportManager;

    @Autowired
    public UserMailDeliveryTaskReportServiceImpl(MailDeliveryGateway mailDeliveryGateway,
            MailDeliveryBounceReportManager mailDeliveryBounceReportManager,
            MailRecipientActionRepository mailRecipientActionRepository,
            FileExportManager fileExportManager) {
        this.mailDeliveryGateway = mailDeliveryGateway;
        this.mailRecipientActionRepository = mailRecipientActionRepository;
        this.mailDeliveryBounceReportManager = mailDeliveryBounceReportManager;
        this.fileExportManager = fileExportManager;
    }

    public MailDeliveryTaskReport getReport(UserMailDeliveryTask task) {
        // cancel lazy loading
        task.getDeliveryChannelNode().getServerKey();
        //
        return mailDeliveryGateway.getReport(task);
    }

    public List<MailRecipientAction> getRecipientActions(UserMailDeliveryTask task) {
        // cancel lazy loading
        task.getDeliveryChannelNode().getServerKey();
        //
        return mailDeliveryGateway.getRecipientActions(task);
    }

    public List<MailRecipientAction> getRecipientActions(UserMailDeliveryTask task, int startIdx, int endIdx) {
        List<MailRecipientAction> recipientActions = getRecipientActions(task);
        if (recipientActions != null) {
            recipientActions = recipientActions.stream().skip(startIdx).limit(endIdx - startIdx + 1)
                    .collect(Collectors.toList());
        }
        return recipientActions;
    }

    public List<MailRecipientAction> getRecipientActions(UserMailDeliveryTask task,
            MailRecipientActionStatus actionStatus, int startIdx, int endIdx) {
        List<MailRecipientAction> recipientActions = getRecipientActions(task);
        if (recipientActions != null) {
            recipientActions = recipientActions.stream().filter(a -> a.getActionStatus().equals(actionStatus))
                    .skip(startIdx).limit(endIdx - startIdx).collect(Collectors.toList());
        }
        return recipientActions;
    }

    @Override
    public void export(OutputStream stream, Collection<? extends MailRecipientAction> recipientActions)
            throws IOException {
        fileExportManager.export(stream, recipientActions);
    }

    @Override
    public File export(Collection<? extends MailRecipientAction> recipientActions) throws IOException {
        return fileExportManager.export(recipientActions);
    }

    public Page<MailRecipientActionDocument> search(User user, String email, Pageable pageable) {
        return mailRecipientActionRepository.search(user, email, pageable);
    }

    public List<MailDeliveryBounceReport> getRecipientBounceSummaryReport(DateTime start, DateTime end) {

        List<MailDeliveryBounceReport> rpts =
                mailDeliveryBounceReportManager.getRecipientBounceSummaryReport(start, end);
        return rpts;
    }

    @Override
    public List<MailRecipientActionDocument> search(String taskId, String email) {
        return this.mailRecipientActionRepository.search(taskId, email);
    }

    @Override
    public MailDeliveryTaskReport countReport(String field, String taskId) {
        List<Terms.Bucket> list = mailRecipientActionRepository.countReport(field, taskId);
        MailDeliveryTaskReport mailDeliveryTaskReport = new MailDeliveryTaskReport();
        packMailDeliveryTaskReportDocument(mailDeliveryTaskReport, list);
        return mailDeliveryTaskReport;
    }

    @Override
    public MailRecipientActionDocument getMailRecipientActionDocument(String taskId, String email) {
        List<MailRecipientActionDocument> list =
                this.mailRecipientActionRepository.findAllByTaskIdAndEmail(taskId, QueryParserUtil.escape(email));
        if (null != list) {
            return list.size() > 0 ? list.get(0) : null;
        }
        return null;
    }

    public List<MailRecipientAction> getAllBounceByRecipientsMailSuffix(String recipientsMailSuffix, DateTime start,
            DateTime end) {

        List<MailRecipientAction> bounceList =
                mailDeliveryBounceReportManager.getAllBounceByRecipientsMailSuffix(recipientsMailSuffix, start, end);
        return bounceList;
    }

    private void packMailDeliveryTaskReportDocument(MailDeliveryTaskReport mailDeliveryTaskReport,
            List<Terms.Bucket> list) {
        int total = 0;
        for (Terms.Bucket bucket : list) {
            int val = (int) bucket.getDocCount();
            String key = bucket.getKeyAsText().toString();
            if (key.equals(MailRecipientAction.MailRecipientActionStatus.OPEN.toString())) {
                mailDeliveryTaskReport.setUniqueOpenCount(val);
            } else if (key.equals(MailRecipientAction.MailRecipientActionStatus.CLICK.toString())) {
                mailDeliveryTaskReport.setMailClicked(val);
            } else if (key.equals(MailRecipientAction.MailRecipientActionStatus.HARD_BOUNCE.toString())) {
                mailDeliveryTaskReport.setHardBounce(val);
            } else if (key.equals(MailRecipientAction.MailRecipientActionStatus.SENT_SUCCESS.toString())) {
                mailDeliveryTaskReport.setSent(val);
            } else if (key.equals(MailRecipientAction.MailRecipientActionStatus.SOFT_BOUNCE.toString())) {
                mailDeliveryTaskReport.setSoftBounce(val);
            } else if (key.equals(MailRecipientAction.MailRecipientActionStatus.SPAM_COMPLAINT.toString())) {
            } else if (key.equals(MailRecipientAction.MailRecipientActionStatus.UNSUBSCRIBE.toString())) {
                mailDeliveryTaskReport.setUnsubscribe(val);
            }
            total += val;
        }
        mailDeliveryTaskReport.setUniqueOpenCount(
                mailDeliveryTaskReport.getUniqueOpenCount() + mailDeliveryTaskReport.getMailClicked());
        mailDeliveryTaskReport.setSent(
                mailDeliveryTaskReport.getSent()
                        + mailDeliveryTaskReport.getHardBounce()
                        + mailDeliveryTaskReport.getSoftBounce()
                        + mailDeliveryTaskReport.getUniqueOpenCount()
        );
        mailDeliveryTaskReport.setTotal(total);
    }

    @Override
    public Page<MailRecipientActionDocument> getDetails(String taskId, Pageable pageable) {
        return mailRecipientActionRepository.findAllByTaskId(taskId, pageable);
    }
}
