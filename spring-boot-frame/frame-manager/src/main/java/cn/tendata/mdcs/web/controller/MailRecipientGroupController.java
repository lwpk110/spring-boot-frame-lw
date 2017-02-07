package cn.tendata.mdcs.web.controller;

import cn.tendata.mdcs.data.domain.MailDeliveryChannel;
import cn.tendata.mdcs.data.domain.MailRecipient;
import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailRecipientGroup;
import cn.tendata.mdcs.service.UserMailRecipientGroupService;
import cn.tendata.mdcs.util.JsonUtils;
import cn.tendata.mdcs.util.MailRecipientUtils;
import cn.tendata.mdcs.web.bind.annotation.CurrentUser;
import cn.tendata.mdcs.web.mail.parse.MailRecipientParser;
import cn.tendata.mdcs.web.mail.parse.MailRecipientRecord;
import cn.tendata.mdcs.web.mail.parse.MailRecipientResult;
import cn.tendata.mdcs.web.model.JsonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user/recipient_group")
public class MailRecipientGroupController {

    private UserMailRecipientGroupService userMailRecipientGroupService;
    private MailRecipientParser mailRecipientParser;

    @Autowired
    public MailRecipientGroupController(UserMailRecipientGroupService userMailRecipientGroupService, MailRecipientParser mailRecipientParser) {
        this.userMailRecipientGroupService = userMailRecipientGroupService;
        this.mailRecipientParser = mailRecipientParser;
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String list(ModelMap map,
                       @SortDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Sort sort,
                       final @CurrentUser User user) {
        Pageable pageable = new PageRequest(0, Integer.MAX_VALUE, sort);
        List<UserMailRecipientGroup> groups = userMailRecipientGroupService.getAll(user, pageable).getContent();
        map.addAttribute("groups", groups);
        return "recipient_group/list";
    }

    @RequestMapping(value = {"{id}/preview"}, method = RequestMethod.GET)
    public String previewList(@PathVariable("id") UserMailRecipientGroup group, ModelMap map) {
        Collection<MailRecipient> recipients = group.getRecipientCollection().getRecipients();
        map.addAttribute("recipients", recipients);
        return "recipient_group/preview";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String createFormByUpload(@RequestParam(value = "type",required = false) String type) {
        if(type == null){
            return "recipient_group/create_form_by_upload";
        }
        return "recipient_group/create_form_by_" + type;
    }

    @RequestMapping(value = "/error_record_preview", method = RequestMethod.GET)
    public String errorRecipientPreview(){
        return "recipient_group/error_record_preview";
    }

    @RequestMapping(value = "/add_by_input", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse createByInput(@RequestParam("content") String content,HttpSession httpSession,JsonResponse response) {
        MailRecipientResult result = (MailRecipientResult) this.mailRecipientParser.parse(content);
        httpSession.setAttribute("recipient", result);
        response.setContent(result);
        return response;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String create(
            @RequestParam String name,
            final @CurrentUser User user,
            HttpSession httpSession) {
        MailRecipientResult result = (MailRecipientResult) httpSession.getAttribute("recipient");
        httpSession.removeAttribute("recipient");
        UserMailRecipientGroup userMailRecipientGroup = new UserMailRecipientGroup(name, result.toMailRecipientCollection(), user);
        userMailRecipientGroup.setDisabledRecipientContent(JsonUtils.serialize(result.getNotExistRecords()));
        userMailRecipientGroup.setDisabledRecipientCount(result.getNotExistRecords().size());
        this.userMailRecipientGroupService.save(userMailRecipientGroup);
        return "redirect:/user/recipient_group/create_success";
    }

    @RequestMapping(value = "create_success", method = RequestMethod.GET)
    public String createSuccess() {
        return "recipient_group/create_success";
    }

    @RequestMapping(value = "/fileupload", produces = {MediaType.TEXT_PLAIN_VALUE})
    @ResponseBody
    public String cvsFileUpload(
            @RequestParam("file") MultipartFile file,
            HttpSession httpSession,
            JsonResponse jsonResponse) throws IOException {
        MailRecipientResult result = (MailRecipientResult) this.mailRecipientParser.parse(file);
        httpSession.setAttribute("recipient", result);
        jsonResponse.setContent(result);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(jsonResponse);
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse delete(
            @PathVariable("id") UserMailRecipientGroup recipientGroup,
            final @CurrentUser User user,
            JsonResponse jsonResponse) {
        if (recipientGroup.getUser().getId().equals(user.getId())) {
            this.userMailRecipientGroupService.delete(recipientGroup.getId());
        } else {
            jsonResponse.setStatus(JsonResponse.Failure);
        }
        return jsonResponse;
    }

    @RequestMapping(value = "/jsonRecipientGroupReport", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> jsonRecipientGroupReport(final @CurrentUser User user,
                                                        @RequestParam("rgId[]") Collection<UserMailRecipientGroup> recipientGroups,
                                                        @RequestParam("cId") MailDeliveryChannel deliveryChannel) {
        Map<String, Object> map = new HashMap<>();
        int sum = MailRecipientUtils.getCount(recipientGroups);
        int uniq = MailRecipientUtils.getRecipientCollection(recipientGroups).getRecipientCount();
        map.put("sum", sum);
        map.put("repeat", sum - uniq);
        map.put("uniq", uniq);
        map.put("totalFee", uniq * deliveryChannel.getFee());
        return map;
    }
}
