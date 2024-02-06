package dev.glory.demo.common.util;

import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import dev.glory.demo.common.code.ResponseCode;

@Slf4j
public class ErrorLogPrinter {

    private ErrorLogPrinter() {
    }

    /**
     * error 로그 출력
     *
     * @param request request
     * @param code    BaseResponseCode
     * @param e       Exception
     */
    public static void logError(HttpServletRequest request, ResponseCode code, Exception e) {
        logError(request, code, e, true);
    }

    /**
     * error 로그 출력
     *
     * @param request    request
     * @param code       BaseResponseCode
     * @param e          Exception
     * @param printTrace stack trace 출력 여부
     */
    public static void logError(HttpServletRequest request, ResponseCode code, Exception e, boolean printTrace) {

        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        Throwable rootCause = getRootCause(e);

        log.info(LogLine.logTitle("ERROR LOG"));

        log.info("# ==> RequestURI = {} , {}", method, requestURI);
        log.info("# ==> ServerIp = {} , ClientIp = {}", IpAddrUtil.getServerIP(), IpAddrUtil.getClientIP());
        log.info("# ==> httpStatus = {} , responseCode = {} - {}",
                code.getStatusCode(), code.getCode(), code.getMessage());
        log.error("# ==> exception = {} , {}", e.getClass().getSimpleName(), e.getMessage());
        log.error("# ==> rootCause = {} , {}", rootCause.getClass().getSimpleName(), rootCause.getMessage());
        if (printTrace) {
            log.error("# ==> cause - ", e);
        }

        log.info(LogLine.LOG_LINE);
    }

    private static Throwable getRootCause(Throwable throwable) {
        Objects.requireNonNull(throwable);
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

}
