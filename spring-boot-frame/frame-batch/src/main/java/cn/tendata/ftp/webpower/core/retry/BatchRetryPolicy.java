package cn.tendata.ftp.webpower.core.retry;

import java.util.HashMap;
import java.util.Map;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.CompositeRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;

/**
 * Created by ernest on 2016/9/6.
 */
public class BatchRetryPolicy extends CompositeRetryPolicy {

    public BatchRetryPolicy(BatchRetryExceptionWrapper... throwables) {
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
        for (BatchRetryExceptionWrapper exceptionWrapper : throwables) {
            retryableExceptions.put(exceptionWrapper.getException(), exceptionWrapper.isRetryAble());
        }

        SimpleRetryPolicy simpleRetryPolicy =
                new SimpleRetryPolicy(BatchRetryExceptionWrapper.MAX_ATTEMPTS, retryableExceptions);
        TimeoutRetryPolicy timeoutRetryPolicy = new TimeoutRetryPolicy();
        timeoutRetryPolicy.setTimeout(BatchRetryExceptionWrapper.TIME_OUT);
        super.setPolicies(new RetryPolicy[] {simpleRetryPolicy, timeoutRetryPolicy});
    }
}
