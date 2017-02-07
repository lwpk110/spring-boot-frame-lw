package cn.tendata.ftp.webpower.config.ftp;

import cn.tendata.ftp.webpower.core.DefaultFtpChannelGateway;
import cn.tendata.ftp.webpower.core.RemoteServerChannelGateway;
import cn.tendata.ftp.webpower.util.WebpowerFtpProperty;
import java.io.File;
import java.util.regex.Pattern;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.expression.ValueExpression;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.FileListFilter;
import org.springframework.integration.file.filters.FileSystemPersistentAcceptOnceFileListFilter;
import org.springframework.integration.file.remote.handler.FileTransferringMessageHandler;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.file.remote.synchronizer.AbstractInboundFileSynchronizer;
import org.springframework.integration.ftp.filters.FtpPersistentAcceptOnceFileListFilter;
import org.springframework.integration.ftp.filters.FtpRegexPatternFileListFilter;
import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizer;
import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizingMessageSource;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.integration.metadata.ConcurrentMetadataStore;
import org.springframework.integration.metadata.PropertiesPersistingMetadataStore;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.support.PeriodicTrigger;

/**
 * ftp config class.
 */
@Configuration
@EnableIntegration
@IntegrationComponentScan
@EnableRetry(proxyTargetClass = true)
public class WebpowerFtpChannelConfig {

    @Autowired
    private Environment env;

    private Logger logger = LoggerFactory.getLogger(WebpowerFtpChannelConfig.class);

    @Bean
    public SessionFactory<FTPFile> ftpSessionFactory() {
        DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
        sf.setHost(env.getProperty("ftp.server-host"));
        sf.setUsername(env.getProperty("ftp.server-login-user"));
        sf.setPassword(env.getProperty("ftp.server-login-password"));
        sf.setPort(Integer.parseInt(env.getProperty("ftp.server-port")));
        sf.setClientMode(2);  // PASSIVE_LOCAL_DATA_CONNECTION_MODE = 2   ACTIVE_LOCAL_DATA_CONNECTION_MODE = 0
        sf.setControlEncoding(WebpowerFtpProperty.FTP_CONTROL_ENCODING);
        return new CachingSessionFactory<FTPFile>(sf);
    }

    @Bean
    public AbstractInboundFileSynchronizer ftpInboundFileSynchronizer() throws Exception {
        AbstractInboundFileSynchronizer fileSynchronizer =
                new FtpInboundFileSynchronizer(ftpSessionFactory());
        fileSynchronizer.setDeleteRemoteFiles(false);
        fileSynchronizer.setRemoteDirectory(WebpowerFtpProperty.REMOTE_GET_DIRECTORY);
        fileSynchronizer.setFilter(compositeFilter());
        return fileSynchronizer;
    }

    @Bean(name = "ftpChannel")
    public PollableChannel inputChannel() {
        return new QueueChannel();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {

        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(20));
        return pollerMetadata;
    }

    @Bean
    @InboundChannelAdapter(value = "ftpChannel", poller = @Poller(fixedRate = WebpowerFtpProperty.POLLER_FIX_RATE))
    public MessageSource<File> ftpMessageSource() throws Exception {
        FtpInboundFileSynchronizingMessageSource source =
                new FtpInboundFileSynchronizingMessageSource(ftpInboundFileSynchronizer());
        source.setLocalDirectory(new File(
                WebpowerFtpProperty.LOCAL_DOWNLOAD_DIRECTORY.equals("") ? env.getProperty("ftp.local-download-dir")
                        : WebpowerFtpProperty.LOCAL_DOWNLOAD_DIRECTORY));
        source.setLocalFilter(fileSystemPersistentAcceptOnceFileListFilter());
        source.setAutoCreateLocalDirectory(true);
        return source;
    }

