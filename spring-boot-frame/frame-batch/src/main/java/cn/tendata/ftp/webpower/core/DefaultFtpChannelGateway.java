package cn.tendata.ftp.webpower.core;

import cn.tendata.ftp.webpower.model.FtpServerDto;
import cn.tendata.ftp.webpower.util.WebpowerFtpProperty;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Description;
import org.springframework.core.env.Environment;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import static org.junit.Assert.assertNotNull;

/**
 * Created by ernest on 2016/8/23.
 */
public class DefaultFtpChannelGateway implements RemoteServerChannelGateway {

    private Logger logger = LoggerFactory.getLogger(RemoteServerChannelGateway.class);

    private final MessageSource messageSource;
    private final AbstractMessageHandler messageHandler;
    private final FtpServerDto ftpServerDto;
    private final FtpRemoteFileTemplate ftpRemoteFileTemplate;

    public DefaultFtpChannelGateway(MessageSource messageSource, AbstractMessageHandler messageHandler,
            FtpRemoteFileTemplate remoteFileTemplate,
            SessionFactory<FTPFile> sessionFactory,
            Environment env) {

        this.messageSource = messageSource;
        this.messageHandler = messageHandler;
        this.ftpRemoteFileTemplate = remoteFileTemplate;

        this.ftpServerDto = new FtpServerDto();
        ftpServerDto.setHost(env.getProperty("ftp.server-host"));
        ftpServerDto.setUser(env.getProperty("ftp.server-login-user"));
        ftpServerDto.setPassword(env.getProperty("ftp.server-login-password"));
        ftpServerDto.setPort(Integer.parseInt(env.getProperty("ftp.server-port")));
    }

    @Override
    public void downloadFile() {
        Message<?> message = messageSource.receive();
        assertNotNull("download message is null", message);
    }

    @Override
    public String uploadFile(String filePath) {
        Message<File> fileMessage = MessageBuilder.withPayload(new File(filePath)).build();
        assertNotNull("file message is null", fileMessage);
        this.messageHandler.handleMessage(fileMessage);
        return retryPolicy(filePath);
    }

    @Retryable(value = {TimeoutException.class}, backoff = @Backoff(delay = 5000L), maxAttempts = 3)
    @Description("处理数目进行重试操作")
    public String retryPolicy_1(String filePath) {
        int handleCount = this.messageHandler.getHandleCount();
        if (handleCount == 0) {
            try {
                throw new TimeoutException();
            } catch (Exception e) {
            }
        } else {
            return getFtpFilePath(filePath);
        }
        return "";
    }

    @Retryable(value = {TimeoutException.class}, backoff = @Backoff(delay = 5000L), maxAttempts = 3)
    @Description("文件不存在进行重试操作！！")
    public String retryPolicy(String filePath) {
        try {
            String[] resArr = this.ftpRemoteFileTemplate.getSessionFactory()
                    .getSession()
                    .listNames("upload/" + filePath.substring(filePath.lastIndexOf("/") + 1));
            if (resArr.length > 0) {
                logger.info("【" + filePath + "】文件已在ftp服务器上存在！！");
                return getFtpFilePath(filePath);
            } else {
                logger.info("【" + filePath + "】文件在ftp服务器上不存在存在！！，即将重试。。。");
                throw new TimeoutException();
            }
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (TimeoutException e) {
            //e.printStackTrace();
        }

        return "";
    }

    public String getFtpFilePath(String filePath) {
        return "ftp://" + ftpServerDto.getUser()
                + ":" + ftpServerDto.getPassword()
                + "@" + ftpServerDto.getHost()
                + ":" + ftpServerDto.getPort()
                + "/" + WebpowerFtpProperty.REMOTE_PUT_DIRECTORY
                + "/" + filePath.substring(filePath.lastIndexOf("/") + 1);
    }
}
