package cn.tendata.mdcs.mail.core;

import org.springframework.core.task.TaskExecutor;
import org.springframework.util.Assert;

public class DefaultMailDeliveryTaskDispatcher implements MailDeliveryTaskDispatcher {

    private final MailDeliveryTaskProcessor taskProcessor;
    private final TaskExecutor taskExecutor;
    
    public DefaultMailDeliveryTaskDispatcher(MailDeliveryTaskProcessor taskProcessor, TaskExecutor taskExecutor) {
        Assert.notNull(taskProcessor, "'taskProcessor' must not be null");
        Assert.notNull(taskExecutor, "'taskExecutor' must not be null");
        this.taskProcessor = taskProcessor;
        this.taskExecutor = taskExecutor;
    }

    public final void dispatch(final MailDeliveryTaskData taskData) {
        taskExecutor.execute(new Runnable() {
            public void run() {
                taskProcessor.process(taskData);
            }
        });
    }
}
