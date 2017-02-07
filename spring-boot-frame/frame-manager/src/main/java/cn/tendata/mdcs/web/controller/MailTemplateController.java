package cn.tendata.mdcs.web.controller;

import cn.tendata.mdcs.data.domain.SystemMailTemplate;
import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailTemplate;
import cn.tendata.mdcs.service.SystemMailTemplateService;
import cn.tendata.mdcs.service.UserMailTemplateService;
import cn.tendata.mdcs.util.StatusEnum;
import cn.tendata.mdcs.web.bind.annotation.CurrentUser;
import cn.tendata.mdcs.web.jackson.WebDataView;
import cn.tendata.mdcs.web.model.JsonResponse;
import cn.tendata.mdcs.web.util.DataTablesParameter;
import cn.tendata.mdcs.web.util.DataTablesResult;
import com.fasterxml.jackson.annotation.JsonView;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.io.IOException;

@Controller
@RequestMapping("/user/template")
@SessionAttributes(types = UserMailTemplate.class)
public class MailTemplateController {

    private final UserMailTemplateService userMailTemplateService;
    private final SystemMailTemplateService systemMailTemplateService;

    @Autowired
    public MailTemplateController(UserMailTemplateService userMailTemplateService, SystemMailTemplateService systemMailTemplateService) {
        this.userMailTemplateService = userMailTemplateService;
        this.systemMailTemplateService = systemMailTemplateService;
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String showIndex() {
        return "template/list";
    }

    @RequestMapping(value = {"list"}, method = RequestMethod.GET)
    @ResponseBody
    @JsonView(WebDataView.UI.class)
    public DataTablesResult<UserMailTemplate> list(
            @ModelAttribute DataTablesParameter parameter,
            @SortDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Sort sort,
            final @CurrentUser User user,
            DataTablesResult<UserMailTemplate> result) {
        Pageable pageable = new PageRequest(parameter.getPage(), parameter.getiDisplayLength(), sort);
        Page<UserMailTemplate> templates = this.userMailTemplateService.getAll(user, pageable);
        result.fillData(templates, parameter);
        return result;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String createForm(ModelMap map) {
        UserMailTemplate form = new UserMailTemplate();
        map.addAttribute("template", form);
        return "template/create_or_update_form";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String create(
            @Validated @ModelAttribute(value = "template") UserMailTemplate form,
            @RequestParam("systemTemplateId") Integer[] ids,
            final @CurrentUser User user,
            SessionStatus status,
            Errors errors) {
        if (errors.hasErrors()) {
            return "template/create_or_update_form";
        }
        for (int i = 0; i < ids.length - 1; i++) {
            SystemMailTemplate systemTemplate = systemMailTemplateService.getById(ids[i]);
            if (systemTemplate != null) {
                systemTemplate.increaseUseCount();
                systemMailTemplateService.save(systemTemplate);
            }
        }
        form.setUser(user);
        form.setApproveStatus(StatusEnum.WAITING_APPROVE.getStatus());
        form.setCreatedDate(new DateTime());
        form.setLastModifiedDate(new DateTime());
        this.userMailTemplateService.save(form);
        status.setComplete();
        return "redirect:/user/template/create_or_update_success";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") UserMailTemplate template, ModelMap map) {
        map.addAttribute("template", template);
        return "template/create_or_update_form";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String update(
            @Validated @ModelAttribute(value = "template") UserMailTemplate form,
            SessionStatus status,
            Errors errors) {
        if (errors.hasErrors()) {
            return "template/create_or_update_form";
        }
        form.setApproveStatus(StatusEnum.WAITING_APPROVE.getStatus());
        form.setLastModifiedDate(new DateTime());
        this.userMailTemplateService.save(form);
        status.setComplete();
        return "redirect:/user/template/create_or_update_success";
    }

    @RequestMapping(value = "/create_or_update_success", method = RequestMethod.GET)
    public String createOrUpdateSuccess() {
        return "template/create_or_update_success";
    }

    @RequestMapping(value = "{id}/preview", method = RequestMethod.GET)
    public String showPreview(
            @PathVariable("id") UserMailTemplate template,
            final @CurrentUser User user,
            ModelMap map) throws IOException {
        if (template.getUser().getId() == user.getId()) {
            map.addAttribute("body", template.getTemplate().getBody());
            return "template/preview";
        }
        return "error";
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse delete(
            @PathVariable("id") UserMailTemplate template,
            final @CurrentUser User user,
            JsonResponse jsonResponse) {
        if (template.getUser().getId() == user.getId()) {
            this.userMailTemplateService.delete(template.getId());
        } else {
            jsonResponse.setStatus(JsonResponse.Failure);
        }
        return jsonResponse;
    }

    @RequestMapping(value = "/note", method = RequestMethod.GET)
    public String note() {
        return "template/note";
    }
}
