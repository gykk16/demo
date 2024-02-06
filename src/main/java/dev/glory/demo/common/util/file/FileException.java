package dev.glory.demo.common.util.file;

import dev.glory.demo.common.code.ResponseCode;
import dev.glory.demo.common.exception.BizRuntimeException;

public class FileException extends BizRuntimeException {

    public FileException(ResponseCode code) {
        super(code);
    }

    public FileException(ResponseCode code, Throwable cause) {
        super(code, cause);
    }

    public FileException(ResponseCode code, String errorMessage, Throwable cause) {
        super(code, errorMessage, cause);
    }

}
