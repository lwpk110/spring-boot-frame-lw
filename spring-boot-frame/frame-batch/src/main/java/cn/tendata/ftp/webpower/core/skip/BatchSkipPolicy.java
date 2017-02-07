package cn.tendata.ftp.webpower.core.skip;

import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;

/**
 * Created by ernest on 2016/11/3.
 */
public class BatchSkipPolicy extends LimitCheckingItemSkipPolicy {

    public BatchSkipPolicy(BatchSkipExceptionWrapper... ExceptionWrappers) {
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();

        for (BatchSkipExceptionWrapper exceptionWrapper : ExceptionWrappers) {
            retryableExceptions.put(exceptionWrapper.getException(), exceptionWrapper.isRetryAble());
        }
        this.setSkippableExceptionMap(retryableExceptions);
        this.setSkipLimit(BatchSkipExceptionWrapper.MAX_SKIP_LIMIT);
    }

    @Override
    public boolean shouldSkip(Throwable t, int skipCount) {
        return super.shouldSkip(t, skipCount);
    }
}
