package cn.tendata.mdcs.web.controller;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import cn.tendata.pay.client.proxy.AccountPaymentResult;
import cn.tendata.pay.client.proxy.PaymentDetail;
import cn.tendata.pay.client.proxy.PaymentGatewayProxy;

public class AccountControllerTest extends SecurityMockMvcTestSupport {

    @Autowired PaymentGatewayProxy paymentGatewayProxy;
    
    @Test
    @DatabaseSetup({"userData.xml", "userTransactionDetailData.xml"})
    public void home_ShouldAddUserAndUserTransactionDetailsToModelAndRenderView() throws Exception {
        mockMvc.perform(get("/user/account/index").with(user(securityUser())))
            .andExpect(status().isOk())
            .andExpect(view().name("account/home"))
            .andExpect(model().size(3))
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attributeExists("loginUser"))
            .andExpect(model().attribute("userTransactionDetails", hasSize(2)));
    }
    
    @Test
    @DatabaseSetup({"userData.xml"})
    public void deposit_ShouldRenderFormView() throws Exception{
        mockMvc.perform(get("/user/account/deposit").with(user(securityUser())))
            .andExpect(status().isOk())
            .andExpect(view().name("account/deposit_form"))
            .andExpect(model().size(2))
            .andExpect(model().attributeExists("user"));
    }
    
    @Test
    public void deposit_ShouldCompleteUserDepositOrderAndReturnValidationErrors() throws Exception{
        mockMvc.perform(post("/user/account/deposit").with(user(securityUser()))
                .param("credits", "0")
                .param("amount", "0.00"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("form", "credits", "amount"));
    }
    
    @Test
    @DatabaseSetup({"userData.xml"})
    @ExpectedDatabase(value = "userDepositOrderDetailStartExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void deposit_ShouldAddUserDepositOrderAndRedirectView() throws Exception{
        AccountPaymentResult paymentResult = new AccountPaymentResult();
        paymentResult.put(AccountPaymentResult.SUCCESS_KEY, true);
        when(paymentGatewayProxy.pay(any(PaymentDetail.class))).thenReturn(paymentResult);
        
        mockMvc.perform(post("/user/account/deposit").with(user(securityUser()))
                .param("credits", "3000")
                .param("amount", "30.00"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/user/account/deposit_completed"))
            .andExpect(flash().attributeCount(1))
            .andExpect(flash().attributeExists("paymentResult"));
        
        ArgumentCaptor<PaymentDetail> paymentDetailArgument = ArgumentCaptor.forClass(PaymentDetail.class);
        verify(paymentGatewayProxy, times(1)).pay(paymentDetailArgument.capture());
        verifyNoMoreInteractions(paymentGatewayProxy);
        
        PaymentDetail paymentDetail = paymentDetailArgument.getValue();
        assertThat(paymentDetail.getTotalFee(), is(new BigDecimal("30.00")));
        assertThat(paymentDetail.getUserId(), is(1L));
    }
    
    @Test
    @DatabaseSetup({"userData.xml", "userDepositOrderDetailData.xml"})
    @ExpectedDatabase(value="userDepositOrderDetailEndExpected.xml", assertionMode=DatabaseAssertionMode.NON_STRICT)
    public void depositCompleted_ShouldRenderView() throws Exception {
        AccountPaymentResult paymentResult = new AccountPaymentResult();
        paymentResult.put(AccountPaymentResult.SUCCESS_KEY, true);
        paymentResult.put(AccountPaymentResult.OUT_TRADE_NO_KEY, "123456");
        mockMvc.perform(get("/user/account/deposit_completed").with(user(securityUser()))
                .flashAttr("paymentResult", paymentResult))
            .andExpect(status().isOk())
            .andExpect(view().name("account/deposit_completed"))
            .andExpect(model().attributeExists("orderDetail"));
    }
    
    @Test
    @DatabaseSetup({"userData.xml"})
    public void childUsers_ShouldAddChildUsersToModelAndRenderListView() throws Exception {
        mockMvc.perform(get("/user/account/child_users").with(user(securityUserWithChildUsers())))
            .andExpect(status().isOk())
            .andExpect(view().name("account/child_users"))
            .andExpect(model().attribute("childUsers", hasSize(2)))
            .andExpect(model().attribute("childUsers", 
                    hasItem(allOf(hasProperty("userId", is(3L)), hasProperty("username", is("user3")))
            )))
            .andExpect(model().attribute("childUsers",
                    hasItem(allOf(hasProperty("userId", is(5L)), hasProperty("username", is("user5")))
            )));
    }
    
    @Test
    @DatabaseSetup({"userData.xml"})
    public void assignBalance_ShouldRenderFormViewAndReturnValidationErrors() throws Exception{
        mockMvc.perform(post("/user/account/child_users/{userId}/assign_balance", 3L).with(user(securityUserWithChildUsers()))
                .param("credits", "-1"))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }
    
    @Test
    @DatabaseSetup({"userData.xml"})
    @ExpectedDatabase(value = "assignBalanceUserNotFoundCompleteExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void assignBalance_UserNotFound_ShouldAddChildUserToModelAndReturnHttpStatusOk() throws Exception{
        mockMvc.perform(post("/user/account/child_users/{userId}/assign_balance", 5L).with(user(securityUserWithChildUsers()))
                .param("credits", "100"))
            .andExpect(status().isOk());
    }
    
    @Test
    @DatabaseSetup({"userData.xml"})
    @ExpectedDatabase(value = "assignBalanceCompleteExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void assignBalance_ShouldAddChildUserToModelAndReturnHttpStatusOk() throws Exception{
        mockMvc.perform(post("/user/account/child_users/{userId}/assign_balance", 3L).with(user(securityUserWithChildUsers()))
                .param("credits", "100"))
            .andExpect(status().isOk());
    }
    
    @Profile("test")
    @Configuration
    static class TestConfig {
        @Bean
        public PaymentGatewayProxy paymentGatewayProxy(){
            return mock(PaymentGatewayProxy.class);
        }
    }
}
