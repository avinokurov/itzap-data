package com.itzap.data.file.exceptions;

import com.itzap.common.ErrorCode;

public enum FileErrorCodes implements ErrorCode {
    FILE_NOT_FOUND("itzap.file.file_not_found", "File not found"),
    FILE_CLOSE("itzap.file.file_close", "File close error"),
    FILE_READ("itzap.file.file_read", "File read error"),
    FILE_PARSE_ASSERT("itzap.file.file_parse_assert", "File parsing logic error"),

    FIELD_TYPE_CONVERSION("itzap.field.field_type_conversion",
            "Field type conversion error");


    private final String errorCode;
    private final String message;

    FileErrorCodes(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
