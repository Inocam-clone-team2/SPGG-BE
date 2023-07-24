package team2.spgg.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team2.spgg.domain.user.dto.SignupRequestDto;
import team2.spgg.domain.user.entity.User;
import team2.spgg.domain.user.entity.UserRoleEnum;
import team2.spgg.domain.user.repository.UserRepository;
import team2.spgg.global.exception.InvalidConditionException;
import team2.spgg.global.responseDto.ApiResponse;
import team2.spgg.global.stringCode.ErrorCodeEnum;
import team2.spgg.global.stringCode.SuccessCodeEnum;
import team2.spgg.global.utils.ResponseUtils;

import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse<?> signup(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        String nickname = signupRequestDto.getNickname();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        checkDuplicatedEmail(email);
        UserRoleEnum role = UserRoleEnum.USER;

        User user = new User(email, nickname, password, role);
        userRepository.save(user);

        log.info("'{}' 이메일을 가진 사용자가 가입했습니다.", email);

        return ResponseUtils.okWithMessage(SuccessCodeEnum.USER_SIGNUP_SUCCESS);
    }


    private void checkDuplicatedEmail(String email) {
        Optional<User> found = userRepository.findByEmail(email);
        if (found.isPresent()) {
            throw new InvalidConditionException(ErrorCodeEnum.DUPLICATE_USERNAME_EXIST);
        }
    }
}
