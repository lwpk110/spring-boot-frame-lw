package cn.tendata.ftp.webpower.manager.csv;

import cn.tendata.ftp.webpower.core.WebPowerMailTaskReportFilter;
import cn.tendata.mdcs.data.elasticsearch.domain.MailRecipientActionDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class CsvItemWriter extends RepositoryItemWriter {
    private final Logger log = LoggerFactory.getLogger(CsvItemWriter.class);
    private final WebPowerMailTaskReportFilter webPowerMailTaskReportFilter;

    public CsvItemWriter(WebPowerMailTaskReportFilter webPowerMailTaskReportFilter) {
        this.webPowerMailTaskReportFilter = webPowerMailTaskReportFilter;
    }

    @Override
    public void write(List items) throws Exception {

        List<MailRecipientActionDocument> list = items;
        Map<String, MailRecipientActionDocument> map = new HashMap<>(3000, 0.75f);
        for (MailRecipientActionDocument item : list) {
            webPowerMailTaskReportFilter.filtRecipientAction(map, item);
        }
        List<MailRecipientActionDocument> newList = new ArrayList<>(map.values());
        log.info("---->写入es：{}条", newList.size());
        super.write(newList);
    }
}

