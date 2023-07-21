package com.example.spgg.global.responseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResponse<T>{

    private boolean success;
    private T info;
    private ErrorResponse error;

    /**
     * ApiResponse 객체를 생성합니다.
     *
     * @param success 성공 여부
     * @param info    정보 객체
     * @param error   에러 응답 객체
     */
    public ApiResponse(boolean success, T info, ErrorResponse error) {
        this.success = success;
        this.info = info;
        this.error = error;
    }
}
