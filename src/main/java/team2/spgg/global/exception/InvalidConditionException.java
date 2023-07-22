package team2.spgg.global.exception;

import team2.spgg.global.stringCode.ErrorCodeEnum;

public class InvalidConditionException extends IllegalArgumentException{

    ErrorCodeEnum errorCodeEnum;

    public InvalidConditionException(ErrorCodeEnum errorCodeEnum) {
        this.errorCodeEnum = errorCodeEnum;
    }
}
