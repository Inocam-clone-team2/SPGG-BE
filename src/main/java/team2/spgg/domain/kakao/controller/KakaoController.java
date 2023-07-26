package team2.spgg.domain.kakao.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import team2.spgg.domain.kakao.entity.Kakao;
import team2.spgg.domain.kakao.entity.oauth.OauthToken;
import team2.spgg.domain.kakao.kakaojwt.JwtProperties;
import team2.spgg.domain.kakao.service.KakaoService;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class KakaoController {


    private final KakaoService kakaoService;

    private final String clientId = "9b3f5d5d410faff975879a54b0c9f165";

    @GetMapping("/kakao")
    public ResponseEntity getKakao(HttpServletRequest request){
        String redirectUri = "http://" + request.getHeader("host") + "/api/oauth/token";
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        log.info("uri = " + uri.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri);
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    // 프론트에서 인가코드 받아오는 url
    @GetMapping("/token")
    public ResponseEntity getLogin(@RequestParam("code") String code,HttpServletRequest request) { //(1)

        String redirectUri = "http://" + request.getHeader("host") + "/api/oauth/token";

        // 넘어온 인가 코드를 통해 access_token 발급
        OauthToken oauthToken = kakaoService.getAccessToken(code, redirectUri);

        //(2)
        // 발급 받은 accessToken 으로 카카오 회원 정보 DB 저장 후 JWT 를 생성
        String jwtToken = kakaoService.saveUserAndGetToken(oauthToken.getAccess_token());

        //(3)
        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        //(4)
        return ResponseEntity.ok().headers(headers).body("success");
    }

    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser(HttpServletRequest request) { //(1)

        //(2)
        Kakao kakao = kakaoService.getUser(request);

        //(3)
        return ResponseEntity.ok().body(kakao);
    }
}
/*
앱 키
플랫폼	앱 키	재발급
네이티브 앱 키	3fe963e66a41089f22e5b69567be2088복사	재발급
REST API 키	9b3f5d5d410faff975879a54b0c9f165복사	재발급
JavaScript 키	179bed4135d6d30735844f5355cbf4ce복사	재발급
Admin 키	c40a759e8a9dbd24909893e8b46b5606복사	재발급
 */
