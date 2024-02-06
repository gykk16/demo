package dev.glory.demo.system.interceptor;

import static dev.glory.demo.common.util.HttpServletUtil.getResponseBody;
import static dev.glory.demo.common.util.LogLine.LOG_LINE;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 응답 바디 로깅 인터셉터
 * <p>
 * {@link LogResponseBody} 어노테이션이 붙은 핸들러 메소드의 응답 바디를 로깅 한다
 *
 * @see LogResponseBody
 */
public class LogResponseBodyInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LogResponseBodyInterceptor.class);

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return; // fast exit
        }

        LogResponseBody annotation = handlerMethod.getMethodAnnotation(LogResponseBody.class);

        if (annotation == null) {
            return; // fast exit
        }

        logResponseBody(annotation);
    }

    private static void logResponseBody(LogResponseBody annotation) {
        int maxLength = annotation.maxLength();
        String responseBody = getResponseBody().length() > maxLength
                ? getResponseBody().substring(0, maxLength) + "\n...more..."
                : getResponseBody();

        log.info(LOG_LINE);
        log.info("# Response Body = {}", responseBody);
        log.info(LOG_LINE);
    }

}
