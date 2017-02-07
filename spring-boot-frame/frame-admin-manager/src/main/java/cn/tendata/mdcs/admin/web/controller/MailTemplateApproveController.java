package cn.tendata.mdcs.admin.web.controller;

import cn.tendata.mdcs.admin.web.util.SecurityAccess;
import cn.tendata.mdcs.admin.web.util.ViewUtils;
import cn.tendata.mdcs.data.domain.UserMailTemplate;
import cn.tendata.mdcs.service.UserMailTemplateService;
import cn.tendata.mdcs.util.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jeashi on 2016/9/21.
 */
@Controller("admin#mailTemplateApproveController")
@PreAuthorize(SecurityAccess.HAS_PERMISSION_MAIL_TEMPLATE_APPROVE)
@RequestMapping(ViewUtils.ADMIN_PATH_PREFIX + "/mail-template-approve")
public class MailTemplateApproveController {
    private UserMailTemplateService userMailTemplateService;

    @Autowired
    public MailTemplateApproveController(
            UserMailTemplateService userMailTemplateService) {
        this.userMailTemplateService = userMailTemplateService;
    }

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Page<UserMailTemplate>> list(
            @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserMailTemplate> entities = this.userMailTemplateService.getAllByApproveStatus(StatusEnum.WAITING_APPROVE.getStatus(), pageable);
        return new ResponseEntity<Page<UserMailTemplate>>(entities, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/approve", method = RequestMethod.POST)
    public ResponseEntity<Void> approve(
            @PathVariable("id") UserMailTemplate userMailTemplate,
            @RequestParam int status) {

        StatusEnum se = null;
        if (1 == status) {
            se = StatusEnum.PASS_APPROVE;
        } else {
            se =StatusEnum.REFUSE_APPROVE;
        }
        this.userMailTemplateService.approveTemplate(se,userMailTemplate.getId());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
