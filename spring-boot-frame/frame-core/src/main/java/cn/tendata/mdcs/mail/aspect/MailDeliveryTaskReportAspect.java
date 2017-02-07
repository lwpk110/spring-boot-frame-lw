package cn.tendata.mdcs.mail.aspect;

import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.mail.MailDeliveryTaskReport;
import cn.tendata.mdcs.mail.MailRecipientAction;
import cn.tendata.mdcs.mail.core.MailDeliveryTaskReportManager;
import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Aspect
public class MailDeliveryTaskReportAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailDeliveryTaskReportAspect.class);

    @Autowired MailDeliveryTaskReportManager taskReportManager;

    @Around(value = "execution(* cn.tendata.mdcs.mail.core.MailDeliveryGateway.getReport(..)) && "
            + "args(task)", argNames = "task")
    public Object getMailDeliveryTaskReport(final ProceedingJoinPoint pjp, final UserMailDeliveryTask task)
            throws Throwable {
        LOGGER.debug("Getting task report, task id: {}.", task.getId());

        MailDeliveryTaskReport savedTaskReport = taskReportManager.getTaskReport(task);
       /* MailDeliveryTaskReport savedTaskReport = null;*/

        if (savedTaskReport != null) {
            return savedTaskReport;
        }
        final MailDeliveryTaskReport taskReport = (MailDeliveryTaskReport) pjp.proceed();
        taskReportManager.saveTaskReport(task, taskReport);
        return taskReport;
    }

    @SuppressWarnings("unchecked")
    @Around(value = "execution(* cn.tendata.mdcs.mail.core.MailDeliveryGateway.getRecipientActions(..)) && "
            + "args(task)", argNames = "task")
    public Object getMailRecipientActions(final ProceedingJoinPoint pjp, final UserMailDeliveryTask task)
            throws Throwable {
        LOGGER.debug("Getting recipient actions, task id: {}.", task.getId());
        List<MailRecipientAction> savedRecipientActions = taskReportManager.getRecipientActions(task);
        if (savedRecipientActions != null) {
            return savedRecipientActions;
        }
        final List<MailRecipientAction> recipientActions = (List<MailRecipientAction>) pjp.proceed();
        taskReportManager.saveRecipientActions(task, recipientActions);
        return recipientActions;
    }
}
