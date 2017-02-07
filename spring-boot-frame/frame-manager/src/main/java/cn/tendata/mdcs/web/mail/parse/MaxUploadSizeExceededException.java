package cn.tendata.mdcs.web.mail.parse;

import cn.tendata.mdcs.core.BasicErrorCodeException;

public class MaxUploadSizeExceededException extends BasicErrorCodeException {

    private static final long serialVersionUID = 1L;

    private static final String MAX_UPLOAD_SIZE_EXCEEDED = "MAX_UPLOAD_SIZE_EXCEEDED";

    public MaxUploadSizeExceededException() {
        super(MAX_UPLOAD_SIZE_EXCEEDED);
    }
    
    public MaxUploadSizeExceededException(Object[] args){
        super(MAX_UPLOAD_SIZE_EXCEEDED, args);
    }
}
