package cn.tendata.ftp.webpower.config.sftp;

import cn.tendata.ftp.webpower.model.WebpowerBatchProperties;
import cn.tendata.ftp.webpower.util.WebpowerSftpProperties;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.FileListFilter;
import org.springframework.integration.file.filters.FileSystemPersistentAcceptOnceFileListFilter;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.metadata.ConcurrentMetadataStore;
import org.springframework.integration.metadata.PropertiesPersistingMetadataStore;
import org.springframework.integration.sftp.filters.SftpPersistentAcceptOnceFileListFilter;
import org.springframework.integration.sftp.filters.SftpRegexPatternFileListFilter;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;

import java.util.regex.Pattern;

/**
 * Created by ernest on 2016/12/19.
 */
@Configuration
public class SftpCommonConfig {
    @Autowired
    private WebpowerBatchProperties webpowerBatchProperties;

    @Bean
    public SessionFactory<LsEntry> sftpSessionFactory() {
        DefaultSftpSessionFactory sftpSessionFactory = new DefaultSftpSessionFactory();
        sftpSessionFactory.setHost(webpowerBatchProperties.getSftpHost());
        sftpSessionFactory.setUser(webpowerBatchProperties.getSftpLoginUser());
        sftpSessionFactory.setPassword(webpowerBatchProperties.getSftpLoginPassword());
        sftpSessionFactory.setPort(webpowerBatchProperties.getSftpPort());
        sftpSessionFactory.setTimeout(15000);
        sftpSessionFactory.setAllowUnknownKeys(true);
        return new CachingSessionFactory<>(sftpSessionFactory);
    }

    @Bean
    public PropertiesPersistingMetadataStore getMetadataStore()
            throws Exception {
        PropertiesPersistingMetadataStore metadataStore = new PropertiesPersistingMetadataStore();
        metadataStore.setBaseDirectory(webpowerBatchProperties.getSftpLocalDirSynchro());
        metadataStore.afterPropertiesSet();
        return metadataStore;
    }

    @Bean
    public FileSystemPersistentAcceptOnceFileListFilter fileSystemPersistentAcceptOnceFileListFilter()
            throws Exception {
        ConcurrentMetadataStore metaDataStore;
        FileSystemPersistentAcceptOnceFileListFilter fileSystemPersistentFilter;
        metaDataStore = getMetadataStore();
        fileSystemPersistentFilter = new FileSystemPersistentAcceptOnceFileListFilter(
                metaDataStore, "");
        // fileSystemPersistentFilter.setFlushOnUpdate(true);
        return fileSystemPersistentFilter;
    }

    @Bean
    public FileListFilter<LsEntry> compositeFilter() throws Exception {
        Pattern pattern = Pattern.compile(WebpowerBatchProperties.FILE_FILTER_RULE);
        CompositeFileListFilter<LsEntry> compositeFileListFilter = new CompositeFileListFilter<>();
        SftpRegexPatternFileListFilter fileListFilter = new SftpRegexPatternFileListFilter(pattern);
        compositeFileListFilter.addFilter(fileListFilter);
        compositeFileListFilter.addFilter(ftpPersistentAcceptOnceFileListFilter());
        return compositeFileListFilter;
    }

    @Bean
    public SftpPersistentAcceptOnceFileListFilter ftpPersistentAcceptOnceFileListFilter()
            throws Exception {
        SftpPersistentAcceptOnceFileListFilter sftpPersistentAcceptOnceFileListFilter =
                new SftpPersistentAcceptOnceFileListFilter(getMetadataStore(),
                        WebpowerBatchProperties.FILE_FILTER_RULE);
        sftpPersistentAcceptOnceFileListFilter.setFlushOnUpdate(true);
        return sftpPersistentAcceptOnceFileListFilter;
    }

}
