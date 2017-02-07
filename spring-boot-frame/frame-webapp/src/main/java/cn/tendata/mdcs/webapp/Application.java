package cn.tendata.mdcs.webapp;

import cn.tendata.ftp.webpower.config.batch.WebpowerMailReportBatchConfig;
import cn.tendata.ftp.webpower.config.sftp.SftpCommonConfig;
import cn.tendata.mdcs.core.io.export.config.FileExportConfig;
import cn.tendata.mdcs.data.elasticsearch.core.CustomEntityMapper;
import cn.tendata.mdcs.mail.config.MailDeliveryAspectConfig;
import cn.tendata.mdcs.mail.config.MailDeliveryGatewayConfig;
import cn.tendata.mdcs.mail.support.DomainSuffixResolver;
import cn.tendata.mdcs.mail.support.MailAgentDeliveryManager;
import cn.tendata.mdcs.service.EntityService;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching(order = 0)
@EnableConfigurationProperties
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Configuration
    @EnableTransactionManagement
    @EnableJpaRepositories(basePackages = "cn.tendata.mdcs.data.repository")
    @EnableJpaAuditing
    @EntityScan(basePackages = "cn.tendata.mdcs.data.domain")
    static class JpaConfig {
    }

    @Configuration
    @EnableElasticsearchRepositories(basePackages = "cn.tendata.mdcs.data.elasticsearch.repository")
    static class ElasticsearchConfig {

        @Bean
        public ElasticsearchTemplate elasticsearchTemplate(Client client,
            ElasticsearchConverter converter) {
            try {
                return new ElasticsearchTemplate(
                    client, converter, new DefaultResultMapper(converter.getMappingContext(),
                    new CustomEntityMapper()));
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
    }

    @Import({MailDeliveryGatewayConfig.class,
        MailDeliveryAspectConfig.class,
        SftpCommonConfig.class,
        WebpowerMailReportBatchConfig.class})
    static class MailDeliveryConfig {

    }

    @Configuration
    @ComponentScan(basePackageClasses = {EntityService.class})
    @Import(FileExportConfig.class)
    static class ServiceConfig {
        @Bean
        public MailAgentDeliveryManager mailAgentDeliveryManager(
            @Value("classpath:domain_suffix.properties") Resource resource) {
            return new MailAgentDeliveryManager(new DomainSuffixResolver(resource));
        }
    }

    @Configuration
    static class SchedualConfig {
    }
}
