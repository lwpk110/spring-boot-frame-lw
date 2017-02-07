package cn.tendata.mdcs.web.controller;

import cn.tendata.mdcs.web.model.TaskQueryParameter;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MailDeliveryTaskControllerTest extends SecurityMockMvcTestSupport {
    @Test
    @DatabaseSetup("userData.xml")
    public void listShouldRenderListView() throws Exception {
        mockMvc.perform(get("/user/delivery_task").with(user(securityUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("delivery_task/list"));
    }

    @Test
    @DatabaseSetup({"userData.xml", "userMailDeliveryChannelData.xml", "userMailDeliverySettingsData.xml",
            "userMailTemplateData.xml", "userMailRecipientGroupData.xml"})
    public void listShouldRetuenMailDeliveryTaskList() throws Exception {
        TaskQueryParameter taskQueryParameter = new TaskQueryParameter();
        taskQueryParameter.setStartDate(new DateTime(2015, 1, 1, 0, 0));
        taskQueryParameter.setStartDate(new DateTime(2016, 1, 1, 0, 0));
        mockMvc.perform(get("/user/delivery_task/list").with(user(securityUser()))
                .param("iDisplayLength", "10")
                .param("iColumns", "4")
                .param("sEcho", "1")
                .param("iColumns", ",,,")
                .sessionAttr("taskQueryParameter", taskQueryParameter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aaData").exists())
                .andExpect(jsonPath("$.iTotalDisplayRecords").exists())
                .andExpect(jsonPath("$.iTotalRecords").exists())
                .andExpect(jsonPath("$.sEcho").exists());
    }

    @Test
    @DatabaseSetup({"userData.xml", "userMailDeliveryChannelData.xml", "userMailDeliverySettingsData.xml",
            "userMailTemplateData.xml", "userMailRecipientGroupData.xml"})
    public void createFormShouldAndEntityToModelAndRenderFormView() throws Exception {
        mockMvc.perform(get("/user/delivery_task/add").with(user(securityUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("delivery_task/create_form"))
                .andExpect(model().attributeExists("defaultDelivery"))
                .andExpect(model().attributeExists("deliverySettings"))
                .andExpect(model().attributeExists("deliveryChannels"))
                .andExpect(model().attributeExists("recipientGroups"))
                .andExpect(model().attributeExists("templates"));
    }

    @Test
    @DatabaseSetup({"userData.xml", "userMailDeliveryChannelData.xml", "userMailDeliverySettingsData.xml",
            "userMailTemplateData.xml", "userMailRecipientGroupData.xml"})
    @ExpectedDatabase(value = "saveUserMailDeliveryTaskExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void submitTaskShouldAndTaskAndReturnJsonResponse() throws Exception {
        mockMvc.perform(post("/user/delivery_task/submit_task").with(user(securityUser()))
                .param("name", "aaa")
                .param("userMailDeliverySettings", "1")
                .param("deliveryChannel", "1")
                .param("userMailRecipientGroups", "4b2f02ca-d857-4c95-b7fa-ee6283ce0d64")
                .param("userMailRecipientGroups", "4b2f02ca-d857-4c95-b7fa-ee6283ce0d65")
                .param("userMailTemplate", "1")
                .param("scheduled", "true")
                .param("mailTemplate.body", "body")
                .param("mailTemplate.subject", "subject")
                .param("mailTemplate.html", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.content").doesNotExist())
                .andExpect(jsonPath("$.msg").doesNotExist());
    }

    @Test
    @DatabaseSetup({"userData.xml", "userMailDeliveryChannelData.xml", "userMailDeliverySettingsData.xml",
            "userMailTemplateData.xml", "userMailRecipientGroupData.xml","userMailDeliveryTaskData.xml"})
    public void deliveryRollbackShouldReturnJsonResponse() throws Exception {
        mockMvc.perform(post("/user/delivery_task/{id}/delivery_rollback", "4b2f02ca-d857-4c95-b7fa-ee6283ce0d64").with(user(securityUser()))
                .param("name", "name")).andExpect(status().isOk())
                		.andExpect(jsonPath("$.status").value(200))
                        .andExpect(jsonPath("$.content").doesNotExist())
                        .andExpect(jsonPath("$.msg").doesNotExist());
    }
}
