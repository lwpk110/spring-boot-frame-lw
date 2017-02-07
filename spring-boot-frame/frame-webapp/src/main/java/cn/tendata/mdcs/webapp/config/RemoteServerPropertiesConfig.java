package cn.tendata.mdcs.webapp.config;

import cn.tendata.ftp.webpower.model.WebpowerBatchProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ernest on 2016/12/20.
 */

@Configuration
public class RemoteServerPropertiesConfig {

    @ConfigurationProperties(prefix = "batch")
    @Bean
    public WebpowerBatchProperties webpowerBatchProperties() {
        return new WebpowerBatchProperties();
    }

}
