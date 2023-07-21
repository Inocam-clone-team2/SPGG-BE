package com.example.spgg.global.exception;

import com.example.spgg.global.stringCode.ErrorCodeEnum;

public class InvalidConditionException extends IllegalArgumentException{

    ErrorCodeEnum errorCodeEnum;

    public InvalidConditionException(ErrorCodeEnum errorCodeEnum) {
        this.errorCodeEnum = errorCodeEnum;
    }
}
