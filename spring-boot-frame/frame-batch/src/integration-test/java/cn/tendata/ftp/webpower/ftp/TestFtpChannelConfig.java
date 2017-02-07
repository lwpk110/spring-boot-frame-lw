package cn.tendata.ftp.webpower.ftp;

import java.io.File;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.expression.ValueExpression;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.remote.handler.FileTransferringMessageHandler;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.ftp.filters.FtpSimplePatternFileListFilter;
import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizer;
import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizingMessageSource;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by ernest on 2016/8/26.
 */

@Configuration
@EnableIntegration
@IntegrationComponentScan
@Import(TestFtpChannelGatewayConfig.class)
@ContextConfiguration
public class TestFtpChannelConfig {
    @Autowired
    private Environment env;

    private final String REMOTE_GET_DIRECTORY = "download";
    private final String REMOTE_PUT_DIRECTORY = "upload";
    private final String LOCAL_DOWNLOAD_DIRECTORY = "build" + File.separator + "local" + File.separator + "download";
    private final String FILE_FILTER_RULE = "*.*";
    private final String POLLER_FIX_RATE = "1000000";

    @Bean
    public SessionFactory<FTPFile> ftpSessionFactory() {
        DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
        sf.setHost(env.getProperty("ftp.serverHost"));
        sf.setUsername(env.getProperty("ftp.serverLoginUser"));
        sf.setPassword(env.getProperty("ftp.serverLoginPassword"));
        sf.setPort(Integer.valueOf(env.getProperty("ftp.serverPort")));
        return new CachingSessionFactory<FTPFile>(sf);
    }

    @Bean
    public FtpInboundFileSynchronizer ftpInboundFileSynchronizer() {
        FtpInboundFileSynchronizer fileSynchronizer = new FtpInboundFileSynchronizer(ftpSessionFactory());
        fileSynchronizer.setDeleteRemoteFiles(false);
        fileSynchronizer.setRemoteDirectory(REMOTE_GET_DIRECTORY);
        fileSynchronizer.setFilter(new FtpSimplePatternFileListFilter(FILE_FILTER_RULE));
        return fileSynchronizer;
    }

    @Bean(name = "ftpChannel")
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

    @Bean
    @InboundChannelAdapter(value = "ftpChannel", poller = @Poller(fixedRate = POLLER_FIX_RATE))
    public MessageSource<File> ftpMessageSource() {
        FtpInboundFileSynchronizingMessageSource source =
                new FtpInboundFileSynchronizingMessageSource(ftpInboundFileSynchronizer());
        source.setLocalDirectory(new File(LOCAL_DOWNLOAD_DIRECTORY));
        source.setAutoCreateLocalDirectory(true);
        source.setLocalFilter(new AcceptOnceFileListFilter<>());
        return source;
    }

    @Bean
    @ServiceActivator(inputChannel = "ftpChannel")
    public MessageHandler inBoundHandler() {
        return (message) -> {
            System.out.println(">>>>>>>>" + message.getPayload());
        };
    }

    @Bean
    public AbstractMessageHandler ftpMessageHandler() {
        FileTransferringMessageHandler<FTPFile> messageHandler =
                new FileTransferringMessageHandler<>(ftpSessionFactory());
        messageHandler.setRemoteDirectoryExpression(new ValueExpression<>(REMOTE_PUT_DIRECTORY));
        messageHandler.setCharset("utf-8");
        return messageHandler;
    }

    @MessagingGateway
    public interface MyGateway {

        @Gateway(requestChannel = "toFtpChannel") void sendToFtp(File file);
    }

    @Bean
    public FtpRemoteFileTemplate template() {
        return new FtpRemoteFileTemplate(ftpSessionFactory());
    }
}
