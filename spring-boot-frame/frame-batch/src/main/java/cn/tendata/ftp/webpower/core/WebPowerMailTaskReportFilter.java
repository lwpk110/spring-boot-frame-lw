package cn.tendata.ftp.webpower.core;

import cn.tendata.ftp.webpower.util.WebpowerRecipientActionStateUtils;
import cn.tendata.mdcs.data.elasticsearch.domain.MailRecipientActionDocument;
import java.util.Map;

/**
 * Created by ernest on 2016/9/19.
 */
public class WebPowerMailTaskReportFilter {

    public void filtRecipientAction(Map<String, MailRecipientActionDocument> map, MailRecipientActionDocument item) {
        MailRecipientActionDocument cacheMailRecipientActionDocument = map.get(item.getEmail()+"_"+item.getTaskId());
        if (!(null != cacheMailRecipientActionDocument &&
                (cacheMailRecipientActionDocument.getActionDate().isAfter(item.getActionDate())
                        || WebpowerRecipientActionStateUtils.isContain(
                        cacheMailRecipientActionDocument.getActionStatus(),
                        WebpowerRecipientActionStateUtils.FINAL_FLAG)))) {
            // 如果所读行的数据动作触发时间小于内存中的数据,忽略本行；
            map.put(item.getEmail()+"_"+item.getTaskId(), item);  //更新map
        }

    }
}
