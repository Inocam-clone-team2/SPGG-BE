package team2.spgg.domain.kakao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import team2.spgg.domain.kakao.dto.KakaoTokenInfo;
import team2.spgg.domain.kakao.entity.User;
import team2.spgg.domain.kakao.entity.oauth.KakaoProfile;
import team2.spgg.domain.kakao.entity.oauth.OauthToken;
import team2.spgg.domain.kakao.kakaojwt.JwtProperties;
import team2.spgg.domain.kakao.repository.UserRepository;
import java.util.Date;

@Service
public class UserService {

    private final UserRepository userRepository; //(1)
    private final RestTemplate restTemplate;

    private final String clientId = "9b3f5d5d410faff975879a54b0c9f165";

    public UserService(UserRepository userRepository, RestTemplateBuilder restTemplateBuilder) {
        this.userRepository =userRepository;
        this.restTemplate = restTemplateBuilder.build();
    }

    public String saveUserAndGetToken(String token) { //(1)
        KakaoProfile profile = findProfile(token);

        User user = userRepository.findByKakaoEmail(profile.getKakao_account().getEmail());
        if(user == null) {
            user = User.builder()
                    .kakaoId(profile.getId())
                    .kakaoProfileImg(profile.getKakao_account().getProfile().getProfile_image_url())
                    .kakaoNickname(profile.getKakao_account().getProfile().getNickname())
                    .kakaoEmail(profile.getKakao_account().getEmail())
                    .userRole("ROLE_USER").build();

            userRepository.save(user);
        }

        return createToken(user); //(2)
    }

    public void findKakaoId(String token){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token); //(1-4)
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers); // 바디, 헤더 바디 타입은 T타입 헤더는 무조건 HttpHeaders 타입

        ResponseEntity<KakaoTokenInfo> kakaoProfileResponse = restTemplate.exchange(
                "https://kapi.kakao.com/v1/user/access_token_info", // 전송할 uri
                HttpMethod.POST, // 전송 방식
                kakaoProfileRequest, // 전송할 데이터
                KakaoTokenInfo.class // 반환할 클래스
        );

        if(userRepository.findByKakaoId(kakaoProfileResponse.getBody().getId()).isPresent())
            throw new IllegalArgumentException("이미 회원가입 한 아이디입니다.");
    }

    public String createToken(User user) { //(2-1)

        //(2-2)
        String jwtToken =
                Jwts.builder()

                //(2-3)
                .setSubject(user.getKakaoEmail())
                .setExpiration(new Date(System.currentTimeMillis()+ JwtProperties.EXPIRATION_TIME))

                //(2-4)
                .claim("id", user.getUserCode())
                .claim("nickname", user.getKakaoNickname())

                //(2-5)
                .signWith(JwtProperties.SECRET, SignatureAlgorithm.HS25)
                        .compact();
                //.HMAC512
        return jwtToken; //(2-6)
    }

    //(1-1)
    public KakaoProfile findProfile(String token) {

        //(1-3)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token); //(1-4)
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //(1-5)
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers);

        //(1-6)
        // Http 요청 (POST 방식) 후, response 변수에 응답을 받음
        ResponseEntity<String> kakaoProfileResponse = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        //(1-7)
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoProfile;
    }

    public OauthToken getAccessToken(String code, String redirectUri) {

        //(3)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //(4)
        MultiValueMap<String, String> payload = new LinkedMultiValueMap<>();
        payload.add("grant_type", "authorization_code");
        payload.add("client_id", clientId);
        payload.add("redirect_uri", redirectUri);
        payload.add("code", code);
//        params.add("client_secret", "{시크릿 키}"); // 생략 가능!

        //(5)
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(payload, headers);

        //(6)
        ResponseEntity<OauthToken> accessTokenResponse = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                OauthToken.class
        );

        return accessTokenResponse.getBody(); //(8)
    }
    public User getUser(HttpServletRequest request) { //(1)
        //(2)
        Long userCode = (Long) request.getAttribute("userCode");

        //(3)
        User user = userRepository.findByKakaoId(userCode);

        //(4)
        return user;
    }
}