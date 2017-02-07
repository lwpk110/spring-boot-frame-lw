package cn.tendata.mdcs.web.controller;

import cn.tendata.mdcs.web.mail.parse.MailRecipientRecord;
import cn.tendata.mdcs.web.mail.parse.MailRecipientResult;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MailRecipientGroupControllerTest extends SecurityMockMvcTestSupport {

    @Test
    @DatabaseSetup({"userData.xml", "userMailRecipientGroupData.xml"})
    public void listShouldAddRecipientGroupToModelAndRenderListView() throws Exception {
        mockMvc.perform(get("/user/recipient_group").with(user(securityUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("recipient_group/list"))
                .andExpect(model().attribute("groups", hasSize(3)))
                .andExpect(model().attribute("groups",
                        hasItem(allOf(
                                hasProperty("recipientCollection", hasProperty("recipientCount", is(3))),
                                hasProperty("name", is("group3"))))))
                .andDo(print());
    }

    @Test
    public void createFormShouldRenderFormView() throws Exception {
        mockMvc.perform(get("/user/recipient_group/add").with(user(securityUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("recipient_group/create_form_by_upload"));
    }

    @Test
    @DatabaseSetup({"userData.xml", "emptyUserMailRecipientGroupData.xml"})
    @ExpectedDatabase(value = "saveUserMailRecipientGroupExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void createNewRecipientGroupShouldAddRecipientGroupAndRedirectSuccessView() throws Exception {
        MailRecipientRecord r1 = new MailRecipientRecord(1, "aaa", "aaa@tendata.cn");
        MailRecipientRecord r2 = new MailRecipientRecord(2, "bbb", "bbb@tendata.cn");
        MailRecipientResult result = new MailRecipientResult();
        result.add(r1);
        result.add(r2);

        mockMvc.perform(post("/user/recipient_group/add").with(user(securityUser()))
                .param("name", "aaa")
                .sessionAttr("recipient", result))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/user/recipient_group/create_success"));
    }

    @Test
    public void cvsFileUploadShouldAddResultToSessionAndReturnJsonResponse() throws Exception {
        Resource resource = context.getResource("classpath:upload/userMailRecipientGroupData.csv");
        MockMultipartFile file = new MockMultipartFile("file", "orig", "application/vnd.ms-excel", resource.getInputStream());
        mockMvc.perform(fileUpload("/user/recipient_group/fileupload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.content.totality").value(5))
                .andExpect(jsonPath("$.content.validRecords").value(hasSize(3)))
                .andExpect(jsonPath("$.msg").doesNotExist())
                .andExpect(request().sessionAttribute("recipient", instanceOf(MailRecipientResult.class)));
    }

    @Test
    @DatabaseSetup({"userData.xml", "userMailRecipientGroupData.xml"})
    @ExpectedDatabase(value = "removeUserMailRecipientGroupExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void deleteShouldDeleteRecipientGroupAndReturnJsonResponse() throws Exception {
        mockMvc.perform(post("/user/recipient_group/{id}/delete", "4b2f02ca-d857-4c95-b7fa-ee6283ce0d64")
                .with(user(securityUser())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.content").doesNotExist())
                .andExpect(jsonPath("$.msg").doesNotExist())
                .andDo(print());
    }

    @Test
    @DatabaseSetup({"userData.xml", "userMailRecipientGroupData.xml", "userMailDeliveryChannelData.xml"})
    public void jsonRecipientGroupReportShouldReturnJsonMapReport() throws Exception {
        mockMvc.perform(get("/user/recipient_group/jsonRecipientGroupReport")
                .with(user(securityUser()))
                .param("cId", "1")
                .param("rgId[]", "4b2f02ca-d857-4c95-b7fa-ee6283ce0d64")
                .param("rgId[]", "4b2f02ca-d857-4c95-b7fa-ee6283ce0d65"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value(3))
                .andExpect(jsonPath("$.repeat").value(1))
                .andExpect(jsonPath("$.uniq").value(2))
                .andExpect(jsonPath("$.totalFee").value(2))
                .andDo(print());
    }
}
