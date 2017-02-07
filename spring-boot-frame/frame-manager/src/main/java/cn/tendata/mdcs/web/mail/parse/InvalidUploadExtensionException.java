package cn.tendata.mdcs.web.mail.parse;

import cn.tendata.mdcs.core.BasicErrorCodeException;

public class InvalidUploadExtensionException extends BasicErrorCodeException {

    private static final long serialVersionUID = 1L;

    private static final String INVALID_UPLOAD_EXTENSION = "INVALID_UPLOAD_EXTENSION";

    public InvalidUploadExtensionException() {
        super(INVALID_UPLOAD_EXTENSION);
    }
}
