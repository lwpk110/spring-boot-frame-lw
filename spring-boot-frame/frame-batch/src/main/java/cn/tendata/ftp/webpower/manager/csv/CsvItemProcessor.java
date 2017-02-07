package cn.tendata.ftp.webpower.manager.csv;

import cn.tendata.ftp.webpower.core.BatchJobListener;
import cn.tendata.ftp.webpower.core.skip.BatchSkipException;
import cn.tendata.ftp.webpower.model.WebpowerBatchProperties;
import cn.tendata.ftp.webpower.model.WebpowerReportDto;
import cn.tendata.ftp.webpower.util.WebpowerChannelActionStatusTransUtil;
import cn.tendata.ftp.webpower.util.WebpowerRecipientActionStateUtils;
import cn.tendata.mdcs.data.elasticsearch.domain.MailRecipientActionDocument;
import cn.tendata.mdcs.mail.MailRecipientAction.MailRecipientActionStatus;
import cn.tendata.mdcs.service.UserMailDeliveryTaskReportService;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

/**
 *
 */
public class CsvItemProcessor implements ItemProcessor<WebpowerReportDto, MailRecipientActionDocument> {

    private final Logger log = LoggerFactory.getLogger(CsvItemProcessor.class);
    private Validator<? super WebpowerReportDto> validator;
    private boolean filter = false;
    private Resource resource;

    @Autowired
    private WebpowerBatchProperties webpowerBatchProperties;

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void setValidator(Validator<? super WebpowerReportDto> validator) {
        this.validator = validator;
    }

    private final UserMailDeliveryTaskReportService userMailDeliveryTaskReportService;

    public CsvItemProcessor(UserMailDeliveryTaskReportService
        userMailDeliveryTaskReportService) {
        this.userMailDeliveryTaskReportService = userMailDeliveryTaskReportService;
    }

    @Override
    public MailRecipientActionDocument process(WebpowerReportDto item) {
        validateItem(item);
        collectionTaskIdForSingleBatch(item.getDmdMailingName());

        MailRecipientActionStatus itemMailRecipientActionStatus =
            WebpowerChannelActionStatusTransUtil.transToCommonStatus(item.getDmdType());
        MailRecipientActionDocument mailRecipientActionDocumentEs = userMailDeliveryTaskReportService
            .getMailRecipientActionDocument(
                item.getDmdMailingName(),
                item.getEmail());


        if (mailRecipientActionDocumentEs == null) {
            mailRecipientActionDocumentEs = new MailRecipientActionDocument();
        } else {
            /**
             * 检索es是否有值，有则判断是否为最终状态，为最终状态则返回null,不写入本行数据,
             * 不是最终状态判断触发时间小于es中时间也不写入本行数据。
             *
             */
            if (null != mailRecipientActionDocumentEs.getActionStatus()) {
                if (WebpowerRecipientActionStateUtils.isContain(mailRecipientActionDocumentEs.getActionStatus(),
                    WebpowerRecipientActionStateUtils.FINAL_FLAG)) {
                    return null;
                } else {

                    if (adjust(mailRecipientActionDocumentEs.getActionStatus(), itemMailRecipientActionStatus,
                        WebpowerRecipientActionStateUtils.PROCESS_FLAG, 0)) {
                        /*
                        判断 本行所读数据状态和es中数据状态的位置下标；
                        * 下标小于es中则不更新
                        */
                        return null;
                    } else if (adjust(mailRecipientActionDocumentEs.getActionStatus(), itemMailRecipientActionStatus,
                        WebpowerRecipientActionStateUtils.PROCESS_FLAG, 1)
                        && !(mailRecipientActionDocumentEs.getActionDate().isBefore(item.getDmdLogDate()))) {
                        /*判断 本行所读数据状态和es中数据状态的位置下标；
                        * 下标==es中并且触发时间 <= es 中的 ，返回null，则不更新
                        */
                        return null;
                    }
                }
            } else {
                mailRecipientActionDocumentEs.setActionStatus(itemMailRecipientActionStatus);
            }
        }
        mailRecipientActionDocumentEs.setActionStatus(itemMailRecipientActionStatus);
        createMailRecipientActionDocument(item, mailRecipientActionDocumentEs);
        return mailRecipientActionDocumentEs;
    }

    private boolean adjust(MailRecipientActionStatus actionStatus1, MailRecipientActionStatus actionStatus2,
        int stateFlag, int logicFlag) {
        if (logicFlag == 0) {  //    >
            return (WebpowerRecipientActionStateUtils.isContain(actionStatus2, stateFlag))
                && (WebpowerRecipientActionStateUtils.isAfter(actionStatus1, actionStatus2, stateFlag));
        } else if (logicFlag == 1) {  //    ==
            return (WebpowerRecipientActionStateUtils.isContain(actionStatus2, stateFlag))
                && (WebpowerRecipientActionStateUtils.isEquals(actionStatus1, actionStatus2, stateFlag));
        }

        return false;
    }

    private void validateItem(WebpowerReportDto item) {
        try {
            this.validator.validate(item);
        } catch (ValidationException var2) {
            log.info("====== a file named: {" + resource.getFilename()
                + "},mail: <" + item.getEmail() + "> has a error:{entity validate err},will  skip");
            writeErrLogs("====== a file named: {" + resource.getFilename()
                + "},mail: <" + item.getEmail() + "> has a error:{entity validate err},will  skip");
            log.info("本行字段验证结果：\n" + String.valueOf(var2));
            writeErrLogs("本行字段验证结果：\n" + String.valueOf(var2));
            if (!this.filter) {
                throw new BatchSkipException(var2.toString());
            }
        }
    }

    private void writeErrLogs(String logsInfo) {
        try {
            FileUtils.forceMkdir(new File(webpowerBatchProperties.getBatchDirErrLogs()));
            FileUtils.writeStringToFile(
                new File(webpowerBatchProperties.getBatchDirErrLogs() + File.separator + "validate-err-"+(new DateTime().toString(
                    "yyyyMMdd") + ".log")),
                new DateTime().toString("yyyy-MM-dd HH:mm:ss") + "  " + logsInfo + "\n",
                true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 收集本次批处理文件的task id.
     */
    private void collectionTaskIdForSingleBatch(String taskId) {
        BatchJobListener.taskIdSet.add(taskId);
    }

    private void createMailRecipientActionDocument(WebpowerReportDto item,
        MailRecipientActionDocument mailRecipientActionDocumentEs) {
        mailRecipientActionDocumentEs.setActionDate(item.getDmdLogDate());
        mailRecipientActionDocumentEs.setAddress(item.getDmdIpAddress());
        mailRecipientActionDocumentEs.setDescription(item.getDmdBounceMessage() + item.getDmdClickUrl());
        mailRecipientActionDocumentEs.setEmail(item.getEmail());
        mailRecipientActionDocumentEs.setIp(validateIp(item.getDmdIpAddress()) ? item.getDmdIpAddress() : "0.0.0.0");
        mailRecipientActionDocumentEs.setOs(item.getDmdClient());
        mailRecipientActionDocumentEs.setTaskName(item.getDmdCampaignName());
        mailRecipientActionDocumentEs.setTaskId(item.getDmdMailingName());
    }

    private boolean validateIp(String ipStr) {
        String ipRegex =
            "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
        Pattern ipPattern = Pattern.compile(ipRegex);
        Matcher matcher = ipPattern.matcher(ipStr);
        return matcher.matches();
    }
}

