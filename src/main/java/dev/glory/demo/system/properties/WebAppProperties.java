package dev.glory.demo.system.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Web Application 환경 설정
 *
 * @param workerId  서버 고유 번호 (0 ~ 31)
 * @param processId 실행 어플리케이션 고유 번호 (0 ~ 31)
 */
@ConfigurationProperties(prefix = "webapp")
public record WebAppProperties(
        Integer workerId,
        Integer processId
) {

}
