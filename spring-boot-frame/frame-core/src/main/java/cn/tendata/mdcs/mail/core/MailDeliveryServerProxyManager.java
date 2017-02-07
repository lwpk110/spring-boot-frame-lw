package cn.tendata.mdcs.mail.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import cn.tendata.mdcs.data.domain.MailDeliveryChannelNode;
import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.mail.MailDeliveryTaskReport;
import cn.tendata.mdcs.mail.MailLink;
import cn.tendata.mdcs.mail.MailRecipientAction;
import cn.tendata.mdcs.mail.MailRecipientAction.MailRecipientActionStatus;

public class MailDeliveryServerProxyManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailDeliveryServerProxyManager.class);
    
    private final MailDeliveryServerProxyFactory serverProxyFactory;

    public MailDeliveryServerProxyManager(MailDeliveryServerProxyFactory serverProxyFactory) {
        Assert.notNull(serverProxyFactory, "'serverProxyFactory' must not be null");
        this.serverProxyFactory = serverProxyFactory;
    }

    public boolean batchSend(MailDeliveryTaskData taskData, MailDeliveryChannelNode channelNode) {
        LOGGER.debug("Batch Sending to [{}], task id:{}, channel node: {}.", channelNode.getServerKey(), taskData.getId(), channelNode.getId());
        final MailDeliveryServerProxy serverProxy = serverProxyFactory.getServerProxy(channelNode.getServerKey());
        return serverProxy.batchSend(taskData, channelNode.getConfigProps());
    }
    
    public MailDeliveryTaskReport getReport(UserMailDeliveryTask task) {
        final MailDeliveryChannelNode channelNode = getDeliveryChannelNode(task);
        LOGGER.debug("Getting report from [{}], task id:{}.", channelNode.getServerKey(), task.getId());
        final MailDeliveryServerProxy serverProxy = serverProxyFactory.getServerProxy(channelNode.getServerKey());
        return serverProxy.getReport(task, channelNode.getConfigProps());
    }
    
    public MailRecipientAction getRecipientAction(UserMailDeliveryTask task, String email) {
        final MailDeliveryChannelNode channelNode = getDeliveryChannelNode(task);
        LOGGER.debug("Getting recipient action from [{}], task id:{}, email:{}.", channelNode.getServerKey(), task.getId(), email);
        final MailDeliveryServerProxy serverProxy = serverProxyFactory.getServerProxy(channelNode.getServerKey());
        return serverProxy.getRecipientAction(getTaskId(task), email, channelNode.getConfigProps());
    }
    
    public List<MailRecipientAction> getRecipientActions(UserMailDeliveryTask task) {
        int endIdx = task.getRecipientCollection().getRecipientCount();
        return getRecipientActions(task, 1, endIdx);
    }
    
    public List<MailRecipientAction> getRecipientActions(UserMailDeliveryTask task, int startIdx, int endIdx) {
        final MailDeliveryChannelNode channelNode = getDeliveryChannelNode(task);
        LOGGER.debug("Getting recipient actions from [{}], task id:{}, start:{}, end:{}.", channelNode.getServerKey(), task.getId(), startIdx, endIdx);
        final MailDeliveryServerProxy serverProxy = serverProxyFactory.getServerProxy(channelNode.getServerKey());
        return serverProxy.getRecipientActions(getTaskId(task), startIdx, endIdx, channelNode.getConfigProps());
    }
    
    public List<MailRecipientAction> getRecipientActions(UserMailDeliveryTask task, MailRecipientActionStatus actionStatus, 
            int startIdx, int endIdx) {
        final MailDeliveryChannelNode channelNode = getDeliveryChannelNode(task);
        LOGGER.debug("Getting recipient actions from [{}], task id:{}, action status:{}, start:{}, end:{}.", channelNode.getServerKey(),
                task.getId(), actionStatus, startIdx, endIdx);
        final MailDeliveryServerProxy serverProxy = serverProxyFactory.getServerProxy(channelNode.getServerKey());
        return serverProxy.getRecipientActions(getTaskId(task), actionStatus, startIdx, endIdx, channelNode.getConfigProps());
    }
    
    public List<MailLink> getLinks(UserMailDeliveryTask task) {
        final MailDeliveryChannelNode channelNode = getDeliveryChannelNode(task);
        LOGGER.debug("Getting links from [{}], task id:{}.", channelNode.getServerKey(), task.getId());
        final MailDeliveryServerProxy serverProxy = serverProxyFactory.getServerProxy(channelNode.getServerKey());
        return serverProxy.getLinks(getTaskId(task), channelNode.getConfigProps());
    }
    
    private MailDeliveryChannelNode getDeliveryChannelNode(UserMailDeliveryTask task){
        if(task.getDeliveryChannelNode() == null){
            throw new IllegalStateException("delivery channel node is missing, task id:" + task.getId());
        }
        return task.getDeliveryChannelNode();
    }
    
    public static String getTaskId(UserMailDeliveryTask task){
        if(StringUtils.hasText(task.getExtraTaskId())){
            return task.getExtraTaskId();
        }
        return task.getTaskId();
    }
}
