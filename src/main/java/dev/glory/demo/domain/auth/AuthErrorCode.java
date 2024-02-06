package dev.glory.demo.domain.auth;

import lombok.Getter;

import dev.glory.demo.common.code.ResponseCode;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthErrorCode implements ResponseCode {

    AUTH_ERROR(HttpStatus.NOT_ACCEPTABLE.value(), false, "ERA999", "인증 오류가 발생했습니다."),
    BAD_CREDENTIALS(HttpStatus.NOT_ACCEPTABLE.value(), false, "ERA001", "잘못된 인증 정보입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_ACCEPTABLE.value(), false, "ERA002", "사용자를 찾을 수 없습니다."),
    EXISTING_USER(HttpStatus.NOT_ACCEPTABLE.value(), false, "ERA101", "사용 불가능한 username 입니다."),
    //
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), false, "ERA201", "접근 권한이 없습니다."),
    ;

    private final int     statusCode;
    private final boolean success;
    private final String  code;
    private final String  message;

    AuthErrorCode(int statusCode, boolean success, String code, String message) {
        this.statusCode = statusCode;
        this.success = success;
        this.code = code;
        this.message = message;
    }

}
