package cn.tendata.mdcs.web.controller;

import cn.tendata.cas.client.security.core.userdetails.LoginUser;
import cn.tendata.mdcs.data.domain.MailDeliveryChannel;
import cn.tendata.mdcs.data.domain.MailRecipient;
import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailDeliverySettings;
import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.data.domain.UserMailRecipientGroup;
import cn.tendata.mdcs.data.domain.UserMailTemplate;
import cn.tendata.mdcs.data.elasticsearch.domain.MailRecipientActionDocument;
import cn.tendata.mdcs.mail.MailDeliveryTaskReport;
import cn.tendata.mdcs.mail.MailRecipientAction;
import cn.tendata.mdcs.mail.MailRecipientAction.MailRecipientActionStatus;
import cn.tendata.mdcs.service.MailDeliveryChannelService;
import cn.tendata.mdcs.service.UserMailDeliverySettingsService;
import cn.tendata.mdcs.service.UserMailDeliveryTaskReportService;
import cn.tendata.mdcs.service.UserMailDeliveryTaskService;
import cn.tendata.mdcs.service.UserMailRecipientGroupService;
import cn.tendata.mdcs.service.UserMailTemplateService;
import cn.tendata.mdcs.service.model.MailDeliveryTaskDetail;
import cn.tendata.mdcs.util.MailRecipientUtils;
import cn.tendata.mdcs.web.bind.annotation.CurrentUser;
import cn.tendata.mdcs.web.jackson.WebDataView;
import cn.tendata.mdcs.web.model.JsonResponse;
import cn.tendata.mdcs.web.model.TaskQueryParameter;
import cn.tendata.mdcs.web.util.DataTablesParameter;
import cn.tendata.mdcs.web.util.DataTablesResult;
import cn.tendata.mdcs.web.util.MailRecipientActionUtils;
import com.fasterxml.jackson.annotation.JsonView;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user/delivery_task")
public class MailDeliveryTaskController {

    private final UserMailDeliveryTaskService deliveryTaskService;
    private final UserMailDeliveryTaskReportService deliveryTaskReportService;
    private final UserMailDeliverySettingsService deliverySettingsService;
    private final MailDeliveryChannelService deliveryChannelService;
    private final UserMailRecipientGroupService recipientGroupService;
    private final UserMailTemplateService templateService;

