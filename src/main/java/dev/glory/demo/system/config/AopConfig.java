package dev.glory.demo.system.config;

import dev.glory.demo.system.aop.logtrace.LogTrace;
import dev.glory.demo.system.aop.logtrace.LogTraceAspect;
import dev.glory.demo.system.aop.logtrace.ThreadLocalLogTrace;
import dev.glory.demo.system.aop.security.SecuredIpAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AopConfig {

    @Bean
    public SecuredIpAspect securedIpAspect() {
        return new SecuredIpAspect();
    }

    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {
        return new LogTraceAspect(logTrace);
    }

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }

}
