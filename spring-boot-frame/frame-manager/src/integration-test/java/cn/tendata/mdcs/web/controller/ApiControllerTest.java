package cn.tendata.mdcs.web.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.util.DigestUtils;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import cn.tendata.mdcs.data.domain.MailRecipient;
import cn.tendata.mdcs.web.model.MailSouRecipientsDto;

public class ApiControllerTest extends MockMvcTestSupport {

    @Test
    public void importMailSouRecipients_TokenNotValid_ShouldReturnValidationErrors() throws Exception {
        MailSouRecipientsDto recipientsDto = new MailSouRecipientsDto();
        recipientsDto.setUserId(1L);
        recipientsDto.setUsername("aaa");
        recipientsDto.setToken("111111");
        recipientsDto.setName("mailsou recipients group");
        recipientsDto.setRecipients(Arrays.asList(new MailRecipient("test1@tendata.cn"), new MailRecipient("test2@tendata.cn")));
        mockMvc.perform(post("/api/mailsou/recipients/import")
                .contentType(TestUtils.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(recipientsDto)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success", is("F")))
            .andExpect(jsonPath("$.errors", contains("token, TokenNotValid")));
    }
    
    @Test
    public void importMailSouRecipients_RecipientsIsEmpty_ShouldReturnValidationErrors() throws Exception {
        MailSouRecipientsDto recipientsDto = new MailSouRecipientsDto();
        recipientsDto.setUserId(1L);
        recipientsDto.setUsername("aaa");
        String token = DigestUtils.md5DigestAsHex(
                String.format("%s:%s@%s", recipientsDto.getUserId(), recipientsDto.getUsername(), "123456").getBytes());
        recipientsDto.setToken(token);
        recipientsDto.setName("mailsou recipients group");
        mockMvc.perform(post("/api/mailsou/recipients/import")
                .contentType(TestUtils.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(recipientsDto)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success", is("F")))
            .andExpect(jsonPath("$.errors", contains("recipients, NotNull")));;
    }
    
//    @Test
//    public void importMailSouRecipients_EmailNotValid_ShouldReturnValidationErrors() throws Exception {
//        MailSouRecipientsDto recipientsDto = new MailSouRecipientsDto();
//        recipientsDto.setUserId(1L);
//        recipientsDto.setUsername("aaa");
//        String token = DigestUtils.md5DigestAsHex(
//                String.format("%s:%s@%s", recipientsDto.getUserId(), recipientsDto.getUsername(), "123456").getBytes());
//        recipientsDto.setToken(token);
//        recipientsDto.setName("mailsou recipients group");
//        recipientsDto.setRecipients(Arrays.asList(new MailRecipient("test1@tendata.cn"), new MailRecipient("111111")));
//        mockMvc.perform(post("/api/mailsou/recipients/import")
//                .contentType(TestUtils.APPLICATION_JSON_UTF8)
//                .content(TestUtils.convertObjectToJsonBytes(recipientsDto)))
//            .andDo(print())
//            .andExpect(status().isBadRequest())
//            .andExpect(jsonPath("$.success", is("F")))
//            .andExpect(jsonPath("$.errors", contains("recipients[1].email, Email")));;
//    }
    
    @Test
    @DatabaseSetup("userData.xml")
    @ExpectedDatabase(value = "importMailSouRecipientsExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void importMailSouRecipients_ShouldAddUserMailRecipientGroupAndReturnHttpStatusOk() throws Exception {
        MailSouRecipientsDto recipientsDto = new MailSouRecipientsDto();
        recipientsDto.setUserId(1L);
        recipientsDto.setUsername("aaa");
        String token = DigestUtils.md5DigestAsHex(
                String.format("%s:%s@%s", recipientsDto.getUserId(), recipientsDto.getUsername(), "123456").getBytes());
        recipientsDto.setToken(token);
        recipientsDto.setName("mailsou recipients group");
        recipientsDto.setRecipients(Arrays.asList(new MailRecipient("test1@tendata.cn"), new MailRecipient("test2@tendata.cn")));
        
        mockMvc.perform(post("/api/mailsou/recipients/import")
                .contentType(TestUtils.APPLICATION_JSON_UTF8)
                .content(TestUtils.convertObjectToJsonBytes(recipientsDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is("T")));
    }
}
