package cn.tendata.mdcs.webapp.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import cn.tendata.cas.client.security.core.userdetails.LoginUser.ChildUser;
import cn.tendata.cas.client.security.core.userdetails.LoginUser.LoginUserBuilder;
import cn.tendata.mdcs.admin.web.util.SecurityAccess;
import cn.tendata.mdcs.webapp.context.UserAuthenticationSuccessListener;

@Profile("dev")
@Configuration
@Order(2)
public class DevSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserAuthenticationSuccessListener userAuthenticationSuccessListener(){
        return new UserAuthenticationSuccessListener();
    }

    @Bean
    public DevAuthenticationConfigurerAdapter devAuthenticationConfigurerAdapter(){
        return new DevAuthenticationConfigurerAdapter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/admin/**").hasAuthority(SecurityAccess.PERMISSION_ADMIN_VIEW)
                .anyRequest().permitAll()
                .and()
            .formLogin()
                .permitAll()
                .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and()
            .headers()
                .frameOptions()
                .sameOrigin();
    }

    private static class DevAuthenticationConfigurerAdapter extends GlobalAuthenticationConfigurerAdapter {

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(username -> {
                Collection<ChildUser> childUsers = new ArrayList<>();
                childUsers.add(new ChildUser(3L, "child1"));
                childUsers.add(new ChildUser(5L, "child2"));
                childUsers.add(new ChildUser(20463L, "lvyun@biogot.com"));
                return new LoginUserBuilder()
                        .username("xiangchi1")
                        .password("admin")
                        .enabled(true)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .authorities(AuthorityUtils.commaSeparatedStringToAuthorityList(
                                "MDCS:ADMIN:VIEW,MDCS:ADMIN:USER:VIEW,MDCS:ADMIN:USER:DEPOSIT," +
                                        "MDCS:ADMIN:CHANNEL:VIEW,MDCS:ADMIN:CHANNEL:MANAGE,MDCS:ADMIN:TASK:VIEW,MDCS:ADMIN:REPORT:VIEW" +
                                        ",MDCS:ADMIN:MAIL:TEMPLATE:APPROVE,MDCS:ADMIN:TOOL:REPORT:VIEW,MDCS:ADMIN:TOOL:REPORT:MANAGE"))
                        .userId(22707L)
                        .childUsers(childUsers)
                        .build();
            });
        }
    }
}
