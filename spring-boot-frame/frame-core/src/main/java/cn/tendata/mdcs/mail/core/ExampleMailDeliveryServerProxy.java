package cn.tendata.mdcs.mail.core;

import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.mail.MailDeliveryTaskReport;
import cn.tendata.mdcs.mail.MailLink;
import cn.tendata.mdcs.mail.MailRecipientAction;
import cn.tendata.mdcs.mail.MailRecipientAction.MailRecipientActionStatus;
import java.util.List;
import java.util.Map;

public class ExampleMailDeliveryServerProxy extends MailDeliveryServerProxy {

 /*   protected ExampleMailDeliveryServerProxy(String serverKey,
        Collection<? extends MailDeliveryServerHandler<?>> handlers) {
        super(serverKey, handlers);
    }*/

    @Override
    public boolean batchSend(MailDeliveryTaskData taskData, Map<String, Object> configProps) {
        return false;
    }

    @Override public MailDeliveryTaskReport getReport(UserMailDeliveryTask task,
        Map<String, Object> configProps) {
        return null;
    }

    @Override public MailRecipientAction getRecipientAction(String taskId, String email,
        Map<String, Object> configProps) {
        return null;
    }

    @Override
    public List<MailRecipientAction> getRecipientActions(String taskId, int startIdx, int endIdx,
        Map<String, Object> configProps) {
        return null;
    }

    @Override public List<MailRecipientAction> getRecipientActions(String taskId,
        MailRecipientActionStatus actionStatus, int startIdx, int endIdx,
        Map<String, Object> configProps) {
        return null;
    }

    @Override public List<MailLink> getLinks(String taskId, Map<String, Object> configProps) {
        return null;
    }
}
