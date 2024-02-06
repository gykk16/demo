package dev.glory.demo.common.code;

import lombok.Getter;

/**
 * 공통 성공 코드
 */
@Getter
public enum SuccessCode implements ResponseCode {

    SUCCESS(200, true, "S00000", "성공"),
    CREATED(201, true, "S00001", "생성 완료"),
    ACCEPTED(202, true, "S00002", "접수 완료"),
    ;

    private final int     statusCode;
    private final boolean success;
    private final String  code;
    private final String  message;

    SuccessCode(int statusCode, boolean success, String code, String message) {
        this.statusCode = statusCode;
        this.success = success;
        this.code = code;
        this.message = message;
    }
}
