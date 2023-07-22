package team2.spgg.domain.user.service;

import lombok.RequiredArgsConstructor;
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
        String email = signupRequestDto.getEmail();
        String nickname = signupRequestDto.getNickname();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        checkDuplicatedEmail(email);
        UserRoleEnum role = UserRoleEnum.USER;

        User user = new User(email, nickname, password, role);
        userRepository.save(user);

        return ResponseUtils.okWithMessage(SuccessCodeEnum.USER_SIGNUP_SUCCESS);
    }

    /**
     * 중복된 회원 이름인지 확인합니다.
     *
     * @param email 회원 이름
     * @throws InvalidConditionException 중복된 회원 이름이 있을 경우 발생하는 예외
     */
    private void checkDuplicatedEmail(String email) {
        Optional<User> found = userRepository.findByEmail(email);
        if (found.isPresent()) {
            throw new InvalidConditionException(ErrorCodeEnum.DUPLICATE_USERNAME_EXIST);
        }
    }
}
