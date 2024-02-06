package dev.glory.demo.system.aop.apidblog;

import static dev.glory.demo.common.util.StringUtil.substringN;

import java.io.IOException;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

import dev.glory.demo.common.code.ErrorCode;
import dev.glory.demo.common.exception.BizException;
import dev.glory.demo.common.exception.BizRuntimeException;
import dev.glory.demo.common.response.ApiResponseEntity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * API 요청/응답 로그를 DB 에 저장하는 Aspect
 * <ul>
 *     <li>API 요청/응답 로그를 DB 에 저장하는 메소드에 {@link ApiDbLog} 어노테이션을 추가 한다 </li>
 * </ul>
 */
@Slf4j
public abstract class ApiDbLogAspect {

    /**
     * API 요청/응답 로그를 DB 에 저장한다
     * <p>
     * 어플리케이션 레이어에서 저장 로직을 구현 한다
     * </p>
     */
    protected abstract void buildAndSaveApiReqResLog(ProceedingJoinPoint joinPoint, ApiDbLog apiDbLog, boolean success,
            int httpStatus, String resCode, String resMsg, String exception, String rootCause) throws IOException;

    protected void saveApiReqResLog(ProceedingJoinPoint joinPoint, ApiDbLog apiDbLog, Object proceed) {
        try {
            String resCode = null;
            String resMsg = null;
            int httpStatus = HttpStatus.OK.value();

            if (proceed instanceof ResponseEntity<?> resEntity) {
                httpStatus = resEntity.getStatusCode().value();

                if (resEntity.getBody() instanceof ApiResponseEntity<?> apiResponseEntity) {
                    resCode = apiResponseEntity.getCode();
                    resMsg = apiResponseEntity.getMessage();
                }
            }

            buildAndSaveApiReqResLog(joinPoint, apiDbLog, true, httpStatus, resCode, resMsg, null, null);

        } catch (Exception e) {
            log.error("# ==> Error in logging API request: ", e);
        }
    }

    protected void saveExceptionApiReqResLog(ProceedingJoinPoint joinPoint, ApiDbLog apiDbLog, Exception e) {
        try {
            String resCode;
            String resMsg;
            int httpStatus;

            if (e instanceof BizRuntimeException bre) {
                resCode = bre.getCode().getCode();
                resMsg = bre.getErrorMessage();
                httpStatus = bre.getCode().getStatusCode();

            } else if (e instanceof BizException be) {
                resCode = be.getCode().getCode();
                resMsg = be.getErrorMessage();
                httpStatus = be.getCode().getStatusCode();

            } else {
                resCode = ErrorCode.ERROR.getCode();
                resMsg = e.getMessage();
                httpStatus = ErrorCode.ERROR.getStatusCode();
            }

            String exception = e.getClass().getName();
            String rootCause = substringN(getRootCause(e).toString(), 255);

            buildAndSaveApiReqResLog(joinPoint, apiDbLog, false, httpStatus, resCode, resMsg, exception, rootCause);

        } catch (Exception ex) {
            log.error("# ==> Error updating API response log: ", ex);
        }
    }

    protected Throwable getRootCause(Throwable throwable) {
        Objects.requireNonNull(throwable);
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

}
