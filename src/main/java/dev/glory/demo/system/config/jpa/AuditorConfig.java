package dev.glory.demo.system.config.jpa;


import static dev.glory.demo.common.constant.WebAppConst.getMdcAccessId;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

/**
 * AuditorProvider
 */
@Configuration
public class AuditorConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of(getMdcAccessId().orElse("unknown"));
    }
}
