package cn.tendata.ftp.webpower.util;

import cn.tendata.ftp.webpower.model.WebpowerBatchProperties;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;

/**
 * Created by ernest on 2017/1/20.
 */
public class MailReportBatchJobBackFileUtil {

    private static final Logger log = LoggerFactory.getLogger(MailReportBatchJobBackFileUtil.class);

    public static void backFiles(JobExecution jobExecution, String errDir, String successDir) {
        JobParameters jobParameters = jobExecution.getJobParameters();
        String batchFile = jobParameters.getString(WebpowerBatchProperties.KEY_FILE_PATH);

        BatchStatus executionStatus = jobExecution.getStatus();
        log.info("batch job executionStatus:" + executionStatus);
        try {
            if (executionStatus.isUnsuccessful()) {
                log.error(
                    "err msg :{}" + jobExecution.getAllFailureExceptions().get(0).getMessage());
                backFiles(batchFile, errDir);
            } else if (executionStatus == BatchStatus.COMPLETED) {
                backFiles(batchFile, successDir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void renamefile(String oldFileName, String newFileName) throws IOException {
        try {
            FileUtils.copyFile(new File(oldFileName), new File(newFileName));
            deleteSourceFile(oldFileName);
        } catch (IOException e) {
            log.error("重命名文件名称出现错误..,错误信息：{}", e.getMessage());
            throw new IOException("重命名文件名称出现错误..，[" + e.getMessage() + "]");
        }
    }

    public static void backFiles(String filePath, String dirErr) throws IOException {
        try {
            backReportFiles(filePath, dirErr);
            deleteSourceFile(filePath);
        } catch (IOException e) {
            log.error("备份文件出现错误...,错误信息：{}", e.getMessage());
            throw new IOException("备份文件出现错误..，[" + e.getMessage() + "]");
        }
    }

    //copy
    private static void backReportFiles(String backReportFilePath, String backDir)
        throws IOException {
        File backReportFileDir = new File(backDir);      //e://webPowerReportBack
        if (!backReportFileDir.exists() && !backReportFileDir.isDirectory()) {
            log.info("[" + backReportFileDir + "] directory is not exist,will create new");
            backReportFileDir.mkdir();
        }
        FileUtils.copyFileToDirectory(new File(backReportFilePath), backReportFileDir);
    }

    //delete
    private static void deleteSourceFile(String backReportFilePath) {
        FileUtils.deleteQuietly(new File(backReportFilePath));
    }
}
