package team2.spgg.domain.user.controller;

import team2.spgg.global.responseDto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team2.spgg.domain.user.dto.SignupRequestDto;
import team2.spgg.domain.user.service.UserService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원 가입을 처리하는 메소드입니다.
     *
     * @param signupRequestDto 회원 가입 요청 DTO
     * @return 처리 결과에 대한 ApiResponse
     */

    @PostMapping("/signup")
    public ApiResponse<?> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }
}
