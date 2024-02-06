package dev.glory.demo.system.aop.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class TracePointcuts {

    @Pointcut("@annotation(dev.glory.demo.common.annotation.ExcludeLogTrace)")
    public void excludeLogTraceAnnotation() {
    }

    @Pointcut("@annotation(dev.glory.demo.common.annotation.LogTraceInfo)")
    public void logTraceAnnotation() {
    }

    @Pointcut("execution(* dev.glory.demo..*Controller.*(..))")
    public void allController() {
    }

    @Pointcut("execution(* dev.glory.demo..*Service.*(..))")
    public void allService() {
    }

    @Pointcut("execution(* dev.glory.demo..*Repository.*(..))")
    public void allRepository() {
    }

    @Pointcut("(allController() || allService() || allRepository() || logTraceAnnotation()) && !excludeLogTraceAnnotation()")
    public void logTracePointcut() {
    }

}
