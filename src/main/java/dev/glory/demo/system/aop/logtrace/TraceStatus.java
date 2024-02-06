package dev.glory.demo.system.aop.logtrace;

public record TraceStatus(
        TraceId traceId,
        Long startTimeMs,
        String message
) {

}
