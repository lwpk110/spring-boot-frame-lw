package cn.tendata.mdcs.admin.web.controller;

import cn.tendata.mdcs.admin.web.util.SecurityAccess;
import cn.tendata.mdcs.admin.web.util.ViewUtils;
import cn.tendata.mdcs.data.domain.MailTemplate;
import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.mail.MailDeliveryTaskReport;
import cn.tendata.mdcs.service.UserMailDeliveryTaskReportService;
import cn.tendata.mdcs.service.UserMailDeliveryTaskService;
import cn.tendata.mdcs.service.model.SearchKeywordType;
import org.joda.time.DateTime;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller("admin#taskController")
@PreAuthorize(SecurityAccess.HAS_PERMISSION_ADMIN_TASK_VIEW)
@RequestMapping(ViewUtils.ADMIN_PATH_PREFIX + "/mail-delivery-tasks")
public class MailDeliveryTaskController {
    private UserMailDeliveryTaskService userMailDeliveryTaskService;
    private UserMailDeliveryTaskReportService userMailDeliveryTaskReportService;

    @Autowired
    public MailDeliveryTaskController(
    		UserMailDeliveryTaskService userMailDeliveryTaskService, 
    		UserMailDeliveryTaskReportService userMailDeliveryTaskReportService) {
        this.userMailDeliveryTaskService = userMailDeliveryTaskService;
        this.userMailDeliveryTaskReportService = userMailDeliveryTaskReportService;
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Page<UserMailDeliveryTask>> list(
            @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, name = "keyword") String keyword,
            @RequestParam("type") SearchKeywordType type,
            @RequestParam(required = false, name = "startDate") DateTime startDate,
            @RequestParam(required = false, name = "endDate") DateTime endDate) {
        Page<UserMailDeliveryTask> entities = this.userMailDeliveryTaskService
        		.getAll(type, keyword, startDate, endDate, pageable);
        return new ResponseEntity<Page<UserMailDeliveryTask>>(entities, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/template", method = RequestMethod.GET)
    public String template(
    		@PathVariable("id") 
    		UserMailDeliveryTask userMailDeliveryTask, 
    		ModelMap mode) {
    	MailTemplate taskTemplate = userMailDeliveryTask.getTemplate();
    	mode.addAttribute("taskTemplate", taskTemplate);
    	return "admin/mail-delivery-task/template";
    }
    
    @ResponseBody
    @RequestMapping(value = "/{id}/report", method = RequestMethod.GET)
    public ResponseEntity<MailDeliveryTaskReport> report(
    		@PathVariable("id") UserMailDeliveryTask userMailDeliveryTask) {
        MailDeliveryTaskReport mailDeliveryTaskReport = this.userMailDeliveryTaskReportService
        		.getReport(userMailDeliveryTask);
        return new ResponseEntity<MailDeliveryTaskReport> (mailDeliveryTaskReport, HttpStatus.OK);
    }
}
