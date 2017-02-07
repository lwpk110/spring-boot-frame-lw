package cn.tendata.mdcs.admin.web.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import cn.tendata.mdcs.admin.web.model.FieldErrorDto;
import cn.tendata.mdcs.admin.web.model.ValidationErrorDto;
import cn.tendata.mdcs.service.model.DirectDepositDetail;
import cn.tendata.mdcs.service.model.SearchKeywordType;

public class UserControllerTest extends SecurityMockMvcTestSupport{

	@Autowired
	MessageSource message;
	
	@Test
	@DatabaseSetup("userData.xml")
	public void testList() throws Exception {
		
		mockMvc.perform(get("/admin/users").with(user(securityUser()))
				.param("keyword", "")
				.param("type", SearchKeywordType.USERNAME.toString()))
				.andExpect(status().isOk());  
		
	}
	
	@Test
	@DatabaseSetup({"userData.xml"})
	@ExpectedDatabase(value = "userDataHasChangedExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void testDeposit() throws Exception {
		
		DirectDepositDetail deposit = new DirectDepositDetail();
		deposit.setCredits(100);
		
		mockMvc.perform(post("/admin/users/{userId}/deposit", 1).with(user(securityUser()))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtils.convertObjectToJsonBytes(deposit)))
				.andExpect(status().isOk());  
		
	}
	
	@Test
	@DatabaseSetup({"userData.xml"})
	public void testDepositWhenCreditsLessThanOne() throws Exception {
		
		ValidationErrorDto errorDto = new ValidationErrorDto();
		errorDto.addFieldError(new FieldErrorDto("directDepositDetail.credits", "值为大于0的整数"));
		
		DirectDepositDetail deposit = new DirectDepositDetail();
		deposit.setCredits(-100); 
		
		mockMvc.perform(post("/admin/users/{userId}/deposit", 1).with(user(securityUser()))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtils.convertObjectToJsonString(deposit))) 
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().string(TestUtils.convertObjectToJsonString(errorDto)));
		
	}
	
	@Test
	@DatabaseSetup("userData.xml")
	public void testListTransactionDetail() throws Exception {
		
		mockMvc.perform(get("/admin/users/transactions").with(user(securityUser()))
				.param("keyword", "")
				.param("type", SearchKeywordType.USERNAME.toString())
				.param("start", "")
				.param("end", ""))
				.andExpect(status().isOk());  
		
	}
	
}
