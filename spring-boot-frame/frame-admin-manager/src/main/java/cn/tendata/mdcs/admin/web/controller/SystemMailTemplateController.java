package cn.tendata.mdcs.admin.web.controller;

import cn.tendata.mdcs.admin.web.model.MailDeliveryChannelDto;
import cn.tendata.mdcs.admin.web.model.SystemTemplateDto;
import cn.tendata.mdcs.admin.web.util.ViewUtils;
import cn.tendata.mdcs.data.domain.SystemMailTemplate;
import cn.tendata.mdcs.service.SystemMailTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by Luo Min on 2017/1/17.
 */
@Controller("admin#systemTemplateController")
@RequestMapping(ViewUtils.ADMIN_PATH_PREFIX + "/system_templates")
public class SystemMailTemplateController {

    private final SystemMailTemplateService systemMailTemplateService;

    @Autowired
    public SystemMailTemplateController(SystemMailTemplateService systemMailTemplateService) {
        this.systemMailTemplateService = systemMailTemplateService;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ResponseEntity<Page<SystemMailTemplate>> list(@PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<SystemMailTemplate> templates = systemMailTemplateService.getAll(pageable);
        return new ResponseEntity<>(templates, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@Valid @RequestBody SystemTemplateDto systemTemplateDto ) {
        SystemMailTemplate template = new SystemMailTemplate();
        template.setName(systemTemplateDto.getName());
        template.setHtmlContent(systemTemplateDto.getHtmlContent());
        template.setPortraits(systemTemplateDto.getPortraits());
        systemMailTemplateService.save(template);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<SystemMailTemplate> getById(@PathVariable("id") SystemMailTemplate systemMailTemplate) {
        return new ResponseEntity<>(systemMailTemplate, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@PathVariable("id") SystemMailTemplate systemMailTemplate,
                                       @Valid @RequestBody SystemTemplateDto systemTemplateDto) {
        systemMailTemplate.setHtmlContent(systemTemplateDto.getHtmlContent());
        systemMailTemplate.setName(systemTemplateDto.getName());
        systemMailTemplateService.save(systemMailTemplate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") SystemMailTemplate systemMailTemplateo) {
        systemMailTemplateService.delete(systemMailTemplateo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/images",method = RequestMethod.GET)
    public ResponseEntity<String[]> getImages(){
        String[] images = systemMailTemplateService.getAllSystemTemplateImage();
        return new ResponseEntity<String[]>(images,HttpStatus.OK);
    }
}