    @Bean
    public PropertiesPersistingMetadataStore getMetadataStore()
            throws Exception {
        PropertiesPersistingMetadataStore metadataStore = new PropertiesPersistingMetadataStore();
        metadataStore.setBaseDirectory(
                WebpowerFtpProperty.LOCAL_DOWNLOAD_DIRECTORY.equals("") ? env.getProperty("ftp.local-download-dir")
                        : WebpowerFtpProperty.LOCAL_DOWNLOAD_DIRECTORY);
        metadataStore.afterPropertiesSet();
        return metadataStore;
    }

    @Bean
    public FileSystemPersistentAcceptOnceFileListFilter fileSystemPersistentAcceptOnceFileListFilter()
            throws Exception {
        ConcurrentMetadataStore metaDataStore;
        FileSystemPersistentAcceptOnceFileListFilter fileSystemPersistentFilter = null;
        metaDataStore = getMetadataStore();
        fileSystemPersistentFilter = new FileSystemPersistentAcceptOnceFileListFilter(
                metaDataStore, "");
        fileSystemPersistentFilter.setFlushOnUpdate(true);
        return fileSystemPersistentFilter;
    }

    @Bean
    public FtpPersistentAcceptOnceFileListFilter ftpPersistentAcceptOnceFileListFilter()
            throws Exception {
        FtpPersistentAcceptOnceFileListFilter ftpPersistentAcceptOnceFileListFilter =
                new FtpPersistentAcceptOnceFileListFilter(getMetadataStore(),
                        WebpowerFtpProperty.FILE_FILTER_RULE);
        ftpPersistentAcceptOnceFileListFilter.setFlushOnUpdate(true);
        return ftpPersistentAcceptOnceFileListFilter;
    }

    @Bean
    public FileListFilter<FTPFile> compositeFilter() throws Exception {
        Pattern pattern = Pattern.compile(WebpowerFtpProperty.FILE_FILTER_RULE);
        CompositeFileListFilter<FTPFile> compositeFileListFilter =
                new CompositeFileListFilter<FTPFile>();
        FileListFilter<FTPFile> fileListFilter = new FtpRegexPatternFileListFilter(
                pattern);
        compositeFileListFilter.addFilter(fileListFilter);
        compositeFileListFilter.addFilter(getAcceptOnceFileFilter());
        return compositeFileListFilter;
    }

    @Bean
    public FileListFilter<FTPFile> getAcceptOnceFileFilter() throws Exception {
        FileListFilter<FTPFile> ftpPersistentAcceptOnceFileListFilter = null;
        ftpPersistentAcceptOnceFileListFilter = new FtpPersistentAcceptOnceFileListFilter(
                getMetadataStore(), WebpowerFtpProperty.FILE_FILTER_RULE);
        return ftpPersistentAcceptOnceFileListFilter;
    }

    @Bean(name = "inBoundHandler")
    @ServiceActivator(inputChannel = "ftpChannel")
    public MessageHandler inBoundHandler() {
        return (message) -> {
            logger.info("监听到消息：" + message.getPayload());
        };
    }

    @Bean
    public RemoteServerChannelGateway ftpChannelGateway(MessageSource messageSource,
                                                        SessionFactory<FTPFile> sessionFactory) {
        return new DefaultFtpChannelGateway(messageSource, ftpMessageHandler(), template(), sessionFactory, env);
    }

    @Bean(name = "ftpMessageHandler")
    public AbstractMessageHandler ftpMessageHandler() {
        FileTransferringMessageHandler<FTPFile> messageHandler =
                new FileTransferringMessageHandler<>(ftpSessionFactory());
        messageHandler.setRemoteDirectoryExpression(new ValueExpression<>(WebpowerFtpProperty.REMOTE_PUT_DIRECTORY));
        messageHandler.setCharset("utf-8");
        messageHandler.setShouldTrack(true);
        messageHandler.setCountsEnabled(true);
        return messageHandler;
    }

    @Bean
    public FtpRemoteFileTemplate template() {
        return new FtpRemoteFileTemplate(ftpSessionFactory());
    }
}
