package cn.tendata.mdcs.mail.core.elasticsearch;

import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.data.elasticsearch.domain.MailDeliveryChannelDocument;
import cn.tendata.mdcs.data.elasticsearch.domain.MailDeliveryTaskReportDocument;
import cn.tendata.mdcs.data.elasticsearch.domain.MailRecipientActionDocument;
import cn.tendata.mdcs.data.elasticsearch.domain.UserDocument;
import cn.tendata.mdcs.data.elasticsearch.repository.MailDeliveryTaskReportRepository;
import cn.tendata.mdcs.data.elasticsearch.repository.MailRecipientActionRepository;
import cn.tendata.mdcs.mail.MailDeliveryTaskReport;
import cn.tendata.mdcs.mail.MailRecipientAction;
import cn.tendata.mdcs.mail.core.AbstractMailDeliveryTaskReportManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

public class ElasticsearchMailDeliveryTaskReportManager extends AbstractMailDeliveryTaskReportManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchMailDeliveryTaskReportManager.class);

    private final MailDeliveryTaskReportRepository taskReportRepository;
    private final MailRecipientActionRepository recipientActionRepository;

    public ElasticsearchMailDeliveryTaskReportManager(MailDeliveryTaskReportRepository taskReportRepository,
            MailRecipientActionRepository recipientActionRepository) {
        Assert.notNull(taskReportRepository, "'taskReportRepository' must not be null");
        Assert.notNull(recipientActionRepository, "'recipientActionRepository' must not be null");
        this.taskReportRepository = taskReportRepository;
        this.recipientActionRepository = recipientActionRepository;
    }

    public MailDeliveryTaskReport getTaskReport(UserMailDeliveryTask task) {
        LOGGER.debug("Retrieving task report from elasticsearch, task id: {}.", task.getId());
        final MailDeliveryTaskReportDocument taskReportDocument = taskReportRepository.findOne(task.getTaskId());
        if (taskReportDocument != null) {
            if (task.getDeliveryChannel().isDisabled()) {
                LOGGER.debug("Delivery channel is disabled, task id: {}, channel id: {}.", task.getTaskId(),
                        task.getDeliveryChannel().getId());
                return createTaskReport(taskReportDocument);
            }
            if (task.getDeliveryChannelNode().isDisabled()) {
                LOGGER.debug("Delivery channel node is disabled, task id: {}, channel node id: {}.", task.getTaskId(),
                        task.getDeliveryChannelNode().getId());
                return createTaskReport(taskReportDocument);
            }
            if (isTaskReportMaxSync(taskReportDocument)) {
                LOGGER.debug("Synchronous task report to maximum limit, task id: {}.", task.getTaskId());
                return createTaskReport(taskReportDocument);
            }

            if (isTaskReportExpired(taskReportDocument)) {  //过期了需不需要更新报告
                if (!isInterfaceChannel(task.getDeliveryChannelNode().getServerKey()) && isHaveDetails(task.getTaskId())) {
                    LOGGER.info("-----------有詳細記錄！！！");
                    LOGGER.debug("Retrieved task report has detail, task id: {}.", task.getTaskId());
                    //判断是否有详单  -> yes
                    return createTaskReport(taskReportDocument);
                }
                LOGGER.debug("Retrieved task report is expired, task id: {}.", task.getTaskId());
            } else {
                LOGGER.debug("Retrieved task report is valid, task id: {}.", task.getTaskId());
                return createTaskReport(taskReportDocument);

            }
        }
        return null;
    }

    private boolean isInterfaceChannel(String serverKey) {
        List<String> list = Arrays.asList(this.getInterfaceChannelServerKeys());
        return list.contains(serverKey);
    }

    private boolean isHaveDetails(String taskId) {
        return recipientActionRepository.countByTaskId(taskId) > 0 ? true : false;
    }

    private MailDeliveryTaskReport createTaskReport(MailDeliveryTaskReportDocument taskReportDocument) {
        MailDeliveryTaskReport taskReport = new MailDeliveryTaskReport(taskReportDocument.getId());
        BeanUtils.copyProperties(taskReportDocument, taskReport);
        return taskReport;
    }

    private boolean isTaskReportExpired(MailDeliveryTaskReportDocument taskReportDocument) {
        return DateTime.now().isAfter(taskReportDocument.getLastModifiedDate().getMillis() + getTimeToLiveInMillis());
    }

    private boolean isTaskReportMaxSync(MailDeliveryTaskReportDocument taskReportDocument) {
        return DateTime.now().isAfter(taskReportDocument.getSendDate().getMillis() + getMaxTimeToSyncInMillis());
    }

    protected void saveTaskReportCore(UserMailDeliveryTask task, MailDeliveryTaskReport taskReport) {
        LOGGER.debug("Saving task report to elasticsearch, task id: {}.", task.getTaskId());
        MailDeliveryTaskReportDocument taskReportDocument = taskReportRepository.findOne(task.getTaskId());
        if (taskReportDocument == null) {
            taskReportDocument = new MailDeliveryTaskReportDocument();
            taskReportDocument.setId(task.getTaskId());
            taskReportDocument.setTaskName(task.getName());
            taskReportDocument.setCreatedDate(DateTime.now());
            UserDocument userDocument = new UserDocument(task.getUser().getId(),
                    task.getUser().getUsername(), task.getParentUserId(), task.getParentUsername());
            taskReportDocument.setUser(userDocument);
            MailDeliveryChannelDocument deliveryChannelDocument =
                    new MailDeliveryChannelDocument(task.getDeliveryChannel().getId(),
                            task.getDeliveryChannel().getName(), task.getDeliveryChannelNode().getId(),
                            task.getDeliveryChannelNode().getName());
            taskReportDocument.setDeliveryChannel(deliveryChannelDocument);
        } else if (isInterfaceChannel(task.getDeliveryChannelNode().getServerKey())) {
            recipientActionRepository.deleteByTaskId(task.getTaskId());
            LOGGER.debug("Deleted expired recipient actions to elasticsearch, task id: {}.", task.getTaskId());
        }
        BeanUtils.copyProperties(taskReport, taskReportDocument);
        taskReportDocument.setLastModifiedDate(DateTime.now());
        taskReportRepository.save(taskReportDocument);
        LOGGER.debug("Saved task report to elasticsearch, task id: {}, last modified date: {}.", task.getTaskId(),
                taskReportDocument.getLastModifiedDate());
    }

    public List<MailRecipientAction> getRecipientActions(UserMailDeliveryTask task) {
        List<MailRecipientActionDocument> recipientActionDocuments =
                recipientActionRepository.findAllByTaskId(task.getTaskId());
        List<MailRecipientAction> recipientActions = null;
        if (recipientActionDocuments != null && recipientActionDocuments.size() > 0) {
            recipientActions = new ArrayList<>(recipientActionDocuments.size());
            for (MailRecipientActionDocument recipientActionDocument : recipientActionDocuments) {
                MailRecipientAction recipientAction = new MailRecipientAction();
                BeanUtils.copyProperties(recipientActionDocument, recipientAction);
                recipientActions.add(recipientAction);
            }
        }
        return recipientActions;
    }

    public void saveRecipientActionsCore(UserMailDeliveryTask task, List<MailRecipientAction> recipientActions) {
        if (CollectionUtils.isEmpty(recipientActions)) {
            return;
        }
        LOGGER.debug("Saving recipient actions to elasticsearch, task id: {}.", task.getTaskId());
        List<MailRecipientActionDocument> recipientActionDocuments =
                createMailRecipientActionDocuments(task.getTaskId(), task.getName(), recipientActions);
        recipientActionRepository.save(recipientActionDocuments);
        LOGGER.debug("Saved {} recipient actions to elasticsearch, task id: {}.", recipientActionDocuments.size(),
                task.getTaskId());
    }

    private List<MailRecipientActionDocument> createMailRecipientActionDocuments(String taskId, String taskName,
            Collection<MailRecipientAction> recipientActions) {
        List<MailRecipientActionDocument> recipientActionDocuments = new ArrayList<>(recipientActions.size());
        for (MailRecipientAction recipientAction : recipientActions) {
            MailRecipientActionDocument recipientActionDocument = new MailRecipientActionDocument();
            recipientActionDocument.setEmail(recipientAction.getEmail());
            recipientActionDocument.setActionDate(recipientAction.getActionDate());
            recipientActionDocument.setActionStatus(recipientAction.getActionStatus());
            recipientActionDocument.setDescription(recipientAction.getDescription());
            recipientActionDocument.setIp(recipientAction.getIp());
            recipientActionDocument.setAddress(recipientAction.getAddress());
            recipientActionDocument.setBrowser(recipientAction.getBrowser());
            recipientActionDocument.setOs(recipientAction.getOs());
            recipientActionDocument.setTaskId(taskId);
            recipientActionDocument.setTaskName(taskName);
            recipientActionDocuments.add(recipientActionDocument);
        }
        return recipientActionDocuments;
    }
}
