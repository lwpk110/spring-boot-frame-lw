package cn.tendata.mdcs.admin.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.tendata.mdcs.admin.web.util.ViewUtils;

@Controller("admin#indexController")
@RequestMapping(ViewUtils.ADMIN_PATH_PREFIX)
public class IndexController {

	@RequestMapping({ "", "/" })
	public String home() {
        return "admin/index";
	}
	
}
