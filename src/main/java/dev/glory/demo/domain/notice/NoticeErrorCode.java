package dev.glory.demo.domain.notice;

import lombok.Getter;

import dev.glory.demo.common.code.ResponseCode;
import org.springframework.http.HttpStatus;

@Getter
public enum NoticeErrorCode implements ResponseCode {

    NOT_FOUND("ERN101", "데이터를 찾을 수 없습니다.");

    private final boolean success;
    private final String  code;
    private final String  message;

    NoticeErrorCode(String code, String message) {
        this.success = false;
        this.code = code;
        this.message = message;
    }


    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_ACCEPTABLE.value();
    }

}
