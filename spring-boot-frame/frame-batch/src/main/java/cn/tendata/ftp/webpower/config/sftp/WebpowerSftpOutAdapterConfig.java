package cn.tendata.ftp.webpower.config.sftp;

import cn.tendata.ftp.webpower.core.DefaultSftpChannelGateway;
import cn.tendata.ftp.webpower.core.RemoteServerChannelGateway;
import cn.tendata.ftp.webpower.model.WebpowerBatchProperties;
import com.jcraft.jsch.ChannelSftp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.file.remote.handler.FileTransferringMessageHandler;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.retry.annotation.EnableRetry;

import java.io.File;

/**
 * Created by ernest on 2016/12/19.
 */
@Configuration
@EnableIntegration
@IntegrationComponentScan
@EnableRetry(proxyTargetClass = true)
public class WebpowerSftpOutAdapterConfig {
    @Autowired
    private WebpowerBatchProperties webpowerBatchProperties;

    @Bean(name = "toSftpChannel")
    public MessageChannel messageChannel() {
        return new DirectChannel();
    }

    @Bean(name = "outboundHandler")
    @ServiceActivator(inputChannel = "toSftpChannel")
    public AbstractMessageHandler handler(SessionFactory<ChannelSftp.LsEntry> sftpSessionFactory) {
        FileTransferringMessageHandler<ChannelSftp.LsEntry> handler =
                new FileTransferringMessageHandler<>(sftpSessionFactory);
        handler.setRemoteDirectoryExpression(
                new LiteralExpression(webpowerBatchProperties.getSftpDirRemoteUpload()));
        handler.setFileNameGenerator(message -> ((File) message.getPayload()).getName());
        return handler;
    }

    @MessagingGateway
    public interface SendToSftpGateway {
        @Gateway(requestChannel = "toSftpChannel")
        void sendToSftp(File file);
    }

    @Bean
    public SftpRemoteFileTemplate sftpRemoteFileTemplate(
            SessionFactory<ChannelSftp.LsEntry> sftpSessionFactory) {
        return new SftpRemoteFileTemplate(sftpSessionFactory);
    }

    @Bean
    public RemoteServerChannelGateway defaultSftpChannelGateway(SendToSftpGateway sendToSftpGateway,
                                                                SftpRemoteFileTemplate sftpRemoteFileTemplate,
                                                                WebpowerBatchProperties webpowerBatchProperties) {
        return new DefaultSftpChannelGateway(sendToSftpGateway, sftpRemoteFileTemplate,
                webpowerBatchProperties);
    }
}
