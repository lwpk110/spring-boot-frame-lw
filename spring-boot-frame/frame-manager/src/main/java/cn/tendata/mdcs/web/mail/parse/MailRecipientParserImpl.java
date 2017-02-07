package cn.tendata.mdcs.web.mail.parse;

import cn.tendata.mdcs.util.JsonUtils;
import cn.tendata.mdcs.web.model.MailAnatomy;
import cn.tendata.mdcs.web.util.CharsetDetUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MailRecipientParserImpl implements MailRecipientParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailRecipientParserImpl.class);

    private MailRecipientConfig mailRecipientConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${filter.mailRecipient-uri}")
    private String filterApi;

    public MailRecipientParserImpl(MailRecipientConfig mailRecipientConfig) {
        this.mailRecipientConfig = mailRecipientConfig;
    }

    @Override
    public MailRecipientCollectionEx parse(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (!this.mailRecipientConfig.getContentTypes().contains(contentType)) {
            LOGGER.error("ContentType:[" + contentType + "] is not support!");
            throw new InvalidUploadExtensionException();
        }
        if (file.getBytes().length > this.mailRecipientConfig.getMaxUploadSize())
            throw new MaxUploadSizeExceededException(new Object[]{this.mailRecipientConfig.getMaxUploadSize()});
        if (file.getBytes().length == 0) {
            throw new EmptyUploadRecordSizeException();
        }
        Charset detectCharset = CharsetDetUtils.detect(new BufferedInputStream(file.getInputStream()), file.getBytes().length);
        String characterEncoding = "void".equals(detectCharset.name()) ? this.mailRecipientConfig.getCharacterEncoding() : detectCharset.name();
        InputStreamReader reader = new InputStreamReader(file.getInputStream(), characterEncoding);
        List<CSVRecord> records = CSVFormat.EXCEL.parse(reader).getRecords();

        if (records.size() > this.mailRecipientConfig.getMaxUploadRecordSize())
            throw new MaxUploadRecordSizeExceededException(new Object[]{this.mailRecipientConfig.getMaxUploadRecordSize()});

        MailRecipientResult result = new MailRecipientResult();
        for (CSVRecord record : records) {
            String fullname = null;
            long recordNumber = record.getRecordNumber();
            String email = record.get(0);
            if (record.size() > 1) {
                fullname = record.get(1);
            }
            MailRecipientRecord recipient = new MailRecipientRecord(recordNumber, fullname, email);
            result.add(recipient);
        }

        return mailFilterParse(result);
    }

    @Override
    public MailRecipientCollectionEx parse(String content) {
        if (!StringUtils.isNotEmpty(content)) {
            throw new EmptyInputContentException();
        }

        String[] array = content.split("\n");
        if (array.length > this.mailRecipientConfig.getMaxUploadRecordSize()) {
            throw new MaxInputContentSizeExceededException(new Object[]{this.mailRecipientConfig.getMaxUploadRecordSize()});
        }
        MailRecipientResult result = new MailRecipientResult();
        for (int i = 0; i < array.length; i++) {
            MailRecipientRecord recipient = new MailRecipientRecord(i + 1, null, array[i].trim());
            result.add(recipient);
        }
        return mailFilterParse(result);
    }

    public MailRecipientCollectionEx mailFilterParse(MailRecipientResult result) {
        if (null != result) {
            Set<MailRecipientRecord> records = result.getValidRecords();
            String mailRecipients = JsonUtils.serialize(records);
            String filterResult = null;
            try {
                filterResult = restTemplate.postForObject(filterApi, MailRecipientRecord.class, String.class, mailRecipients);
                LOGGER.info("mailFilterParse::filterApi url【" + filterApi+ "】filterResult 【" + filterResult + "】");
            } catch (Exception ex) {
                LOGGER.error("mailFilterParse::url【" + filterApi + "】 error");
            }
            if(StringUtils.isNotEmpty(filterResult)){
               JSONObject jsonObject = JSON.parseObject(filterResult);
                if(jsonObject.containsKey("recipients")) {
                    String strResult = jsonObject.getString("recipients");
                    if(StringUtils.isNotEmpty(strResult)) {
                        MailAnatomy mailAnatomy = JsonUtils.deserialize(strResult, MailAnatomy.class);
                        if (null != mailAnatomy) {
                            List<MailRecipientRecord> availableRecords = mailAnatomy.mailRecipientAvailable;
                            if (null != availableRecords) {
                                Collections.sort(mailAnatomy.getMailRecipientDisable());
                                result.setNotExistRecords(mailAnatomy.getMailRecipientDisable());
                                result.setValidRecords(new HashSet(availableRecords));
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
