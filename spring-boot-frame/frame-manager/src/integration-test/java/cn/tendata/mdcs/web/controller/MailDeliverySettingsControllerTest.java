package cn.tendata.mdcs.web.controller;

import cn.tendata.mdcs.data.domain.MailDeliverySettings;
import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailDeliverySettings;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MailDeliverySettingsControllerTest extends SecurityMockMvcTestSupport {
    @Test
    @DatabaseSetup({"userData.xml", "userMailDeliverySettingsData.xml"})
    public void list_ShouldAddUserMailDeliverySettingsToModelAndRenderListView() throws Exception {
        mockMvc.perform(get("/user/delivery_settings").with(user(securityUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("delivery_settings/list"))
                .andExpect(model().attribute("deliverySettings", hasSize(3)))
                .andExpect(model().attribute("deliverySettings",
                        hasItem(allOf(hasProperty("deliverySettings", hasProperty("senderName", is("test_johnny"))),
                                hasProperty("deliverySettings", hasProperty("senderEmail", is("test_11@qq.com")))))))
                .andDo(print());
    }

    @Test
    @DatabaseSetup({"userData.xml", "emptyUserMailDeliverySettingsData.xml"})
    @ExpectedDatabase(value = "saveUserMailDeliverySettingsExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void create_UserMailDeliverySettingsAndReturnJsonResponse() throws Exception {
        mockMvc.perform(post("/user/delivery_settings/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(user(securityUser()))
                .param("deliverySettings.senderName", "test_johnny")
                .param("deliverySettings.senderEmail", "test_11@qq.com")
                .param("deliverySettings.replyName", "admin")
                .param("deliverySettings.replyEmail", "test_22@qq.com")
                .param("checked", "1")
                .sessionAttr("newDeliverySettings", new UserMailDeliverySettings()))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"status\":200,\"msg\":null,\"content\":null}"))
                .andDo(print());
    }

    @Test
    @DatabaseSetup({"userData.xml", "saveUserMailDeliverySettingsExpected.xml"})
    @ExpectedDatabase(value = "updateUserMailDeliverySettingsExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void update_UserMailDeliverySettingsAndReturnJsonResponse() throws Exception {
        UserMailDeliverySettings userMailDeliverySettings = new UserMailDeliverySettings();
        MailDeliverySettings mailDeliverySettings = new MailDeliverySettings();
        mailDeliverySettings.setSenderName("test_johnny");
        mailDeliverySettings.setSenderEmail("test_11@qq.com");
        mailDeliverySettings.setReplyName("admin");
        mailDeliverySettings.setReplyEmail("test_22@qq.com");
        userMailDeliverySettings.setId(1L);
        userMailDeliverySettings.setChecked(false);
        userMailDeliverySettings.setDeliverySettings(mailDeliverySettings);
        User user = new User(1, "aaa");
        userMailDeliverySettings.setUser(user);
        mockMvc.perform(post("/user/delivery_settings/{id}/edit", 1L)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("deliverySettings.senderName", "test_johnny2")
                .sessionAttr("userMailDeliverySettings", userMailDeliverySettings))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"status\":200,\"msg\":null,\"content\":null}"))
                .andDo(print());
    }

    @Test
    @DatabaseSetup({"userData.xml", "userMailDeliverySettingsData.xml"})
    @ExpectedDatabase(value = "removeUserMailDeliverySettingsExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void delete_UserMailDeliverySettingsAndReturnJsonResponse() throws Exception {
        mockMvc.perform(post("/user/delivery_settings/{id}/delete", 1L)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(user(securityUser())))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"status\":200,\"msg\":null,\"content\":null}"))
                .andDo(print());
    }
}
