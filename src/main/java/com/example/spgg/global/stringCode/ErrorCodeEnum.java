package com.example.spgg.global.stringCode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
public enum ErrorCodeEnum {

    TOKEN_INVALID(BAD_REQUEST, "유효한 토큰이 아닙니다."),
    TOKEN_EXPIRED(BAD_REQUEST, "토큰이 만료되었습니다"),
    LOGIN_FAIL(BAD_REQUEST, "로그인 실패"),
    DUPLICATE_USERNAME_EXIST(BAD_REQUEST, "중복된 사용자가 존재합니다"),
    USER_NOT_MATCH(BAD_REQUEST, "작성자만 수정,삭제가 가능합니다"),
    POST_NOT_EXIST(BAD_REQUEST, "존재하지 않는 게시글입니다"),
    COMMENT_NOT_EXIST(BAD_REQUEST, "존재하지 않는 댓글입니다"),
    FILE_INVALID(BAD_REQUEST, "유효한 파일이 아닙니다"),
    FILE_DECODE_FAIL(BAD_REQUEST, "파일 이름 디코딩에 실패했습니다"),
    URL_INVALID(BAD_REQUEST, "잘못된 URL 형식입니다."),
    EXTRACT_INVALID(BAD_REQUEST, "확장자를 추출할 수 없습니다."),
    UPLOAD_FAIL(BAD_REQUEST, "유효하지 않은 요청입니다");

    private final HttpStatus status;
    private final String message;

    ErrorCodeEnum(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
