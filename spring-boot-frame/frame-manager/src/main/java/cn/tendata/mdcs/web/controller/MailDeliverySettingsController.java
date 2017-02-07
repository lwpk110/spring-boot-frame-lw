package cn.tendata.mdcs.web.controller;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailDeliverySettings;
import cn.tendata.mdcs.service.UserMailDeliverySettingsService;
import cn.tendata.mdcs.web.bind.annotation.CurrentUser;
import cn.tendata.mdcs.web.model.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;

@Controller
@RequestMapping("/user/delivery_settings")
@SessionAttributes(types = UserMailDeliverySettings.class)
public class MailDeliverySettingsController {

    @Autowired
    private UserMailDeliverySettingsService userMailDeliverySettingsService;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String list(ModelMap map,
                       @SortDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Sort sort,
                       final @CurrentUser User user) {
        Pageable pageable = new PageRequest(0, Integer.MAX_VALUE, sort);
        List<UserMailDeliverySettings> deliverySettings = userMailDeliverySettingsService.getAll(user, pageable).getContent();
        map.addAttribute("deliverySettings", deliverySettings);
        map.addAttribute("newDeliverySettings", new UserMailDeliverySettings());
        return "delivery_settings/list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse add(
            @Validated
            @ModelAttribute(value = "newDeliverySettings")
            UserMailDeliverySettings form,
            final @CurrentUser User user,
            SessionStatus status,
            JsonResponse jsonResponse) {
        UserMailDeliverySettings mailDeliverySettings = userMailDeliverySettingsService.getChecked(user);
        if (mailDeliverySettings == null) {
            form.setChecked(true);
        }
        form.setUser(user);
        userMailDeliverySettingsService.save(form);
        status.setComplete();
        return jsonResponse;
    }

    @RequestMapping(value = "{id}/edit", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse updateForm(
            @PathVariable("id") UserMailDeliverySettings form,
            ModelMap map,
            JsonResponse jsonResponse) {
        map.addAttribute("userMailDeliverySettings", form);
        jsonResponse.setContent(form);
        return jsonResponse;
    }

    @RequestMapping(value = "{id}/edit", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse update(
            @Validated @ModelAttribute(value = "userMailDeliverySettings") UserMailDeliverySettings form,
            SessionStatus status,
            JsonResponse jsonResponse) {
        userMailDeliverySettingsService.save(form);
        status.setComplete();
        return jsonResponse;
    }

    @RequestMapping(value = "{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse delete(
            @PathVariable("id") UserMailDeliverySettings userMailDeliverySettings,
            final @CurrentUser User user,
            JsonResponse jsonResponse) {
        if (userMailDeliverySettings.getUser().getId() == user.getId()) {
            userMailDeliverySettingsService.delete(userMailDeliverySettings.getId());
        } else {
            jsonResponse.setStatus(JsonResponse.Failure);
        }
        return jsonResponse;
    }

    @RequestMapping("{id}/default_delivery_change")
    @ResponseBody
    public JsonResponse changeDelivery(
            @PathVariable("id") UserMailDeliverySettings target,
            JsonResponse jsonResponse) {
        userMailDeliverySettingsService.setChecked(target);
        return jsonResponse;
    }
}
