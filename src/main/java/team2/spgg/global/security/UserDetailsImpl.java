package team2.spgg.global.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import team2.spgg.domain.kakao.entity.Kakao;
import team2.spgg.domain.kakao.entity.oauth.KakaoProfile;
import team2.spgg.domain.user.entity.User;
import team2.spgg.domain.user.entity.UserRoleEnum;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final User user;
    private final Kakao kakao;

    /**
     * UserDetailsImpl 객체를 생성합니다.
     *
     * @param user User 객체
     */
    public UserDetailsImpl(User user, Kakao kakao) {
        this.user = user;
        this.kakao = kakao;
    }

    /**
     * User 객체를 반환합니다.
     *
     * @return User 객체
     */
    public User getUser() {
        return user;
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }


    public String getUsername(){ return user.getEmail();
    }

    public Kakao getKakao() {
        return  kakao;
    }

    public String getKakaoEmail() {
        return kakao.getKakaoEmail();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum role = user.getRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
