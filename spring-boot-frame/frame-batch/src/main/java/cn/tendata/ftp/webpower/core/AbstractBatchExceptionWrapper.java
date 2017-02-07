package cn.tendata.ftp.webpower.core;

/**
 * Created by ernest on 2016/11/4.
 */
public abstract class AbstractBatchExceptionWrapper {

    private Class<? extends Throwable> exception;
    private boolean retryAble;

    public AbstractBatchExceptionWrapper(Class<? extends Throwable> exception, boolean retryAble) {
        this.exception = exception;
        this.retryAble = retryAble;
    }

    public Class<? extends Throwable> getException() {
        return exception;
    }

    public void setException(Class<? extends Throwable> exception) {
        this.exception = exception;
    }

    public boolean isRetryAble() {
        return retryAble;
    }

}
