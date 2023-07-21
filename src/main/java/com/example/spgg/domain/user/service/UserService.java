package com.example.spgg.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.spgg.domain.user.dto.SignupRequestDto;
import com.example.spgg.domain.user.entity.User;
import com.example.spgg.domain.user.entity.UserRoleEnum;
import com.example.spgg.domain.user.repository.UserRepository;
import com.example.spgg.global.exception.InvalidConditionException;
import com.example.spgg.global.responseDto.ApiResponse;

import java.util.Optional;

import static com.example.spgg.global.stringCode.ErrorCodeEnum.*;
import static com.example.spgg.global.stringCode.SuccessCodeEnum.*;
import static com.example.spgg.global.utils.ResponseUtils.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입을 처리하는 메소드입니다.
     *
     * @param signupRequestDto 회원 가입 요청 DTO
     * @return 처리 결과에 대한 ApiResponse
     */
    public ApiResponse<?> signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        checkDuplicatedUsername(username);
        UserRoleEnum role = UserRoleEnum.USER;

        User user = new User(username, password, role);
        userRepository.save(user);

        return okWithMessage(USER_SIGNUP_SUCCESS);
    }

    /**
     * 중복된 회원 이름인지 확인합니다.
     *
     * @param username 회원 이름
     * @throws InvalidConditionException 중복된 회원 이름이 있을 경우 발생하는 예외
     */
    private void checkDuplicatedUsername(String username) {
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new InvalidConditionException(DUPLICATE_USERNAME_EXIST);
        }
    }
}
