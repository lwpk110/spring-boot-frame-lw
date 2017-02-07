package cn.tendata.mdcs.admin.web.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import cn.tendata.mdcs.admin.web.model.MailDeliveryChannelNodeDto;

public class MailDeliveryChannelNodeControllerTest extends SecurityMockMvcTestSupport {

    @Test
    @DatabaseSetup({"userData.xml", "saveMailDeliveryChannelsExpected.xml", "saveMailDeliveryChannelNodesExpected.xml"})
    public void testList() throws Exception {

        mockMvc.perform(get("/admin/mail-delivery-channels/{channelId}/nodes", 1)
                .with(user(securityUser())))
                .andExpect(status().isOk());

    }

    @Test
    @DatabaseSetup({"userData.xml", "emptyMailDeliveryChannelNodesData.xml", "saveMailDeliveryChannelsExpected.xml"})
    @ExpectedDatabase(value = "saveMailDeliveryChannelNodesExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void testCreate() throws Exception {

        MailDeliveryChannelNodeDto channelNodeDto = new MailDeliveryChannelNodeDto();
        channelNodeDto.setServerKey("1");
        channelNodeDto.setName("testnode1");
        channelNodeDto.setConfigProps("{\"a\":2}");
        channelNodeDto.setNeedCampaigns(false);

        mockMvc.perform(post("/admin/mail-delivery-channels/{channelId}/nodes", 1)
                .with(user(securityUser()))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonString(channelNodeDto)))
                .andExpect(status().isCreated());

    }

    @Test
    @DatabaseSetup({"userData.xml", "saveMailDeliveryChannelsExpected.xml", "saveMailDeliveryChannelNodesExpected.xml"})
    @ExpectedDatabase(value = "updateMailDeliveryChannelNodesExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void testUpdate() throws Exception {

        MailDeliveryChannelNodeDto channelNodeDto = new MailDeliveryChannelNodeDto();
        channelNodeDto.setName("test update nodeName");
        channelNodeDto.setServerKey("1");
        channelNodeDto.setConfigProps("{\"a\":2}");
        channelNodeDto.setNeedCampaigns(false);

        mockMvc.perform(put("/admin/mail-delivery-channels/{channelId:[1-9]\\d*}/nodes/{id}", 1, 1)
                .with(user(securityUser()))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonString(channelNodeDto)))
                .andExpect(status().isOk());

    }

    @Test
    @DatabaseSetup({"userData.xml", "saveMailDeliveryChannelsExpected.xml", "saveMailDeliveryChannelNodesExpected.xml"})
    @ExpectedDatabase(value = "changeMailDeliveryChannelNodeStatusExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void testChangeDisabled() throws Exception {

        mockMvc.perform(put("/admin/mail-delivery-channels/{channelId:[1-9]\\d*}/nodes/{id}/change-disabled", 1, 1)
                .with(user(securityUser()))
                .param("disabled", "0"))
                .andExpect(status().isOk());

    }

    @Test
    @DatabaseSetup({"userData.xml", "saveMailDeliveryChannelsExpected.xml", "saveMailDeliveryChannelNodesExpected.xml"})
    @ExpectedDatabase(value = "saveMailDeliveryChannelNodesExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void testGetById() throws Exception {

        mockMvc.perform(get("/admin/mail-delivery-channels/{channelId:[1-9]\\d*}/nodes/{id}", 1, 1)
                .with(user(securityUser())))
                .andExpect(status().isOk());

    }

}
