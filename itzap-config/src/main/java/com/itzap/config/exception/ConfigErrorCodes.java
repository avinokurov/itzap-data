package com.itzap.config.exception;

import com.itzap.common.ErrorCode;

public enum ConfigErrorCodes implements ErrorCode {
    CONFIG_NOT_FOUND("itzap.config.file_not_found", "File not found"),
    CONFIG_CLOSE("itzap.config.config_close", "File close error"),
    INI_SECTION_NOT_FOUND("itzap.config.ini_section_not_found", "INI section not found");


    private final String errorCode;
    private final String message;

    ConfigErrorCodes(String errorCode, String message) {
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
