package cn.tendata.mdcs.mail.core;

import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.mail.MailDeliveryTaskReport;
import cn.tendata.mdcs.mail.MailLink;
import cn.tendata.mdcs.mail.MailRecipientAction;
import cn.tendata.mdcs.mail.MailRecipientAction.MailRecipientActionStatus;
import java.util.List;
import java.util.Map;

public abstract class MailDeliveryServerProxy {

   /* private final String serverKey;
    private final Map<String, MailDeliveryServerHandler<?>> handlerMap;

    public final String getServerKey() {
        return serverKey;
    }*/

    protected MailDeliveryServerProxy(/*String serverKey, Collection<? extends MailDeliveryServerHandler<?>> handlers*/) {
      /*  Assert.hasText(serverKey, "'serverKey' must not be empty");
        Assert.notEmpty(handlers, "'handlers' must have elements");
        this.serverKey = serverKey;
        this.handlerMap = new HashMap<>(handlers.size());
        for (MailDeliveryServerHandler<?> handler : handlers) {
            handlerMap.put(handler.getClass().getName(), handler);
        }*/
    }

    public abstract boolean batchSend(MailDeliveryTaskData taskData, Map<String, Object> configProps);

    public abstract MailDeliveryTaskReport getReport(UserMailDeliveryTask task, Map<String, Object> configProps);

    public abstract MailRecipientAction getRecipientAction(String taskId, String email, Map<String, Object> configProps);

    public abstract List<MailRecipientAction> getRecipientActions(String taskId, int startIdx, int endIdx, Map<String, Object> configProps);

    public abstract List<MailRecipientAction> getRecipientActions(String taskId, MailRecipientActionStatus actionStatus,
                                                                  int startIdx, int endIdx, Map<String, Object> configProps);

    public abstract List<MailLink> getLinks(String taskId, Map<String, Object> configProps);

/*    @SuppressWarnings("unchecked")
    public final <T extends MailDeliveryServerHandler<?>> T getHandler(final Class<T> handlerClass) {
        if (handlerMap.containsKey(handlerClass.getName())) {
            return (T) handlerMap.get(handlerClass.getName());
        }
        return null;
    }*/
}
