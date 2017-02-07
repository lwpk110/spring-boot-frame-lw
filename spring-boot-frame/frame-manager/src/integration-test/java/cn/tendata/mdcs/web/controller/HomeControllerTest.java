package cn.tendata.mdcs.web.controller;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class HomeControllerTest extends SecurityMockMvcTestSupport {
    @Test
    @DatabaseSetup({"userData.xml", "userMailDeliveryChannelData.xml", "userMailDeliverySettingsData.xml",
            "userMailTemplateData.xml", "userMailRecipientGroupData.xml"})
    public void homeShouldRenderHomeView() throws Exception {
        mockMvc.perform(get("/user/index").with(user(securityUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }
}
