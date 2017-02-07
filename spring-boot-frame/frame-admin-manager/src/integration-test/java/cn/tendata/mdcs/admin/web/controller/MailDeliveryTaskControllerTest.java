package cn.tendata.mdcs.admin.web.controller;

import cn.tendata.mdcs.service.model.SearchKeywordType;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class MailDeliveryTaskControllerTest extends SecurityMockMvcTestSupport {

    @Test
    @DatabaseSetup({"userData.xml", "mailDeliveryChannelData.xml", "userMailDeliveryTaskData.xml"})
    public void testList() throws Exception {
        mockMvc.perform(get("/admin/mail-delivery-tasks").with(user(securityUser()))
                .param("type", SearchKeywordType.USERNAME.toString())
                .param("keyword", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content").exists());
    }
   
    @Test
	@DatabaseSetup({"userData.xml", "mailDeliveryChannelData.xml", "userMailDeliveryTaskData.xml"})
	public void testTemplate() throws Exception {
		
		mockMvc.perform(get("/admin/mail-delivery-tasks/{id}/template", 
				"4b2f02ca-d857-4c95-b7fa-ee6283ce0d64").with(user(securityUser())))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/mail-delivery-task/template"));
		
	}
    
}
