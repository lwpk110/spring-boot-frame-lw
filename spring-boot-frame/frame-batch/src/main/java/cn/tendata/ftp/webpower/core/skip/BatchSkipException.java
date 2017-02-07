package cn.tendata.ftp.webpower.core.skip;

import org.springframework.batch.core.step.skip.SkipException;

/**
 * Created by ernest on 2016/11/1.
 */
public class BatchSkipException extends SkipException {

    public BatchSkipException(String msg) {
        super(msg);
    }

    public BatchSkipException(String msg, Throwable nested) {
        super(msg, nested);
    }
}
