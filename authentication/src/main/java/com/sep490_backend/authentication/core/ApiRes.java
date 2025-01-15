package com.sep490_backend.authentication.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sep490_backend.authentication.core.enums.CommonApiCode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ApiRes<T> {
    private int code;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static <T> ApiRes<T> with(ApiCode apiCode, T data) {
        return ApiRes
                .<T>builder()
                .code(apiCode.getCode())
                .message(apiCode.getMessage())
                .data(data)
                .build();
    }

    public static <T> ApiRes<T> with(ApiCode apiCode) {
        return ApiRes
                .<T>builder()
                .code(apiCode.getCode())
                .message(apiCode.getMessage())
                .data(null)
                .build();
    }

    public static <T> ApiRes<T> success(T data) {
        return ApiRes.with(CommonApiCode.SUCCESS, data);
    }

    public static <T> ApiRes<T> error(String msg) {
        return ApiRes
                .<T>builder()
                .code(CommonApiCode.ERROR.getCode())
                .message(msg)
                .build();
    }
}
