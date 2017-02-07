package cn.tendata.mdcs.mail.core;

import java.util.List;

import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.mail.MailDeliveryTaskReport;
import cn.tendata.mdcs.mail.MailRecipientAction;

public interface MailDeliveryTaskReportManager {

    MailDeliveryTaskReport getTaskReport(UserMailDeliveryTask task);
    
    void saveTaskReport(UserMailDeliveryTask task, MailDeliveryTaskReport taskReport);
    
    List<MailRecipientAction> getRecipientActions(UserMailDeliveryTask task);
    
    void saveRecipientActions(UserMailDeliveryTask task, List<MailRecipientAction> recipientActions);
}
