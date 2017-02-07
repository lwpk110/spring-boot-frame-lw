package cn.tendata.mdcs.web.mail.parse;

import cn.tendata.mdcs.core.BasicErrorCodeException;

public class EmptyUploadRecordSizeException extends BasicErrorCodeException {

    private static final long serialVersionUID = 1L;

    private static final String EMPTY_UPLOAD_RECORD_SIZE = "EMPTY_UPLOAD_RECORD_SIZE";

    public EmptyUploadRecordSizeException() {
        super(EMPTY_UPLOAD_RECORD_SIZE);
    }
}
