package cn.tendata.mdcs.admin.web.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import cn.tendata.mdcs.admin.web.model.FieldErrorDto;
import cn.tendata.mdcs.admin.web.model.MailDeliveryChannelDto;
import cn.tendata.mdcs.admin.web.model.ValidationErrorDto;

public class MailDeliveryChannelControllerTest extends SecurityMockMvcTestSupport {

	@Test
	@DatabaseSetup("userData.xml")
	public void testList () throws Exception {
		
		mockMvc.perform(get("/admin/mail-delivery-channels")
				.with(user(securityUser()))
				.param("name", ""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").exists());  
		
	}
	
	@Test
    @DatabaseSetup({"userData.xml", "saveMailDeliveryChannelsExpected.xml"})
    @ExpectedDatabase(value = "changeMailDeliveryChannelStatusExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void testChangeDisabled() throws Exception {
		
		mockMvc.perform(put("/admin/mail-delivery-channels/{id}/change-disabled", 1)
				.with(user(securityUser()))
				.param("disabled", "0"))
        		.andExpect(status().isOk()); 
		
	}
	
	@Test
    @DatabaseSetup({"userData.xml", "emptyMailDeliveryChannelsData.xml"})
    @ExpectedDatabase(value = "saveMailDeliveryChannelsExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void testCreate () throws Exception {
		
		MailDeliveryChannelDto channelDto = new MailDeliveryChannelDto ("test1", "test delivery channel", 3, 233);
		mockMvc.perform(post("/admin/mail-delivery-channels")
				.with(user(securityUser()))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtils.convertObjectToJsonString(channelDto))) 
		        .andExpect(status().isCreated());
		
	}
	
	@Test
    @DatabaseSetup({"userData.xml", "saveMailDeliveryChannelsExpected.xml"})
    @ExpectedDatabase(value = "updateMailDeliveryChannelsExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void testUpdate () throws Exception {
		
		MailDeliveryChannelDto channelDto = new MailDeliveryChannelDto("aaaa", "test delivery channel", 3, 233);
		channelDto.setName("aaaa");
		mockMvc.perform(put("/admin/mail-delivery-channels/{id}", 1)
				.with(user(securityUser()))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtils.convertObjectToJsonString(channelDto))) 
        		.andExpect(status().isOk());
		
	}
	
	@Test
    @DatabaseSetup({"userData.xml"})
	public void testUpdateNameWhenMoreThan100Characters () throws Exception {
		
		MailDeliveryChannelDto channelDto = new MailDeliveryChannelDto(null, "test delivery channel", 3, 233);
		channelDto.setName(null);
		
		ValidationErrorDto errorDto = new ValidationErrorDto();
		errorDto.addFieldError(new FieldErrorDto("mailDeliveryChannelDto.name", "至少输入1个字且最多允许输入100个字"));
		
		mockMvc.perform(put("/admin/mail-delivery-channels/{id}", 1)
				.with(user(securityUser()))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtils.convertObjectToJsonString(channelDto))) 
        		.andExpect(status().isBadRequest())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().string(TestUtils.convertObjectToJsonString(errorDto)));
		
	}
	
	@Test
    @DatabaseSetup({"userData.xml", "saveMailDeliveryChannelsExpected.xml"})
    @ExpectedDatabase(value = "saveMailDeliveryChannelsExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void testGetById() throws Exception {
		mockMvc.perform(get("/admin/mail-delivery-channels/{id}", 1)
				.with(user(securityUser())))
				.andExpect(status().isOk());  
	}
	
}
