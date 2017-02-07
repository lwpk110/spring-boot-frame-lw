package cn.tendata.mdcs.mail.core;

import java.util.List;

import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.mail.MailDeliveryTaskReport;
import cn.tendata.mdcs.mail.MailLink;
import cn.tendata.mdcs.mail.MailRecipientAction;
import cn.tendata.mdcs.mail.MailRecipientAction.MailRecipientActionStatus;

public interface MailDeliveryGateway {

    void process(UserMailDeliveryTask task);
    
    MailDeliveryTaskReport getReport(UserMailDeliveryTask task);
    
    MailRecipientAction getRecipientAction(UserMailDeliveryTask task, String email);
    
    List<MailRecipientAction> getRecipientActions(UserMailDeliveryTask task);
    
    List<MailRecipientAction> getRecipientActions(UserMailDeliveryTask task, int startIdx, int endIdx);
    
    List<MailRecipientAction> getRecipientActions(UserMailDeliveryTask task, MailRecipientActionStatus actionStatus, 
            int startIdx, int endIdx);
    
    List<MailLink> getLinks(UserMailDeliveryTask task);
}
