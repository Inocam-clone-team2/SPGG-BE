package com.example.spgg.global.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.spgg.global.responseDto.ApiResponse;
import com.example.spgg.global.responseDto.ErrorResponse;
import com.example.spgg.global.stringCode.ErrorCodeEnum;
import com.example.spgg.global.stringCode.SuccessCodeEnum;

@Getter
@NoArgsConstructor
public class ResponseUtils {

    public static <T> ApiResponse<T> ok(T response) {
        return new ApiResponse<>(true, response, null);
    }

    public static ApiResponse<?> okWithMessage(SuccessCodeEnum successCodeEnum) {
        return new ApiResponse<>(true, successCodeEnum.getMessage(), null);
    }
    public static ApiResponse<?> error(String message, int status) {
        return new ApiResponse<>(false, null, new ErrorResponse(message, status));
    }

    public static ApiResponse<?> customError(ErrorCodeEnum errorCodeEnum) {
        return new ApiResponse<>(false, null, new ErrorResponse(errorCodeEnum));
    }
}
