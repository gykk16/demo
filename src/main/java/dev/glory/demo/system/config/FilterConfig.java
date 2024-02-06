package dev.glory.demo.system.config;

import dev.glory.demo.system.filter.ContentCachingFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    @ConditionalOnMissingBean
    public ContentCachingFilter contentCatchingFilter() {
        return new ContentCachingFilter();
    }

}
