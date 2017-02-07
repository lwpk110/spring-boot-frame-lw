package cn.tendata.ftp.webpower.config.sftp;

import cn.tendata.ftp.webpower.model.WebpowerBatchProperties;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.filters.FileListFilter;
import org.springframework.integration.file.filters.FileSystemPersistentAcceptOnceFileListFilter;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizer;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizingMessageSource;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.io.File;

/**
 * ftp config class.
 */
@Configuration
@EnableIntegration
@IntegrationComponentScan
public class WebpowerSftpInAdapterConfig {
    @Autowired
    private WebpowerBatchProperties webpowerBatchProperties;

    @Bean(name = "sftpChannel")
    public MessageChannel messageChannel() {
        return new DirectChannel();
    }

    @Bean
    public SftpInboundFileSynchronizer sftpInboundFileSynchronizer(SessionFactory<LsEntry> sftpSessionFactory, FileListFilter<LsEntry> compositeFilter) {
        SftpInboundFileSynchronizer fileSynchronizer = new SftpInboundFileSynchronizer(sftpSessionFactory);
        fileSynchronizer.setDeleteRemoteFiles(false);
        fileSynchronizer.setRemoteDirectory(webpowerBatchProperties.getSftpDirRemoteDownload());
        fileSynchronizer.setFilter(compositeFilter);
        return fileSynchronizer;
    }

    @Bean
    @InboundChannelAdapter(value = "sftpChannel", poller = @Poller(fixedDelay = WebpowerBatchProperties.POLLER_FIX_RATE))
    public MessageSource<File> sftpMessageSource(SessionFactory<LsEntry> sftpSessionFactory,
                                                 FileSystemPersistentAcceptOnceFileListFilter fileSystemPersistentAcceptOnceFileListFilter,
                                                 FileListFilter<LsEntry> compositeFilter) {
        SftpInboundFileSynchronizingMessageSource source =
                new SftpInboundFileSynchronizingMessageSource(sftpInboundFileSynchronizer(sftpSessionFactory, compositeFilter));
        source.setLocalDirectory(new File(webpowerBatchProperties.getSftpLocalDirSynchro()));
        source.setAutoCreateLocalDirectory(true);
        source.setLocalFilter(fileSystemPersistentAcceptOnceFileListFilter);
        return source;
    }

    @Bean(name = "inboundHandler")
    @ServiceActivator(inputChannel = "sftpChannel")
    public MessageHandler handler() {
        return message -> System.out.println("sftp 同步消息：" + message.getPayload());
    }

}
