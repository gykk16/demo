package dev.glory.demo.system.aop.logtrace;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

@Aspect
@Order(2)
public class LogTraceAspect {

    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Around("dev.glory.demo.system.aop.pointcut.TracePointcuts.logTracePointcut()")
    public Object doTimer(ProceedingJoinPoint joinPoint) throws Throwable {

        TraceStatus status = null;
        try {
            status = logTrace.begin(joinPoint.getSignature().toShortString());
            Object result = joinPoint.proceed();
            logTrace.end(status);

            return result;

        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

}
