package cn.tendata.ftp.webpower.core;

import cn.tendata.ftp.webpower.model.WebpowerBatchProperties;
import cn.tendata.ftp.webpower.util.MailReportBatchJobBackFileUtil;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;

/**
 * Created by ernest on 2016/9/2.
 */
public class BatchJobListener implements JobExecutionListener {
    private final Logger log = LoggerFactory.getLogger(BatchJobListener.class);
    @Autowired
    private WebpowerBatchProperties webpowerBatchProperties;

    public static HashSet<String> taskIdSet = new HashSet<>();

    private final WebPowerMailTaskReportSaveHandler webPowerReportSaveHandler;

    public BatchJobListener(WebPowerMailTaskReportSaveHandler webPowerReportSaveHandler) {
        this.webPowerReportSaveHandler = webPowerReportSaveHandler;
    }

    long startTime;
    long endTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
        taskIdSet.clear(); //批处理前清空taskid收集器
        log.info("webPower batch start..");
    }

    @Override
    @Retryable
    public void afterJob(JobExecution jobExecution) {
        log.info("webPower batch end..");
        log.info("taskid set contains:" + taskIdSet.toString());

        MailReportBatchJobBackFileUtil.backFiles(jobExecution,
            webpowerBatchProperties.getBatchDirErr(), webpowerBatchProperties.getBatchDirSuccess());
        this.totalReport();
    }



    private void totalReport() {
        log.info("webPower batch total begin..");
        if (taskIdSet.size() > 0) {
            for (String taskId : taskIdSet) {
                try {
                    webPowerReportSaveHandler.saveReport(taskId);
                } catch (Exception e) {
                    log.error("saving task report for task id:{} has errs,err msg:{}", taskId,
                        e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

}
