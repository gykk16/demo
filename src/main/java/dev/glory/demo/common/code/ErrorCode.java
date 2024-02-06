package dev.glory.demo.common.code;

import lombok.Getter;

/**
 * 공통 에러 코드
 */
@Getter
public enum ErrorCode implements ResponseCode {

    // 400
    BAD_REQUEST(400, false, "ERR001", "잘못된 요청 입니다."),
    UNAUTHORIZED(401, false, "ERR002", "인증 되지 않은 사용자 입니다."),
    NOT_FOUND(404, false, "ERR003", "리소스를 찾을 수 없습니다."),
    FORBIDDEN(403, false, "ERR004", "접근 권한이 없습니다."),
    BLACK_LIST(403, false, "ERR005", "접근 권한이 없습니다. B"),
    //
    NOT_READABLE(400, false, "ERR100", "잘못된 요청 입니다."),
    INVALID_ARGUMENT(400, false, "ERR111", "파라미터 오류 입니다."),
    VALIDATION_FAILURE(400, false, "ERR112", "데이터 검증 오류 입니다."),
    // 500
    ERROR(500, false, "ERR999", "오류가 발생 했습니다."),
    SYSTEM_ERROR(500, false, "SYS999", "시스템 오류가 발생 했습니다."),
    ;

    private final int     statusCode;
    private final boolean success;
    private final String  code;
    private final String  message;

    ErrorCode(int statusCode, boolean success, String code, String message) {
        this.statusCode = statusCode;
        this.success = success;
        this.code = code;
        this.message = message;
    }

}