    @Autowired
    public MailDeliveryTaskController(UserMailDeliveryTaskService deliveryTaskService,
            UserMailDeliveryTaskReportService deliveryTaskReportService,
            UserMailDeliverySettingsService deliverySettingsService,
            MailDeliveryChannelService deliveryChannelService, UserMailRecipientGroupService recipientGroupService,
            UserMailTemplateService templateService) {
        this.deliveryTaskService = deliveryTaskService;
        this.deliveryTaskReportService = deliveryTaskReportService;
        this.deliverySettingsService = deliverySettingsService;
        this.deliveryChannelService = deliveryChannelService;
        this.recipientGroupService = recipientGroupService;
        this.templateService = templateService;
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String showHome(
            final @CurrentUser User user,
            final @AuthenticationPrincipal LoginUser loginUser,
            ModelMap map) {
        map.addAttribute("user", user);
        map.addAttribute("loginUser", loginUser);
        return "delivery_task/list";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @JsonView(WebDataView.UI.class)
    public DataTablesResult<UserMailDeliveryTask> list(
            @ModelAttribute(value = "taskQueryParameter") TaskQueryParameter taskQueryParameter,
            @ModelAttribute("parameter") DataTablesParameter parameter,
            @SortDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Sort sort,
            final @CurrentUser User user,
            final @AuthenticationPrincipal LoginUser loginUser,
            DataTablesResult<UserMailDeliveryTask> result) {
        Pageable pageable = new PageRequest(parameter.getPage(), parameter.getiDisplayLength(), sort);
        Page<UserMailDeliveryTask> deliveryTasks = this.deliveryTaskService
                .getAll(user, taskQueryParameter.getDateTimeQuery(), taskQueryParameter.getTaskName(),
                        taskQueryParameter.getUserQuery(loginUser.getParentUserId()), pageable);
        result.fillData(deliveryTasks, parameter);
        return result;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String createForm(final @CurrentUser User user, ModelMap map) {
        UserMailDeliverySettings defaultDelivery = this.deliverySettingsService.getChecked(user);
        List<UserMailDeliverySettings> deliverySettings = this.deliverySettingsService.getAll(user);
        List<MailDeliveryChannel> deliveryChannels = this.deliveryChannelService.getAvailableChannels();
        List<UserMailRecipientGroup> recipientGroups = this.recipientGroupService.getAll(user);
        List<UserMailTemplate> templates = this.templateService.getAllApprovePass(user);
        map.addAttribute("user", user);
        map.addAttribute("defaultDelivery", defaultDelivery);
        map.addAttribute("deliverySettings", deliverySettings);
        map.addAttribute("deliveryChannels", deliveryChannels);
        map.addAttribute("recipientGroups", recipientGroups);
        map.addAttribute("templates", templates);
        return "delivery_task/create_form";
    }

    @RequestMapping(value = "/create_success", method = RequestMethod.GET)
    public String createSuccess() {
        return "delivery_task/create_success";
    }

    @RequestMapping(value = "/submit_task", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse submitTask(
            @Validated @ModelAttribute MailDeliveryTaskDetail detail,
            final @CurrentUser User user,
            final @AuthenticationPrincipal LoginUser loginUser,
            JsonResponse result) {
        detail.setParentUserId(loginUser.getParentUserId());
        detail.setParentUsername(loginUser.getParentUsername());
        detail.setUser(user);
        this.deliveryTaskService.submit(detail);
        return result;
    }

    @RequestMapping(value = "/{id}/json_report", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse jsonReport(
            @PathVariable("id") UserMailDeliveryTask task,
            JsonResponse response) {
        MailDeliveryTaskReport report = deliveryTaskReportService.getReport(task);
        response.setContent(report);
        return response;
    }

    @RequestMapping(value = "/{id}/report", method = RequestMethod.GET)
    public String report(
            @PathVariable("id") UserMailDeliveryTask task,
            ModelMap map) {
        MailDeliveryTaskReport report = deliveryTaskReportService.getReport(task);
        map.addAttribute("report", report);
        map.addAttribute("task", task);
        map.addAttribute("recipientCount", task.getRecipientCollection().getRecipientCount());
        return "delivery_task/report";
    }

    @RequestMapping(value = "/{id}/mail_preview", method = RequestMethod.GET)
    public String mailPreview(@PathVariable("id") UserMailDeliveryTask task,
            ModelMap map) {
        map.addAttribute("body", task.getTemplate().getBody());
        return "template/preview";
    }

    @RequestMapping(value = "/{id}/retry", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse retry(
            @PathVariable("id") UserMailDeliveryTask task,
            JsonResponse result) {
        deliveryTaskService.retry(task);
        return result;
    }

    @RequestMapping(value = "/{id}/cancel", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse cancel(
            @PathVariable("id") UserMailDeliveryTask task,
            JsonResponse result) {
        deliveryTaskService.cancel(task);
        return result;
    }

    @RequestMapping(value = "/{id}/report_detail", method = RequestMethod.GET)
    @ResponseBody
    public DataTablesResult<MailRecipientAction> reportDetail(
            @PathVariable("id") UserMailDeliveryTask task,
            @ModelAttribute("parameter") DataTablesParameter parameter,
            DataTablesResult<MailRecipientAction> result) {
        List<MailRecipientAction> list =
                deliveryTaskReportService.getRecipientActions(task, parameter.getiDisplayStart(),
                        parameter.getEndIndex());
        Set<MailRecipientAction> actions = null == list ? new HashSet<>() : new HashSet<>(list);
        List<MailRecipientAction> newActions = new ArrayList<>(actions);
        result.fillData(newActions, parameter, task.getRecipientCollection().getRecipients().size());
        return result;
    }

    @RequestMapping(value = "/{id}/add_recipient_group", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse addRecipientGroup(
            @PathVariable("id") UserMailDeliveryTask task,
            @RequestParam("mailRecipientActionStatus") EnumSet<MailRecipientActionStatus> mailRecipientActionStatusSet,
            @RequestParam("name") String name,
            final @CurrentUser User user,
            JsonResponse response) {
        List<MailRecipientAction> mailRecipientActions = deliveryTaskReportService.getRecipientActions(task);
        Collection<MailRecipient> mailRecipients =
                MailRecipientActionUtils.getMailRecipients(task, mailRecipientActions, mailRecipientActionStatusSet);
        UserMailRecipientGroup group = new UserMailRecipientGroup(name, mailRecipients, user);
        recipientGroupService.save(group);
        return response;
    }

    @RequestMapping(value = "/recent_task_list", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse recentTaskList(final @CurrentUser User loginUser,
            JsonResponse response) {
        List<UserMailDeliveryTask> tasks = deliveryTaskService.getAll(loginUser, 5);
        response.setContent(tasks);
        return response;
    }

    @RequestMapping(value = "/mail_record", method = RequestMethod.GET)
    public String mailRecordPage() {
        return "delivery_task/mail_record";
    }

    @RequestMapping(value = "/mail_record_list", method = RequestMethod.GET)
    @ResponseBody
    public DataTablesResult<MailRecipientActionDocument> mailRecordList(
            @RequestParam(value = "email", required = false) String email,
            @ModelAttribute("parameter") DataTablesParameter parameter,
            @SortDefault(sort = {"actionDate"}, direction = Sort.Direction.DESC) Sort sort,
            final @CurrentUser User user,
            DataTablesResult<MailRecipientActionDocument> result) {
        Pageable pageable = new PageRequest(parameter.getPage(), parameter.getiDisplayLength(), sort);
        Page<MailRecipientActionDocument> documents = deliveryTaskReportService.search(user, email, pageable);
        result.fillData(documents, parameter);
        return result;
    }

    @RequestMapping(value = "/{id}/delivery_rollback", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse deliveryRollback(
            @PathVariable("id") UserMailDeliveryTask task,
            @ModelAttribute("name") String name,
            @CurrentUser User user,
            JsonResponse response) {
        Collection<MailRecipient> mailRecipients = task.getRecipientCollection().getRecipients();
        UserMailRecipientGroup group = new UserMailRecipientGroup(name, mailRecipients, user);
        recipientGroupService.save(group);
        return response;
    }

    @RequestMapping(value = "/recipients_count", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse getRecipientCollectionCount(
            @RequestParam("rgId[]") Collection<UserMailRecipientGroup> recipientGroups,
            JsonResponse response) {
        Map<String, Long> result = new HashMap<>();
        long retains = MailRecipientUtils.getRecipientCollection(recipientGroups).getRecipientCount();
        long total = MailRecipientUtils.getCount(recipientGroups);
        result.put("retains", retains);
        result.put("total", total);
        response.setContent(result);
        return response;
    }

    @RequestMapping("/{id}/export")
    public void exportToLocal(
            @PathVariable("id") UserMailDeliveryTask task,
            @RequestParam("mailRecipientActionStatus") EnumSet<MailRecipientActionStatus> mailRecipientActionStatusSet,
            HttpServletResponse response
    ) throws IOException {
        List<MailRecipientAction> mailRecipientActions = deliveryTaskReportService.getRecipientActions(task);
        Collection<MailRecipientAction> mailRecipients =
                MailRecipientActionUtils.filterByActionStatus(mailRecipientActions, mailRecipientActionStatusSet);
        File file = deliveryTaskReportService.export(mailRecipients);
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if (mimeType == null) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        response.setContentType(mimeType);
        response.setHeader("Content-disposition", "attachment; filename=" + file.getName());
        response.setContentLength((int) file.length());
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }
}
