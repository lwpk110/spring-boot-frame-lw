package cn.tendata.mdcs.web.mail.parse;

import cn.tendata.mdcs.core.BasicErrorCodeException;

public class MaxInputContentSizeExceededException extends BasicErrorCodeException {

    private static final long serialVersionUID = 1L;

    private static final String MAX_INPUT_CONTENT_SIZE = "MAX_INPUT_CONTENT_SIZE";

    public MaxInputContentSizeExceededException() {
        super(MAX_INPUT_CONTENT_SIZE);
    }

    public MaxInputContentSizeExceededException(Object[] args){
        super(MAX_INPUT_CONTENT_SIZE, args);
    }
}
