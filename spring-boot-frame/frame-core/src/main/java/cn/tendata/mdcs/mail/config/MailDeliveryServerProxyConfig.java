package cn.tendata.mdcs.mail.config;

import cn.tendata.mdcs.mail.core.ExampleMailDeliveryServerProxy;
import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.tendata.mdcs.mail.core.MailDeliveryServerProxy;
import cn.tendata.mdcs.mail.core.MailDeliveryServerProxyFactory;
import cn.tendata.mdcs.mail.core.MailDeliveryServerProxyManager;

@Configuration
public class MailDeliveryServerProxyConfig {

    @Bean
    public MailDeliveryServerProxyManager mailDeliveryServerProxyManager(MailDeliveryServerProxyFactory serverProxyFactory){
        return new MailDeliveryServerProxyManager(serverProxyFactory);
    }
    @Bean
    public ExampleMailDeliveryServerProxy exampleMailDeliveryServerProxy(){
        return new ExampleMailDeliveryServerProxy();
    }
    @Bean
    public MailDeliveryServerProxyFactory mailDeliveryServerProxyFactory(Collection<MailDeliveryServerProxy> servers){
        return new MailDeliveryServerProxyFactory(servers);
    }
}
