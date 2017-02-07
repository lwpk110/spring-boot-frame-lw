package cn.tendata.ftp.webpower.core.retry;

import org.springframework.retry.RetryException;

/**
 * Created by ernest on 2016/11/1.
 */
public class BatchRetryException extends RetryException {

    public BatchRetryException(String msg) {
        super(msg);
    }

    public BatchRetryException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
