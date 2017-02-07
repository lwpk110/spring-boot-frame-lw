package cn.tendata.mdcs.web.mail.parse;

import cn.tendata.mdcs.core.BasicErrorCodeException;

public class EmptyInputContentException extends BasicErrorCodeException {

    private static final long serialVersionUID = 1L;

    private static final String EMPTY_INPUT_CONTENT = "EMPTY_INPUT_CONTENT";

    public EmptyInputContentException() {
        super(EMPTY_INPUT_CONTENT);
    }
}
