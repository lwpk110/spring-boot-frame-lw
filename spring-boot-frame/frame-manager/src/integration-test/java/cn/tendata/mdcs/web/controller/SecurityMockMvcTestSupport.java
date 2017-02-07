package cn.tendata.mdcs.web.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import cn.tendata.cas.client.security.core.userdetails.LoginUser;
import cn.tendata.cas.client.security.core.userdetails.LoginUser.ChildUser;
import cn.tendata.cas.client.security.core.userdetails.LoginUser.LoginUserBuilder;

@ContextConfiguration
public abstract class SecurityMockMvcTestSupport extends MockMvcTestSupport {

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    protected LoginUser securityUser() {
        return loginUserBuilder().build();
    }
    
    protected LoginUser securityUserWithChildUsers() {
        List<ChildUser> childUsers = new ArrayList<>(2);
        childUsers.add(new ChildUser(3L, "user3"));
        childUsers.add(new ChildUser(5L, "user5"));
        return loginUserBuilder().childUsers(childUsers).build();
    }
    
    protected LoginUserBuilder loginUserBuilder() {
        return new LoginUserBuilder()
                .username("user1")
                .password("NO_PASSWORD")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .authorities(AuthorityUtils.NO_AUTHORITIES)
                .userId(1L);
    }

    @Profile("test")
    @Configuration
    @EnableWebSecurity
    static class SecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
        }
    }
}
