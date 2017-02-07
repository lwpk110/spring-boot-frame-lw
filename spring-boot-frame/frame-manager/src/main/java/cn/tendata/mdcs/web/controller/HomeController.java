package cn.tendata.mdcs.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailDeliverySettings;
import cn.tendata.mdcs.data.domain.UserMailRecipientGroup;
import cn.tendata.mdcs.service.UserMailDeliverySettingsService;
import cn.tendata.mdcs.service.UserMailDeliveryTaskService;
import cn.tendata.mdcs.service.UserMailRecipientGroupService;
import cn.tendata.mdcs.service.UserMailTemplateService;
import cn.tendata.mdcs.util.MailRecipientUtils;
import cn.tendata.mdcs.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/user")
public class HomeController {
	private UserMailDeliverySettingsService deliverySettingsService;
	private UserMailRecipientGroupService recipientGroupService;
	private UserMailTemplateService templateService;
	private UserMailDeliveryTaskService deliveryTaskService;

	@Autowired
	public HomeController(UserMailDeliverySettingsService deliverySettingsService,
			UserMailRecipientGroupService recipientGroupService, UserMailTemplateService templateService,
			UserMailDeliveryTaskService deliveryTaskService) {
		this.deliverySettingsService = deliverySettingsService;
		this.recipientGroupService = recipientGroupService;
		this.templateService = templateService;
		this.deliveryTaskService = deliveryTaskService;
	}

	@RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	public String home(ModelMap map, final @CurrentUser User user) {
		UserMailDeliverySettings deliverySettings = this.deliverySettingsService.getChecked(user);
		List<UserMailRecipientGroup> recipientGroups = this.recipientGroupService.getAll(user);
		int groupCount = recipientGroups.size();
		long recipientCount = MailRecipientUtils.getCount(recipientGroups);
		long tplCount = this.templateService.getCount(user);
		long taskCount = this.deliveryTaskService.getCount(user);
		map.addAttribute("user", user);
		map.addAttribute("deliverySettings", deliverySettings);
		map.addAttribute("groupCount", groupCount);
		map.addAttribute("recipientCount", recipientCount);
		map.addAttribute("tplCount", tplCount);
		map.addAttribute("taskCount", taskCount);
		return "home";
	}
}
