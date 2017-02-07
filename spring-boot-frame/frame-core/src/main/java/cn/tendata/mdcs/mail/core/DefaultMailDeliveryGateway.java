package cn.tendata.mdcs.mail.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;

import cn.tendata.mdcs.core.cache.CacheNames;
import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.mail.MailDeliveryTaskReport;
import cn.tendata.mdcs.mail.MailLink;
import cn.tendata.mdcs.mail.MailRecipientAction;
import cn.tendata.mdcs.mail.MailRecipientAction.MailRecipientActionStatus;

public class DefaultMailDeliveryGateway implements MailDeliveryGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMailDeliveryGateway.class);
    
    private final MailDeliveryTaskDispatcher taskDispatcher;
    private final MailDeliveryServerProxyManager serverProxyManager;
    
    public DefaultMailDeliveryGateway(MailDeliveryTaskDispatcher taskDispatcher, 
            MailDeliveryServerProxyManager serverProxyManager) {
        Assert.notNull(taskDispatcher, "'taskDispatcher' must not be null");
        Assert.notNull(serverProxyManager, "'serverProxyManager' must not be null");
        this.taskDispatcher = taskDispatcher;
        this.serverProxyManager = serverProxyManager;
    }

    public void process(UserMailDeliveryTask task) {
        LOGGER.debug("Processing task, task id:{}.", task.getId());
        final MailDeliveryTaskData taskData = new MailDeliveryTaskData(task);
        taskDispatcher.dispatch(taskData);
    }
    
    @Cacheable(cacheNames = CacheNames.MAIL_DELIVERY_TASK_REPORTS, key = "#task.id")
    public MailDeliveryTaskReport getReport(UserMailDeliveryTask task) {
        return serverProxyManager.getReport(task);
    }

    @Cacheable(cacheNames = CacheNames.MAIL_RECIPIENT_ACTIONS, key = "#task.id+':'+#email")
    public MailRecipientAction getRecipientAction(UserMailDeliveryTask task, String email) {
        return serverProxyManager.getRecipientAction(task, email);
    }
    
    @Cacheable(cacheNames = CacheNames.MAIL_RECIPIENT_ACTIONS, key = "#task.id")
    public List<MailRecipientAction> getRecipientActions(UserMailDeliveryTask task) {
        return serverProxyManager.getRecipientActions(task);
    }

    @Cacheable(cacheNames = CacheNames.MAIL_RECIPIENT_ACTIONS, key = "#task.id+':'+#startIdx+':'+#endIdx")
    public List<MailRecipientAction> getRecipientActions(UserMailDeliveryTask task, int startIdx, int endIdx) {
        return serverProxyManager.getRecipientActions(task, startIdx, endIdx);
    }

    @Cacheable(cacheNames = CacheNames.MAIL_RECIPIENT_ACTIONS, key = "#task.id+':'+#actionStatus+':'+#startIdx+':'+#endIdx")
    public List<MailRecipientAction> getRecipientActions(UserMailDeliveryTask task, MailRecipientActionStatus actionStatus, 
            int startIdx, int endIdx) {
        return serverProxyManager.getRecipientActions(task, actionStatus, startIdx, endIdx);
    }

    @Cacheable(cacheNames = CacheNames.MAIL_LINKS, key = "#task.id")
    public List<MailLink> getLinks(UserMailDeliveryTask task) {
        LOGGER.debug("Getting links, task id:{}.", task.getId());
        return serverProxyManager.getLinks(task);
    }
}
