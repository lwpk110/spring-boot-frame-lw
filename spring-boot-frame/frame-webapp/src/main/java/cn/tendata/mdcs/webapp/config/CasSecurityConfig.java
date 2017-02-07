package cn.tendata.mdcs.webapp.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import cn.tendata.cas.client.configuration.CasClientConfigurerAdapter;
import cn.tendata.cas.client.configuration.CasClientProperties;
import cn.tendata.cas.client.configuration.CasClientSecurityConfigurer;
import cn.tendata.cas.client.configuration.EnableCasClient;
import cn.tendata.cas.client.security.web.authentication.logout.CasLogoutSuccessHandler;
import cn.tendata.mdcs.admin.web.util.SecurityAccess;
import cn.tendata.mdcs.webapp.context.UserAuthenticationSuccessListener;

@Profile("cas")
@Configuration
@EnableCasClient
@Order(2)
public class CasSecurityConfig {

    @Bean
    public static ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }

    @ConfigurationProperties("cas")
    @Bean
    public CasClientProperties casClientProperties(){
        return new CasClientProperties();
    }

    @Bean
    public UserAuthenticationSuccessListener userAuthenticationSuccessListener(){
        return new UserAuthenticationSuccessListener();
    }

    @Profile("cas")
    @Configuration
    public static class CasClientConfig extends CasClientConfigurerAdapter {

        @Autowired
        private Environment env;

        @Autowired
        private CasLogoutSuccessHandler casLogoutSuccessHandler;

        // Work around https://jira.spring.io/browse/SEC-2855
        @Bean
        public SessionRegistry sessionRegistry() {
            SessionRegistry sessionRegistry = new SessionRegistryImpl();
            return sessionRegistry;
        }

        @Bean
        public ConcurrentSessionFilter concurrentSessionFilter() {
            ConcurrentSessionFilter concurrentSessionFilter = new ConcurrentSessionFilter(sessionRegistry(),
                    env.getRequiredProperty(WebMvcConfig.LINK_ACCOUNTS_ROOT) + "/expired");
            CustomRedirectStrategy customRedirectStrategy = new CustomRedirectStrategy();
            concurrentSessionFilter.setRedirectStrategy(customRedirectStrategy);
            return concurrentSessionFilter;
        }

        @Bean
        public CompositeSessionAuthenticationStrategy sessionAuthenticationStrategy() {
            List<SessionAuthenticationStrategy> list = new ArrayList<SessionAuthenticationStrategy>();
            ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy =
                    new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
            concurrentSessionControlAuthenticationStrategy.setMaximumSessions(1);
            list.add(concurrentSessionControlAuthenticationStrategy);
            list.add(new RegisterSessionAuthenticationStrategy(sessionRegistry()));
            CompositeSessionAuthenticationStrategy compositeSessionAuthenticationStrategy = new CompositeSessionAuthenticationStrategy(list);
            return compositeSessionAuthenticationStrategy;
        }

        @Override
        public void configure(CasClientSecurityConfigurer casClient) throws Exception {
            casClient.sessionAuthenticationStrategy(sessionAuthenticationStrategy());
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                .addFilter(concurrentSessionFilter())
                .authorizeRequests()
                    .antMatchers("/user/**").authenticated()
                    .antMatchers("/admin/**").hasAuthority(SecurityAccess.PERMISSION_ADMIN_VIEW)
                    .anyRequest().permitAll()
                    .and()
                .headers()
                    .frameOptions()
                    .sameOrigin()
                    .and()
                .csrf()
                    .ignoringAntMatchers("/login/**", "/logout")
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessHandler(casLogoutSuccessHandler);
        }
    }
}
