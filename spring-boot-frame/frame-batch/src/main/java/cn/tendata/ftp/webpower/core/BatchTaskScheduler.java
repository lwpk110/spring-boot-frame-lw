package cn.tendata.ftp.webpower.core;

import cn.tendata.ftp.webpower.model.WebpowerBatchProperties;
import cn.tendata.ftp.webpower.util.MailReportBatchJobBackFileUtil;
import cn.tendata.mdcs.util.CollectionUtils;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by ernest on 2016/9/5.
 */
public class BatchTaskScheduler {

    private final Logger log = LoggerFactory.getLogger(BatchTaskScheduler.class);
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    Job reportJob;
    @Autowired
    private WebpowerBatchProperties webpowerBatchProperties;

    private Object lock = new Object();

    @Scheduled(cron = "0 40 2 * * ?")
    public void scheduleExecute() throws JobParametersInvalidException,
        JobExecutionAlreadyRunningException,
        JobRestartException,
        JobInstanceAlreadyCompleteException, IOException {
        synchronized (lock) {
            this.executeBatch();
        }
    }

    public void executeBatch() throws IOException, JobParametersInvalidException {
        List<File> files = this.getFiles(webpowerBatchProperties.getSftpLocalDirSynchro());
        if (files.size() > 0) {
            for (File file : files) {
                String filePath = file.getAbsolutePath();
                log.info("The need to deal with the CSV report file is：[" + filePath + "]");
                JobParameters jobParameters = new JobParametersBuilder()
                    //.addLong("time", System.currentTimeMillis())
                    .addString(WebpowerBatchProperties.KEY_FILE_PATH, filePath)
                    .toJobParameters();
                try {
                    jobLauncher.run(reportJob, jobParameters);
                } catch (JobExecutionAlreadyRunningException e) {
                    log.warn("file：{} is running...",filePath);
                    e.printStackTrace();
                } catch (JobRestartException e) {
                    e.printStackTrace();
                } catch (JobInstanceAlreadyCompleteException e) {
                    String newName = filePath + new DateTime().toString("yyyyMMddHHmmss");
                    String backDir = webpowerBatchProperties.getBatchDirErr();
                    this.completedErrBack(filePath, newName, backDir);
                    log.warn("file：{} has been completed，will not re-Run,file will backed in ：{} ", filePath, backDir);
                    e.printStackTrace();
                }
            }
        }
    }

    private List<File> getFiles(String pathDir) {
        Collection<File> files;
        files = FileUtils.listFiles(new File(pathDir), WebpowerBatchProperties.SUFFIXES_SEARCH_FILT,
            false);
        return CollectionUtils.toList(files);
    }

    private void completedErrBack(String filePath, String newFileName, String backDir)
        throws IOException {
        MailReportBatchJobBackFileUtil.renamefile(filePath, newFileName);
        MailReportBatchJobBackFileUtil.backFiles(newFileName, backDir);
    }
}
