package cn.tendata.ftp.webpower.ftp;

import java.io.File;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.integration.file.remote.handler.FileTransferringMessageHandler;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by ernest on 2016/9/23.
 */
@Configuration
@EnableIntegration
@IntegrationComponentScan
@ContextConfiguration
public class TestOutboudGatewayConfig {
    @Bean
    public SessionFactory<FTPFile> ftpSessionFactory() {
        DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
        sf.setHost("192.168.0.103");
        sf.setPort(21);
        sf.setUsername("test1");
        sf.setPassword("test1");
        return new CachingSessionFactory<FTPFile>(sf);
    }

    @Bean
    @ServiceActivator(inputChannel = "ftpChannel" , outputChannel = "toFtpChannel")
    public MessageHandler handler() {
        FileTransferringMessageHandler handler = new FileTransferringMessageHandler(ftpSessionFactory());
        handler.setRemoteDirectoryExpression(new LiteralExpression("upload"));
        handler.setFileNameGenerator(new FileNameGenerator() {

            @Override
            public String generateFileName(Message<?> message) {
                return "handlerContent.test";
            }
        });
        return handler;
    }

    @Bean(name = "toFtpChannel")
    public PollableChannel inputChannel() {
        QueueChannel channel = new QueueChannel();
        return channel;
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {

        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(20));
        return pollerMetadata;
    }
    @MessagingGateway
    public interface MyGateway {

        @Gateway(requestChannel = "toFtpChannel")
        void sendToFtp(File file);
    }
}
