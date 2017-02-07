package cn.tendata.mdcs.web.config;

import cn.tendata.mdcs.web.bind.support.ChildUserHandlerMethodArgumentResolver;
import cn.tendata.mdcs.web.bind.support.CurrentUserHandlerMethodArgumentResolver;
import cn.tendata.mdcs.web.mail.parse.MailRecipientParserImpl;
import cn.tendata.mdcs.web.mail.parse.MailRecipientConfig;
import cn.tendata.mdcs.web.mail.parse.MailRecipientParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.List;

@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
@ComponentScan(basePackages = "cn.tendata.mdcs.web")
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public MailRecipientParser mailRecipientParser() {
		return new MailRecipientParserImpl(new MailRecipientConfig());
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
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
}
