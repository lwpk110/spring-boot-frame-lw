package cn.tendata.ftp.webpower.core;

import cn.tendata.ftp.webpower.config.sftp.WebpowerSftpOutAdapterConfig.SendToSftpGateway;
import cn.tendata.ftp.webpower.model.WebpowerBatchProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Description;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by ernest on 2016/8/23.
 */
public class DefaultSftpChannelGateway implements RemoteServerChannelGateway {

    private Logger logger = LoggerFactory.getLogger(DefaultSftpChannelGateway.class);
    private final WebpowerBatchProperties webpowerBatchProperties;
    private final SendToSftpGateway sendToSftpGateway;
    private final SftpRemoteFileTemplate sftpRemoteFileTemplate;

    public DefaultSftpChannelGateway(SendToSftpGateway sendToSftpGateway,
                                     SftpRemoteFileTemplate sftpRemoteFileTemplate,
                                     WebpowerBatchProperties webpowerBatchProperties) {
        this.sendToSftpGateway = sendToSftpGateway;
        this.sftpRemoteFileTemplate = sftpRemoteFileTemplate;
        this.webpowerBatchProperties = webpowerBatchProperties;
    }

    @Override
    public void downloadFile() {

    }

    @Override
    public String uploadFile(String filePath) {
        sendToSftpGateway.sendToSftp(new File(filePath));
        return retryPolicy(filePath);
    }

    @Retryable(value = {TimeoutException.class}, backoff = @Backoff(delay = 5000L))
    @Description("文件不存在进行重试操作！！")
    private String retryPolicy(String filePath) {
        try {
            String[] resArr = this.sftpRemoteFileTemplate.getSessionFactory()
                    .getSession()
                    .listNames("upload/" + filePath.substring(filePath.lastIndexOf("/") + 1));
            if (resArr.length > 0) {
                logger.info("【" + filePath + "】文件已在sftp服务器上存在！！");
                return getFtpFilePath(filePath);
            } else {
                logger.info("【" + filePath + "】文件在sftp服务器上不存在！！，即将重试。。。");
                throw new TimeoutException();
            }
        } catch (IOException | TimeoutException ignored) {
        }

        return "";
    }

    private String getFtpFilePath(String filePath) {
        return "sftp://" + webpowerBatchProperties.getSftpLoginUser()
                + ":" + webpowerBatchProperties.getSftpLoginPassword()
                + "@" + webpowerBatchProperties.getSftpHost()
                + ":" + webpowerBatchProperties.getSftpPort()
                + "/" + webpowerBatchProperties.getSftpDirRemoteUpload()
                + "/" + filePath.substring(filePath.lastIndexOf("/") + 1);
    }
}
