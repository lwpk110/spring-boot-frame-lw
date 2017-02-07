package cn.tendata.mdcs.admin.web.config;

import cn.tendata.ftp.webpower.core.BatchTaskScheduler;
import cn.tendata.ftp.webpower.model.WebpowerBatchProperties;
import cn.tendata.mdcs.core.DefaultMailDeliveryBounceReportManager;
import cn.tendata.mdcs.core.io.export.FileExportManager;
import cn.tendata.mdcs.data.elasticsearch.repository.MailRecipientActionRepository;
import cn.tendata.mdcs.data.jpa.config.DataConfig;
import cn.tendata.mdcs.mail.core.MailDeliveryGateway;
import cn.tendata.mdcs.mail.support.MailAgentDeliveryManager;
import cn.tendata.mdcs.service.EntityService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.*;

import static org.mockito.Mockito.mock;

@Profile("test")
@Configuration
@ComponentScan(basePackageClasses = {EntityService.class})
@Import({DataConfig.class, WebMvcConfig.class})
public class TestConfig {

    @Bean
    public MailDeliveryGateway mailDeliveryGateway() {
        return mock(MailDeliveryGateway.class);
    }

    @Bean
    public MailRecipientActionRepository mailRecipientActionRepository() {
        return mock(MailRecipientActionRepository.class);
    }

    @Bean
    public FileExportManager fileExportManager() {
        return mock(FileExportManager.class);
    }

    @Bean
    public MailAgentDeliveryManager mailAgentDeliveryManager() {
        return mock(MailAgentDeliveryManager.class);
    }

    @Bean
    public DefaultMailDeliveryBounceReportManager defaultMailDeliveryBounceReportManager() {
        return mock(DefaultMailDeliveryBounceReportManager.class);
    }
    @Bean
    public WebpowerBatchProperties webpowerBatchProperties() {
        return mock(WebpowerBatchProperties.class);
    }
    @Bean
    public BatchTaskScheduler batchTaskScheduler() {
        return mock(BatchTaskScheduler.class);
    }

    @Bean
    public JobLauncher jobLauncher() {
        return mock(JobLauncher.class);
    }

    @Bean
    public Job job() {
        return mock(Job.class);
    }

}
