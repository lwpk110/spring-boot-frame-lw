package cn.tendata.mdcs.webapp.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import cn.tendata.mdcs.web.bind.support.ChildUserHandlerMethodArgumentResolver;
import cn.tendata.mdcs.web.bind.support.CurrentUserHandlerMethodArgumentResolver;
import cn.tendata.mdcs.web.mail.parse.MailRecipientParserImpl;
import cn.tendata.mdcs.web.mail.parse.MailRecipientConfig;
import cn.tendata.mdcs.web.mail.parse.MailRecipientParser;
import cn.tendata.mdcs.web.util.StringToDatetimeConverter;

@Configuration
@ComponentScan(basePackages = "cn.tendata.mdcs.**.web.controller")
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    public static final String LINK_ACCOUNTS_ROOT = "link.accounts.root";
    public static final String LINK_WMT_ROOT = "link.wmt.root";
    public static final String LINK_CONTACTX_ROOT = "link.contactx.root";
    public static final String LINK_PAY_ROOT = "link.pay.root";
    public static final String LINK_WENDA_ROOT = "link.wenda.root";
    public static final String LINK_ANALYSIS_ROOT = "link.analysis.root";

    @Autowired
    private Environment env;

    @Autowired
    public void setThymeleafViewResolver(ThymeleafViewResolver thymeleafViewResolver) {
        Map<String, String> vars = new HashMap<String, String>();
        vars.put("LINK_ACCOUNTS_ROOT", env.getRequiredProperty(LINK_ACCOUNTS_ROOT));
        vars.put("LINK_WMT_ROOT", env.getRequiredProperty(LINK_WMT_ROOT));
        vars.put("LINK_CONTACTX_ROOT", env.getRequiredProperty(LINK_CONTACTX_ROOT));
        vars.put("LINK_PAY_ROOT", env.getRequiredProperty(LINK_PAY_ROOT));
        vars.put("LINK_WENDA_ROOT", env.getRequiredProperty(LINK_WENDA_ROOT));
        vars.put("LINK_ANALYSIS_ROOT", env.getRequiredProperty(LINK_ANALYSIS_ROOT));
        thymeleafViewResolver.setStaticVariables(vars);
    }

    @Bean
    public CurrentUserHandlerMethodArgumentResolver currentUserHandlerMethodArgumentResolver() {
        return new CurrentUserHandlerMethodArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserHandlerMethodArgumentResolver());
        argumentResolvers.add(new ChildUserHandlerMethodArgumentResolver());
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public MailRecipientParser mailRecipientParser() {
        return new MailRecipientParserImpl(new MailRecipientConfig());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToDatetimeConverter());
    }
}
