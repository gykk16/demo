package dev.glory.demo.system.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import dev.glory.demo.common.code.ErrorCode;
import dev.glory.demo.common.code.ResponseCode;
import dev.glory.demo.common.exception.BizException;
import dev.glory.demo.common.exception.BizRuntimeException;
import dev.glory.demo.common.response.ApiResponseEntity;
import dev.glory.demo.common.util.ErrorLogPrinter;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 어플리케이션 공통 예외 처리 핸들러
 *
 * @see BizRuntimeException
 * @see BizException
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
@RequiredArgsConstructor
public class CommonExceptionHandler {

    public static ResponseEntity<ApiResponseEntity<Object>> createApiResponseEntity(
            HttpServletRequest request, ResponseCode code, Exception e) {
        return createApiResponseEntity(request, code, e, false, false);
    }

    public static ResponseEntity<ApiResponseEntity<Object>> createApiResponseEntity(
            HttpServletRequest request, ResponseCode code, Exception e, boolean printStackTrace, boolean saveDb) {
        return createApiResponseEntity(request, code, e, printStackTrace, saveDb, null);
    }

    public static <T> ResponseEntity<ApiResponseEntity<T>> createApiResponseEntity(HttpServletRequest request,
            ResponseCode code, Exception e, boolean printStackTrace, boolean saveDb, T data) {

        ErrorLogPrinter.logError(request, code, e, printStackTrace);
        if (saveDb) {
            // todo: db 에 에러 로그 저장
        }
        return ApiResponseEntity.of(code, data);
    }

    @ExceptionHandler(BizRuntimeException.class)
    public ResponseEntity<ApiResponseEntity<Object>> bizRuntimeException(
            HttpServletRequest request, BizRuntimeException e) {
        return createApiResponseEntity(request, e.getCode(), e, e.isPrintStackTrace(), e.isSaveDb());
    }

    @ExceptionHandler(BizException.class)
    public ResponseEntity<ApiResponseEntity<Object>> bizException(HttpServletRequest request, BizRuntimeException e) {
        return createApiResponseEntity(request, e.getCode(), e, e.isPrintStackTrace(), e.isSaveDb());
    }

    @ExceptionHandler({
            NoResourceFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HandlerMethodValidationException.class,
            Exception.class
    })
    public ResponseEntity<ApiResponseEntity<Object>> handleException(HttpServletRequest request, Exception ex) {

        if (ex instanceof NoResourceFoundException subEx) {
            return handleNoResourceFoundException(request, subEx, ErrorCode.NOT_FOUND);

        } else if (ex instanceof HttpRequestMethodNotSupportedException subEx) {
            return handleHttpRequestMethodNotSupportedException(request, subEx, ErrorCode.NOT_FOUND);

        } else if (ex instanceof MethodArgumentNotValidException subEx) {
            return handleMethodArgumentNotValidException(request, subEx, ErrorCode.INVALID_ARGUMENT);

        } else if (ex instanceof MethodArgumentTypeMismatchException subEx) {
            return handleMethodArgumentTypeMismatchException(request, subEx, ErrorCode.NOT_READABLE);

        } else if (ex instanceof HttpMessageNotReadableException subEx) {
            return handleHttpMessageNotReadableException(request, subEx, ErrorCode.NOT_READABLE);

        } else if (ex instanceof HandlerMethodValidationException subEx) {
            return handleHandlerMethodValidationException(request, subEx, ErrorCode.VALIDATION_FAILURE);

        } else {
            return createApiResponseEntity(request, ErrorCode.ERROR, ex, true, true);

        }
    }

    private ResponseEntity<ApiResponseEntity<Object>> handleNoResourceFoundException(
            HttpServletRequest request, NoResourceFoundException ex, ResponseCode errorCode) {

        return createApiResponseEntity(request, errorCode, ex);
    }

    private ResponseEntity<ApiResponseEntity<Object>> handleHttpRequestMethodNotSupportedException(
            HttpServletRequest request, HttpRequestMethodNotSupportedException ex, ResponseCode errorCode) {

        return createApiResponseEntity(request, errorCode, ex);
    }

    private ResponseEntity<ApiResponseEntity<Object>> handleMethodArgumentNotValidException(
            HttpServletRequest request, MethodArgumentNotValidException ex, ResponseCode errorCode) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return createApiResponseEntity(request, errorCode, ex, false, false, errors);
    }

    private ResponseEntity<ApiResponseEntity<Object>> handleMethodArgumentTypeMismatchException(
            HttpServletRequest request, MethodArgumentTypeMismatchException ex, ResponseCode errorCode) {

        return createApiResponseEntity(request, errorCode, ex);
    }

    private ResponseEntity<ApiResponseEntity<Object>> handleHttpMessageNotReadableException(
            HttpServletRequest request, HttpMessageNotReadableException ex, ResponseCode errorCode) {

        return createApiResponseEntity(request, errorCode, ex);
    }

    private ResponseEntity<ApiResponseEntity<Object>> handleHandlerMethodValidationException(
            HttpServletRequest request, HandlerMethodValidationException ex, ResponseCode errorCode) {

        Map<String, String> errors = new HashMap<>();
        ex.getAllValidationResults().forEach(validationResult -> {
            List<MessageSourceResolvable> resolvableErrors = validationResult.getResolvableErrors();
            resolvableErrors.forEach(resolvableError -> {
                boolean hasField = resolvableError.getCodes() != null && resolvableError.getCodes().length > 1;
                String fieldName = hasField ? resolvableError.getCodes()[1] : "field";
                String errorMessage = resolvableError.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
        });

        return createApiResponseEntity(request, errorCode, ex, false, false, errors);
    }

}
