package team2.spgg.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import team2.spgg.domain.user.entity.User;
import team2.spgg.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 사용자의 세부 정보를 로드합니다.
     *
     * @param username 사용자 이름
     * @return UserDetails 객체
     * @throws UsernameNotFoundException 주어진 사용자 이름이 존재하지 않을 경우 예외가 발생합니다.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 " + username + "은 존재하지 않습니다"));

        return new UserDetailsImpl(user);
    }
}
