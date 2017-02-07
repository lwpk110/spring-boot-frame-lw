package cn.tendata.mdcs.admin.web.controller;

import cn.tendata.ftp.webpower.core.BatchTaskScheduler;
import cn.tendata.mdcs.admin.web.util.SecurityAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Luo Min on 2016/12/5.
 */
@Controller
@RequestMapping("/user/tool")
@PreAuthorize(SecurityAccess.HAS_PERMISSION_ADMIN_USER_REPORT_VIEW)
public class ToolController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BatchTaskScheduler batchTaskScheduler;

    @Autowired
    public ToolController(BatchTaskScheduler batchTaskScheduler) {
        this.batchTaskScheduler = batchTaskScheduler;
    }

    @PreAuthorize(SecurityAccess.HAS_PERMISSION_ADMIN_USER_REPORT_MANAGE)
    @RequestMapping(value = "/report/execute_timing_task", method = RequestMethod.GET)
    public ResponseEntity<Void> executeBatchTask() {
        logger.info("准备启动线程更新报告");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    batchTaskScheduler.executeBatch();
                    logger.info("启动线程更新报告结束");
                } catch (Exception e) {
                    logger.info("启动线程更新报告出现异常,异常信息:{}",e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
