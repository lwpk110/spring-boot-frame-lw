package cn.tendata.mdcs.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.data.elasticsearch.domain.MailRecipientActionDocument;
import cn.tendata.mdcs.data.elasticsearch.repository.MailRecipientActionRepository;
import cn.tendata.mdcs.data.repository.UserMailDeliveryTaskPredicates;
import cn.tendata.mdcs.data.repository.UserMailDeliveryTaskRepository;
import cn.tendata.mdcs.mail.MailRecipientAction;
import cn.tendata.mdcs.mail.MailRecipientAction.MailRecipientActionStatus;
import cn.tendata.mdcs.service.model.MailDeliveryBounceReport;
import cn.tendata.mdcs.util.CollectionUtils;

public class DefaultMailDeliveryBounceReportManager implements MailDeliveryBounceReportManager {

    private final MailRecipientActionRepository recipientActionRepository;
    private final UserMailDeliveryTaskRepository userMailDeliveryTaskRepository;

    public DefaultMailDeliveryBounceReportManager(MailRecipientActionRepository recipientActionRepository,
                                                  UserMailDeliveryTaskRepository userMailDeliveryTaskRepository) {

        this.recipientActionRepository = recipientActionRepository;
        this.userMailDeliveryTaskRepository = userMailDeliveryTaskRepository;
    }

    public List<MailDeliveryBounceReport> getRecipientBounceSummaryReport(DateTime start, DateTime end) {

        List<MailDeliveryBounceReport> rpts = new ArrayList<MailDeliveryBounceReport>();
        List<UserMailDeliveryTask> tasks = getAllUserMailDeliveryTaskFromPerDay(start, end);
        Map<String, MailDeliveryBounceReport> dataMap = new HashMap<String, MailDeliveryBounceReport>();

        if (null != tasks && !tasks.isEmpty()) {
            for (int k = 0; k < tasks.size(); k++) {
                String taskId = tasks.get(k).getId().toString();
                List<MailRecipientActionDocument> docs = recipientActionRepository.findAllByTaskId(taskId);
                if (null != docs && !docs.isEmpty()) {
                    for (int i = 0; i < docs.size(); i++) {
                        MailRecipientActionStatus actionStatus = docs.get(i).getActionStatus();
                        String email = docs.get(i).getEmail();
                        if (StringUtils.isNotEmpty(email)) {
                            int indexOf = email.indexOf("@");
                            if (indexOf > 0) {
                                String subMail = email.substring(indexOf + 1);
                                MailDeliveryBounceReport msReport = null;
                                if (dataMap.containsKey(subMail)) {
                                    msReport = this.mailSuffixReportHandle(dataMap.get(subMail), actionStatus);
                                } else {
                                    msReport = new MailDeliveryBounceReport(actionStatus, subMail);
                                }
                                dataMap.put(subMail, msReport);
                            }
                        }
                    }
                }
            }
        }

        for (String key : dataMap.keySet()) {
            MailDeliveryBounceReport mailSuffixReportEntity = dataMap.get(key);
            Integer middleNum = new Integer(10000)
                    * (mailSuffixReportEntity.getHardBounce() + mailSuffixReportEntity.getSoftBounce());
            Integer lastNum = middleNum / (mailSuffixReportEntity.getTotal());
            mailSuffixReportEntity.setBounceRate(lastNum / new Integer(100));
            rpts.add(mailSuffixReportEntity);

        }

        Collections.sort(rpts, new Comparator<MailDeliveryBounceReport>() {
            public int compare(MailDeliveryBounceReport o1, MailDeliveryBounceReport o2) {
                if (o2.getBounceRate() > o1.getBounceRate()) {
                    return 1;
                }
                if (o2.getBounceRate() < o1.getBounceRate()) {
                    return -1;
                }
                return 0;
            }
        });
        // 根据退信率排序
        return rpts;
    }

