package dev.glory.demo.system.config;

import dev.glory.demo.common.util.idgenerator.TraceIdGenerator;
import dev.glory.demo.system.properties.WebAppProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdGeneratorConfig {

    @Bean
    public TraceIdGenerator traceIdGenerator(WebAppProperties properties) {
        return new TraceIdGenerator(properties.workerId(), properties.processId());
    }

}
