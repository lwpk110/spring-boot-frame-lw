package cn.tendata.mdcs.web.controller;

import cn.tendata.mdcs.data.domain.SystemMailTemplate;
import cn.tendata.mdcs.service.SystemMailTemplateService;
import cn.tendata.mdcs.util.CollectionUtils;
import cn.tendata.mdcs.web.model.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Luo Min on 2017/1/17.
 */
@Controller
@RequestMapping("/user/system_template")
public class SystemMailTemplateController {

    private final SystemMailTemplateService systemMailTemplateService;

    @Autowired
    public SystemMailTemplateController(SystemMailTemplateService systemMailTemplateService) {
        this.systemMailTemplateService = systemMailTemplateService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse list(@SortDefault(sort = {"createdDate"}, direction = Sort.Direction.ASC) Sort sort,JsonResponse response) {
        List<SystemMailTemplate> templates = CollectionUtils.toList(systemMailTemplateService.getAll(sort));
        response.setContent(templates);
        return response;
    }

    @RequestMapping(value = "/use/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse increaseUseCount(@PathVariable("id") SystemMailTemplate template, JsonResponse response) {
        template.increaseUseCount();
        systemMailTemplateService.save(template);
        return response;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getById(@PathVariable("id") SystemMailTemplate template, JsonResponse response) {
       response.setContent(template);
        return response;
    }
}
