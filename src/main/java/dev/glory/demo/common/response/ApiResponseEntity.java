package dev.glory.demo.common.response;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import dev.glory.demo.common.code.ErrorCode;
import dev.glory.demo.common.code.ResponseCode;
import dev.glory.demo.common.code.SuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

/**
 * 표준 API 응답 객체
 *
 * @param <T> 응답 데이터 타입
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponseEntity<T> {

    /**
     * 성공 여부
     */
    @JsonProperty("success")
    private final boolean success;

    /**
     * 응답 코드
     */
    @JsonProperty("code")
    private final String code;

    /**
     * 응답 메시지
     */
    @JsonProperty("message")
    private final String message;

    /**
     * 응답 일시
     */
    @JsonProperty("responseDt")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final LocalDateTime responseDt;

    /**
     * data
     */
    @JsonProperty("data")
    private final T data;

    @Builder
    private ApiResponseEntity(ResponseCode code, String message, T responseData) {
        this.success = code.isSuccess();
        this.code = code.getCode();
        this.message = StringUtils.hasText(message) ? message : code.getMessage();
        this.responseDt = LocalDateTime.now();
        this.data = responseData;
    }

    public static <T> ResponseEntity<ApiResponseEntity<T>> ofSuccess() {
        return of(SuccessCode.SUCCESS);
    }

    public static <T> ResponseEntity<ApiResponseEntity<T>> ofError() {
        return of(ErrorCode.ERROR);
    }

    public static ResponseEntity<ApiResponseEntity<String>> ofException(Exception e) {
        return of(ErrorCode.ERROR, e.getClass().getSimpleName(), e.getMessage());
    }

    public static <T> ResponseEntity<ApiResponseEntity<T>> of(ResponseCode code) {
        return of(code, null);
    }

    public static <T> ResponseEntity<ApiResponseEntity<T>> of(ResponseCode code, T responseData) {
        return of(code, code.getMessage(), responseData);
    }

    public static <T> ResponseEntity<ApiResponseEntity<T>> of(ResponseCode code, String message, T responseData) {
        return ResponseEntity
                .status(code.getStatusCode())
                .body(ApiResponseEntity.<T>builder()
                        .code(code)
                        .message(message)
                        .responseData(responseData)
                        .build());
    }

}
