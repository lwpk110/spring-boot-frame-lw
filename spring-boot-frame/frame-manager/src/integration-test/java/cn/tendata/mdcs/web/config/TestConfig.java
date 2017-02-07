package cn.tendata.mdcs.web.config;

import cn.tendata.mdcs.core.io.export.FileExportManager;
import cn.tendata.mdcs.data.elasticsearch.repository.MailRecipientActionRepository;
import cn.tendata.mdcs.data.jpa.config.DataConfig;
import cn.tendata.mdcs.mail.core.MailDeliveryGateway;
import cn.tendata.mdcs.mail.support.MailAgentDeliveryManager;
import cn.tendata.mdcs.service.EntityService;
import org.springframework.web.client.RestTemplate;

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
    public MailRecipientActionRepository mailRecipientActionRepository(){
        return mock(MailRecipientActionRepository.class);
    }
    
    @Bean
    public FileExportManager fileExportManager(){
        return mock(FileExportManager.class);
    }
    
    @Bean
    public MailAgentDeliveryManager mailAgentDeliveryManager(){
        return mock(MailAgentDeliveryManager.class);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
