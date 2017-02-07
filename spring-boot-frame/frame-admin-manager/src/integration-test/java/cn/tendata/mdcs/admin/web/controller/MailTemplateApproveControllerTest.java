package cn.tendata.mdcs.admin.web.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;

import com.github.springtestdbunit.annotation.DatabaseSetup;

public class MailTemplateApproveControllerTest extends SecurityMockMvcTestSupport{
    
	@Test
	@DatabaseSetup({"userData.xml", "userMailTemplateData.xml"})
	public void testList() throws Exception{
		mockMvc.perform(get("/admin/mail-template-approve/list").with(user(securityUser())))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.totalElements").value(2));
		
	}
	
	@Test
	@DatabaseSetup({"userData.xml", "userMailTemplateData.xml"})
	public void testApprove() throws Exception{
		mockMvc.perform(post("/admin/mail-template-approve/{id}/approve",1L).with(user(securityUser()))
				.param("status", "1"))
		.andExpect(status().isOk());			
	}
}
