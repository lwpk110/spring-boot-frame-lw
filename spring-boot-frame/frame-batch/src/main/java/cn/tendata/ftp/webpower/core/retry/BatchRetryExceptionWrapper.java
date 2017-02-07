package cn.tendata.ftp.webpower.core.retry;

import cn.tendata.ftp.webpower.core.AbstractBatchExceptionWrapper;

/**
 * Created by ernest on 2016/11/4.
 */
public class BatchRetryExceptionWrapper extends AbstractBatchExceptionWrapper {

    public static final long TIME_OUT = 5000L;
    public static final int MAX_ATTEMPTS = 2;

    public BatchRetryExceptionWrapper(Class<? extends Throwable> exception, boolean retryAble) {
        super(exception, retryAble);
    }
}
