package dev.glory.demo.common.constant;

import static dev.glory.demo.common.constant.MdcConst.ACCESS_ID;
import static dev.glory.demo.common.constant.MdcConst.REQUEST_START_TIME;
import static dev.glory.demo.common.constant.MdcConst.REQUEST_TRACE_KEY;
import static dev.glory.demo.common.constant.MdcConst.REQ_LOG_ID;

import java.util.Optional;
import java.util.Set;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.slf4j.MDC;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebAppConst {

    public static final Set<String> INTERNAL_IPS = Set.of(
            "127.0.0.1", "0:0:0:0:0:0:0:1"
    );

    // ================================================================================================================
    // MDC 상수
    // ================================================================================================================

    public static final String REQUEST_ID_HEADER = "X-Pet-Request-Id";

    public static final String[] FILTER_EXCLUDE_PATH = {
            "/css/", "/js/", "/img/", "/images/", "/error/", "/download/", "/common/file", ".ico"
    };

    public static final String[] INTERCEPTOR_EXCLUDE_PATH = {
            "/css/**", "/js/**", "/img/**", "/images/**", "/error/**", "/download/**", "/common/file**", "/*.ico"
    };

    public static Optional<String> getMdcTraceKey() {
        return Optional.ofNullable(MDC.get(REQUEST_TRACE_KEY));
    }

    public static long getMdcStartTime() {
        try {
            return Long.parseLong(MDC.get(REQUEST_START_TIME));
        } catch (Exception e) {
            return 0L;
        }
    }

    public static Optional<String> getMdcAccessId() {
        return Optional.ofNullable(MDC.get(ACCESS_ID));
    }

    public static Optional<String> getMdcReqLogId() {
        return Optional.ofNullable(MDC.get(REQ_LOG_ID));
    }

}
