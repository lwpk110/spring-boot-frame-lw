package cn.tendata.mdcs.mail.core;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.scheduling.annotation.Async;

import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.mail.MailDeliveryTaskReport;
import cn.tendata.mdcs.mail.MailRecipientAction;

public abstract class AbstractMailDeliveryTaskReportManager implements MailDeliveryTaskReportManager {

    private final Set<String> watchDog = new HashSet<>(50);

    private final String[] interfaceChannelServerKeys = {"FOCUS_SEND"};

    public String[] getInterfaceChannelServerKeys() {
        return interfaceChannelServerKeys;
    }

    private long timeToLiveInMillis = 2 * 60 * 60 * 1000L;
    private long maxTimeToSyncInMillis = 30 * 24 * 60 * 60 * 1000L;
    
    public long getTimeToLiveInMillis() {
        return timeToLiveInMillis;
    }
    
    public void setTimeToLiveInMillis(long timeToLiveInMillis) {
        this.timeToLiveInMillis = timeToLiveInMillis;
    }
    
    public long getMaxTimeToSyncInMillis() {
        return maxTimeToSyncInMillis;
    }
    
    public void setMaxTimeToSyncInMillis(long maxTimeToSyncInMillis) {
        this.maxTimeToSyncInMillis = maxTimeToSyncInMillis;
    }
    
    @Async
    public final void saveTaskReport(final UserMailDeliveryTask task, final MailDeliveryTaskReport taskReport) {
        save("task_report-" + taskReport.getTaskId(), key -> saveTaskReportCore(task, taskReport));
    }
    
    @Async
    public final void saveRecipientActions(UserMailDeliveryTask task, List<MailRecipientAction> recipientActions){
        save("task_recipient_actions-" + task.getTaskId(), key -> saveRecipientActionsCore(task, recipientActions));
    }
    
    private void save(String key, SaveFunction func){
        if(watchDog.contains(key)){
            return; 
        }
        synchronized (watchDog) {
            if(watchDog.contains(key)){
                return;
            }
            try {
                watchDog.add(key);
                func.save(key);
            } finally {
                watchDog.remove(key);
            }
        }
    }
    
    protected abstract void saveTaskReportCore(UserMailDeliveryTask task, MailDeliveryTaskReport taskReport);
    protected abstract void saveRecipientActionsCore(UserMailDeliveryTask task, List<MailRecipientAction> recipientActions);
    
    public interface SaveFunction {
        
        void save(String key);
    }
}
