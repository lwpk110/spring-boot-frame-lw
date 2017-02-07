package cn.tendata.mdcs.webapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.tendata.pay.client.DefaultNotifyDataHandler;
import cn.tendata.pay.client.DefaultPaymentClient;
import cn.tendata.pay.client.DefaultPaymentConfig;
import cn.tendata.pay.client.DefaultReturnDataHandler;
import cn.tendata.pay.client.PaymentClient;
import cn.tendata.pay.client.proxy.DefaultPaymentGatewayProxy;
import cn.tendata.pay.client.proxy.PaymentGatewayProxy;

@Configuration
public class PaymentConfig {

    @ConfigurationProperties(prefix = "payment")
    @Bean
    public DefaultPaymentConfig defaultPaymentConfig() {
        return new DefaultPaymentConfig();
    }
    
    @Bean
    public PaymentGatewayProxy paymentGatewayProxy() {
        return new DefaultPaymentGatewayProxy(defaultPaymentConfig());
    }
    
    @Bean
    public PaymentClient paymentClient() {
        return new DefaultPaymentClient(defaultPaymentConfig(), new DefaultReturnDataHandler(),
                new DefaultNotifyDataHandler());
    }
}
