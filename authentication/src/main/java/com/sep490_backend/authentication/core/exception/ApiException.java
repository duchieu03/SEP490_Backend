package com.sep490_backend.authentication.core.exception;

import com.sep490_backend.authentication.core.ApiCode;
import lombok.Data;

@Data
public class ApiException extends RuntimeException{
    private ApiCode errorCode;

    public ApiException(ApiCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
