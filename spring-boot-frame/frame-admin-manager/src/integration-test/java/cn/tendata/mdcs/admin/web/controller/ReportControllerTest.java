package cn.tendata.mdcs.admin.web.controller;

import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Luo Min on 2016/12/5.
 */
public class ReportControllerTest extends MockMvcTestSupport {

    @Test
    public void testManualExecuteBatchTask()throws Exception{
        mockMvc.perform(get("/user/tool/report/execute_timing_task"))
                .andExpect(status().isOk());
    }
}
