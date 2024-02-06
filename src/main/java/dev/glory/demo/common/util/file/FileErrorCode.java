package dev.glory.demo.common.util.file;

import lombok.Getter;

import dev.glory.demo.common.code.ResponseCode;
import org.springframework.http.HttpStatus;

@Getter
public enum FileErrorCode implements ResponseCode {
    // 파일 오류
    FILE_ERROR("FIL001", "파일 오류 입니다."),
    FILE_SAVE_ERROR("FIL002", "파일 저장 오류 입니다."),
    FILE_READ_ERROR("FIL003", "파일 읽기 오류 입니다."),
    NO_FILE_ERROR("FIL104", "업로드할 파일이 없습니다."),
    FILE_FORMAT_ERROR("FIL005", "파일 형식 오류 입니다."),
    //
    DUPLICATE_ERROR("FIL201", "중복된 데이터가 있습니다."),
    DUPLICATE_IN_FILE_ERROR("FIL202", "파일에 중복된 데이터가 있습니다."),
    DUPLICATE_IN_DB_ERROR("FIL203", "DB에 중복된 데이터가 있습니다."),
    ;

    private final String code;
    private final String message;

    FileErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_ACCEPTABLE.value();
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

}
