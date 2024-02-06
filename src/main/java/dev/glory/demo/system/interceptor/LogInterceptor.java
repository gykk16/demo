package dev.glory.demo.system.interceptor;

import static dev.glory.demo.common.util.HttpServletUtil.getRequestBody;
import static dev.glory.demo.common.util.HttpServletUtil.getRequestParams;
import static dev.glory.demo.common.util.LogLine.LOG_LINE;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 요청 정보 로깅 인터셉터
 * <p>
 * 모든 요청에 대해 요청 파라미터와 요청 바디를 로깅 한다
 */
public class LogInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        logRequestParameters();
        logRequestBody();
        log.info(LOG_LINE);

        return true;
    }

    private void logRequestParameters() {
        String requestParameters = getRequestParams();
        if (StringUtils.hasText(requestParameters)) {
            log.info("# Request Parameters = {}", requestParameters);
        }
    }

    private void logRequestBody() throws IOException {
        String requestBody = getRequestBody();
        if (StringUtils.hasText(requestBody)) {
            log.info("# Request Body = {}", requestBody);
        }
    }

}
