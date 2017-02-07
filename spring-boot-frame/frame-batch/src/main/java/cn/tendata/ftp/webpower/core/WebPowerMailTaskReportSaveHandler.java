package cn.tendata.ftp.webpower.core;

import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.mail.MailDeliveryTaskReport;
import cn.tendata.mdcs.mail.core.MailDeliveryTaskReportManager;
import cn.tendata.mdcs.service.UserMailDeliveryTaskReportService;
import cn.tendata.mdcs.service.UserMailDeliveryTaskService;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ernest on 2016/9/8.
 */
public class WebPowerMailTaskReportSaveHandler {
    private final MailDeliveryTaskReportManager mailDeliveryTaskReportManager;
    private final UserMailDeliveryTaskReportService userMailDeliveryTaskReportService;
    private final UserMailDeliveryTaskService userMailDeliveryTaskService;

    public WebPowerMailTaskReportSaveHandler(MailDeliveryTaskReportManager mailDeliveryTaskReportManager,
            UserMailDeliveryTaskReportService userMailDeliveryTaskReportService,
            UserMailDeliveryTaskService userMailDeliveryTaskService) {
        this.mailDeliveryTaskReportManager = mailDeliveryTaskReportManager;
        this.userMailDeliveryTaskReportService = userMailDeliveryTaskReportService;
        this.userMailDeliveryTaskService = userMailDeliveryTaskService;
    }

    @Transactional
    public void saveReport(String taskId) {

        UserMailDeliveryTask task = userMailDeliveryTaskService.getById(UUID.fromString(taskId));

        mailDeliveryTaskReportManager.saveTaskReport(task, createMailDeliveryTaskReport(task));
    }

    private MailDeliveryTaskReport createMailDeliveryTaskReport(UserMailDeliveryTask task) {
        task.getDeliveryChannelNode().getServerKey();
        MailDeliveryTaskReport mailDeliveryTaskReport =
                userMailDeliveryTaskReportService.countReport("actionStatus", task.getTaskId());
        mailDeliveryTaskReport.setReplyEmail(task.getDeliverySettings().getReplyEmail());
        mailDeliveryTaskReport.setReplyName(task.getDeliverySettings().getReplyName());
        mailDeliveryTaskReport.setSenderEmail(task.getDeliverySettings().getSenderEmail());
        mailDeliveryTaskReport.setSenderName(task.getDeliverySettings().getSenderName());
        mailDeliveryTaskReport.setSubject(task.getTemplate().getSubject());
        mailDeliveryTaskReport.setTaskId(task.getTaskId());
        //暂时设置，报告中不包含这两个字段
        mailDeliveryTaskReport.setSendDate(task.isScheduled() ? task.getScheduledDate() : task.getCreatedDate());
        mailDeliveryTaskReport.setFinishDate(task.isScheduled() ? task.getScheduledDate() : task.getCreatedDate());
        return mailDeliveryTaskReport;
    }
}
