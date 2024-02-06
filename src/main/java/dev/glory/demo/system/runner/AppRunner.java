package dev.glory.demo.system.runner;

import lombok.extern.slf4j.Slf4j;

import dev.glory.demo.api.auth.dto.RegisterRequest;
import dev.glory.demo.common.util.LogLine;
import dev.glory.demo.domain.auth.service.AuthService;
import dev.glory.demo.domain.auth.service.dto.AuthServiceResponse;
import dev.glory.demo.domain.user.role.Role;
import dev.glory.demo.system.properties.AppProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Slf4j
public class AppRunner {

    @Value("${info.app.version:}")
    private String appVersion;

    @Bean
    CommandLineRunner run(AppProperties appProperties) {
        return args -> {
            log.info(LogLine.LOG_LINE);
            log.info("# ==> App Name = {}", appProperties.name());
            log.info("# ==> App Version = {}", appVersion);
            log.info(LogLine.LOG_LINE);
        };
    }

    @Bean
    @Profile("local")
    CommandLineRunner runLocal(AuthService authService) {
        return args -> {

            RegisterRequest userManager =
                    new RegisterRequest("manager", "password", "manager", "manager@demo.com", Role.MANAGER);
            RegisterRequest userAdmin =
                    new RegisterRequest("admin", "password", "admin", "admin@demo.com", Role.ADMIN);

            AuthServiceResponse managerAuth = authService.register(userManager.toServiceRequest());
            AuthServiceResponse adminAuth = authService.register(userAdmin.toServiceRequest());

            log.info("==> adminAuth.getAccessToken() = {}", adminAuth.accessToken());
            log.info("==> managerAuth.getAccessToken() = {}", managerAuth.accessToken());
        };
    }

}
