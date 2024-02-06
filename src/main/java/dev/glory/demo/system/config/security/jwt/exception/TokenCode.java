package dev.glory.demo.system.config.security.jwt.exception;

import lombok.Getter;

import dev.glory.demo.common.code.ResponseCode;
import org.springframework.http.HttpStatus;

@Getter
public enum TokenCode implements ResponseCode {

    TOKEN_ERROR(HttpStatus.UNAUTHORIZED.value(), false, "TOK999", "인증 오류 입니다."),
    TOKEN_VALIDATION_ERROR(HttpStatus.UNAUTHORIZED.value(), false, "TOK900", "인증 정보 검증 오류 입니다."),


    NO_TOKEN(HttpStatus.UNAUTHORIZED.value(), false, "TOK001", "인증 정보가 없습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED.value(), false, "TOK002", "만료된 인증 정보 입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED.value(), false, "TOK003", "지원 하지 않은 인증 정보 입니다."),
    WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED.value(), false, "TOK004", "잘못된 타입의 인증 정보 입니다."),
    WRONG_TOKEN(HttpStatus.UNAUTHORIZED.value(), false, "TOK005", "잘못된 인증 정보 입니다."),
    AUTH_ERROR(HttpStatus.UNAUTHORIZED.value(), false, "TOK006", "인증 오류 입니다."),


    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED.value(), false, "TOK201", "refresh token 이 없습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), false, "TOK202", "만료된 인증 정보 입니다."),
    ;


    private final int     statusCode;
    private final boolean success;
    private final String  code;
    private final String  message;

    TokenCode(int statusCode, boolean success, String code, String message) {
        this.statusCode = statusCode;
        this.success = success;
        this.code = code;
        this.message = message;
    }

}
