package cn.tendata.mdcs.web.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cn.tendata.mdcs.core.DefaultMailDeliveryBounceReportManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import cn.tendata.pay.client.PaymentClient;
import cn.tendata.pay.client.PaymentClientHandleResult;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

public class PaymentControllerTest extends MockMvcTestSupport {

    @Autowired PaymentClient paymentClient;

    @Test
    @DatabaseSetup({"userData.xml", "userDepositOrderDetailData.xml"})
    @ExpectedDatabase(value="userDepositOrderDetailEndExpected.xml", assertionMode=DatabaseAssertionMode.NON_STRICT)
    public void notify_ShouldCompleteDepositOrderAndReturnTextContent() throws Exception{
        PaymentClientHandleResult handleResult = new PaymentClientHandleResult();
        handleResult.setSuccess(true);
        handleResult.setOutTradeNo("123456");
        when(paymentClient.doNotify(anyMapOf(String.class, String.class))).thenReturn(handleResult);

        mockMvc.perform(get("/pay/notify")
                .param("out_trade_no", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().string(is("success")));
    }

    @Profile("test")
    @Configuration
    static class TestConfig {

        @Bean
        public PaymentClient paymentClient(){
            return mock(PaymentClient.class);
		}

        @Bean
        public DefaultMailDeliveryBounceReportManager defaultMailDeliveryBounceReportManager(){
            return mock(DefaultMailDeliveryBounceReportManager.class);
        }

	}
}