    private MailDeliveryBounceReport mailSuffixReportHandle(MailDeliveryBounceReport mr,
                                                            MailRecipientActionStatus actionStatus) {
        if (actionStatus.equals(MailRecipientActionStatus.HARD_BOUNCE)) {
            mr.setHardBounce(mr.getHardBounce() + 1);
        }
        if (actionStatus.equals(MailRecipientActionStatus.SOFT_BOUNCE)) {
            mr.setSoftBounce(mr.getSoftBounce() + 1);
        }
        mr.setTotal(mr.getTotal() + 1);
        return mr;
    }

    private List<UserMailDeliveryTask> getAllUserMailDeliveryTaskFromPerDay(DateTime start, DateTime end) {
        if (start.isAfter(end)) {
            return null;
        }

        List<UserMailDeliveryTask> tasks = new ArrayList<UserMailDeliveryTask>();
        start = start.withTimeAtStartOfDay();
        DateTime dayEnd = start.plusHours(23).plusMinutes(59).plusSeconds(59);
        do {
            dayEnd = dayEnd.isAfter(end) ? end : dayEnd;

            List<UserMailDeliveryTask> userMailDeliveryTaskPerList = CollectionUtils
                    .toList(userMailDeliveryTaskRepository.findAll(UserMailDeliveryTaskPredicates.list(start, dayEnd)));
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(userMailDeliveryTaskPerList)) {
                tasks.addAll(userMailDeliveryTaskPerList);
            }
            start = start.plusDays(1);
            dayEnd = dayEnd.plusDays(1);

        } while (!start.isAfter(end));

        return tasks;

    }

    public List<MailRecipientAction> getAllBounceByRecipientsMailSuffix(String recipientsMailSuffix, DateTime start, DateTime end) {

        List<UserMailDeliveryTask> tasks = getAllUserMailDeliveryTaskFromPerDay(start, end);
        List<MailRecipientAction> recipientBounceList = new ArrayList<MailRecipientAction>();
        if (!org.springframework.util.CollectionUtils.isEmpty(tasks)) {
            for (int k = 0; k < tasks.size(); k++) {
                String taskId = tasks.get(k).getId().toString();
                recipientBounceList = this.getMailRecipientActionListByTaskId(taskId, recipientBounceList, recipientsMailSuffix);
            }
        }
        return recipientBounceList;
    }

    private List<MailRecipientAction> getMailRecipientActionListByTaskId(String taskId, List<MailRecipientAction> recipientBounceList, String recipientsMailSuffix) {
        if (null == recipientBounceList) {
            recipientBounceList = new ArrayList<MailRecipientAction>();
        }
        List<MailRecipientActionDocument> docs = recipientActionRepository.findAllByTaskId(taskId);
        if (!org.springframework.util.CollectionUtils.isEmpty(docs)) {
            for (int i = 0; i < docs.size(); i++) {
                String email = docs.get(i).getEmail();
                if (StringUtils.isNotEmpty(email) && email.endsWith(recipientsMailSuffix)) {
                    MailRecipientAction mailRecipientAction = this.assembleMailRecipientAction(docs.get(i));
                    if (null != mailRecipientAction) {
                        recipientBounceList.add(mailRecipientAction);
                    }
                }
            }
        }
        return recipientBounceList;
    }

    private MailRecipientAction assembleMailRecipientAction(MailRecipientActionDocument docs) {
        MailRecipientActionStatus acStatus = docs.getActionStatus();
        boolean needShow = false;
        MailRecipientAction mailRecipientAction = new MailRecipientAction();
        if (acStatus.equals(MailRecipientActionStatus.HARD_BOUNCE)) {
            mailRecipientAction.setActionStatus(MailRecipientActionStatus.HARD_BOUNCE);
            needShow = true;

        } else if (acStatus.equals(MailRecipientActionStatus.SOFT_BOUNCE)) {
            mailRecipientAction.setActionStatus(MailRecipientActionStatus.SOFT_BOUNCE);
            needShow = true;
        }
        if (needShow) {
            mailRecipientAction.setEmail(docs.getEmail());
            mailRecipientAction.setDescription(docs.getDescription());
            mailRecipientAction.setActionDate(docs.getActionDate());
            return mailRecipientAction;
        }
        return null;
    }

}
