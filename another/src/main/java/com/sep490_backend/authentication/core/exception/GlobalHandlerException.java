package com.sep490_backend.authentication.core.exception;

import com.sep490_backend.authentication.core.ApiRes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler(value = {ApiException.class})
    public ResponseEntity<ApiRes<String>> apiException(ApiException apiException){
        String error = apiException.getErrorCode().getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiRes.error(error));
    }
}
