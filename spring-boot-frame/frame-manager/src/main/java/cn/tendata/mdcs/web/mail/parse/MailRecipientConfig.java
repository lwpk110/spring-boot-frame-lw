package cn.tendata.mdcs.web.mail.parse;

import java.util.ArrayList;
import java.util.List;

public class MailRecipientConfig {

    private static List<String> contentTypes = new ArrayList<>(3);

    static {
        contentTypes.add("application/vnd.ms-excel");
        contentTypes.add("application/octet-stream");
        contentTypes.add("text/csv");
    }

    public List<String> getContentTypes() {
        return contentTypes;
    }

    private long maxUploadSize = 4194304;
    private long maxUploadRecordSize = 5000;
    private String characterEncoding = "utf-8";

    public long getMaxUploadSize() {
        return maxUploadSize;
    }

    public void setMaxUploadSize(long maxUploadSize) {
        this.maxUploadSize = maxUploadSize;
    }

    public long getMaxUploadRecordSize() {
        return maxUploadRecordSize;
    }

    public void setMaxUploadRecordSize(long maxUploadRecordSize) {
        this.maxUploadRecordSize = maxUploadRecordSize;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }
}
