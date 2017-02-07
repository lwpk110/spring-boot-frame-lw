package cn.tendata.mdcs.web.mail.parse;

import cn.tendata.mdcs.core.BasicErrorCodeException;

public class MaxUploadRecordSizeExceededException extends BasicErrorCodeException {

    private static final long serialVersionUID = 1L;

    private static final String MAX_UPLOAD_RECORD_SIZE_EXCEEDED = "MAX_UPLOAD_RECORD_SIZE_EXCEEDED";

    public MaxUploadRecordSizeExceededException() {
        super(MAX_UPLOAD_RECORD_SIZE_EXCEEDED);
    }
    
    public MaxUploadRecordSizeExceededException(Object[] args){
        super(MAX_UPLOAD_RECORD_SIZE_EXCEEDED, args);
    }
}
