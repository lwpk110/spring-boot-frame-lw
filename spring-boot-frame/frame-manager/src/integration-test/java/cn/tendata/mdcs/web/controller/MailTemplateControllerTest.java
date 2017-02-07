package cn.tendata.mdcs.web.controller;

import cn.tendata.mdcs.data.domain.MailTemplate;
import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailTemplate;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.Test;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MailTemplateControllerTest extends SecurityMockMvcTestSupport {

    @Test
    @DatabaseSetup({"userData.xml", "userMailTemplateData.xml"})
    public void listShouldAddPartnersToModelAndRenderListView() throws Exception {
        mockMvc.perform(get("/user/template/list").with(user(securityUser()))
                .param("iDisplayLength", "10")
                .param("iColumns", "4")
                .param("sEcho", "1")
                .param("iColumns", ",,,"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aaData").exists())
                .andExpect(jsonPath("$.iTotalDisplayRecords").exists())
                .andExpect(jsonPath("$.iTotalRecords").exists())
                .andExpect(jsonPath("$.sEcho").exists());
    }

    @Test
    public void createFormShouldRenderFormView() throws Exception {
        mockMvc.perform(get("/user/template/add").with(user(securityUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("template/create_or_update_form"))
                .andExpect(model().size(1))
                .andExpect(model().attributeExists("template"));
    }

    @Test
    @DatabaseSetup({"userData.xml", "emptyUserMailTemplateData.xml"})
    @ExpectedDatabase(value = "saveUserMailTemplateExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void createNewTemplateShouldAddTemplateAndRedirectSuccessView() throws Exception {
        mockMvc.perform(post("/user/template/add").with(user(securityUser()))
                .param("name", "aaa")
                .param("template.subject", "test template")
                .param("template.body", "test template")
                .param("systemTemplateId",new String[]{"1"})
                .sessionAttr("template", new UserMailTemplate()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/user/template/create_or_update_success"));
    }

    @Test
    @DatabaseSetup({"userData.xml", "userMailTemplateData.xml"})
    public void updateShouldAddTemplateToModelAndRenderFormView() throws Exception {
        mockMvc.perform(get("/user/template/{id}/edit", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("template/create_or_update_form"))
                .andExpect(model().attribute("template", hasProperty("id", is(1L))))
                .andExpect(model().attribute("template", hasProperty("name", is("aaa"))));
    }

    @Test
    @DatabaseSetup({"userData.xml", "saveUserMailTemplateExpected.xml"})
    @ExpectedDatabase(value = "updateUserMailTemplateExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void updateShouldUpdateTemplateAndRedirectListView() throws Exception {
        MailTemplate mailTemplate = new MailTemplate();
        mailTemplate.setBody("test template");
        mailTemplate.setSubject("test template");
        mailTemplate.setHtml(false);

        User user = new User(1, "aaa");

        UserMailTemplate userMailTemplate = new UserMailTemplate();
        userMailTemplate.setId(1L);
        userMailTemplate.setName("aaa");
        userMailTemplate.setTemplate(mailTemplate);
        userMailTemplate.setUser(user);

        mockMvc.perform(post("/user/template/{id}/edit", 1L)
                .param("name", "aaaa")
                .sessionAttr("template", userMailTemplate))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/user/template/create_or_update_success"));
    }

    @Test
    @DatabaseSetup({"userData.xml", "userMailTemplateData.xml"})
    @ExpectedDatabase(value = "removeUserMailTemplateExpected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void deleteShouldDeleteTemplateAndReturnJsonResponse() throws Exception {
        mockMvc.perform(post("/user/template/{id}/delete", 1L)
                .with(user(securityUser())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.content").doesNotExist())
                .andExpect(jsonPath("$.msg").doesNotExist());
    }
}
