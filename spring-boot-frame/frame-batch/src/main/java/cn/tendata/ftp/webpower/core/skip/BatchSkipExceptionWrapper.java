package cn.tendata.ftp.webpower.core.skip;

import cn.tendata.ftp.webpower.core.AbstractBatchExceptionWrapper;

/**
 * Created by ernest on 2016/11/4.
 */
public class BatchSkipExceptionWrapper extends AbstractBatchExceptionWrapper {
    public static final int MAX_SKIP_LIMIT = 30;

    public BatchSkipExceptionWrapper(Class<? extends Throwable> exception, boolean retryAble) {
        super(exception, retryAble);
    }
}
