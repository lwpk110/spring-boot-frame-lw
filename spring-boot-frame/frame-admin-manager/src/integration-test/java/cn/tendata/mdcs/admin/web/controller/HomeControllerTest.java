package cn.tendata.mdcs.admin.web.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;

import com.github.springtestdbunit.annotation.DatabaseSetup;

public class HomeControllerTest extends SecurityMockMvcTestSupport{

	@Test
	@DatabaseSetup("userData.xml")
	public void testShowDashboard() throws Exception {
		
		mockMvc.perform(get("/admin/").with(user(securityUser())))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/index"));
		
	}
	
}
