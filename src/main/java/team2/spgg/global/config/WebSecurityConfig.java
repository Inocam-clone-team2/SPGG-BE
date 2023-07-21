package team2.spgg.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import team2.spgg.global.jwt.JwtAuthenticationFilter;
import team2.spgg.global.jwt.JwtAuthorizationFilter;
import team2.spgg.global.jwt.JwtExceptionFilter;
import team2.spgg.global.jwt.JwtProvider;
import team2.spgg.global.security.UserDetailsServiceImpl;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    /**
     * JwtExceptionFilter를 빈으로 생성합니다.
     * JWT 예외 처리를 담당합니다.
     *
     * @return JwtExceptionFilter 인스턴스
     */
    @Bean
    public JwtExceptionFilter jwtExceptionFilter() {
        return new JwtExceptionFilter();
    }

    /**
     * AuthenticationManager를 빈으로 생성합니다.
     * 인증 매니저를 구성합니다.
     *
     * @param configuration AuthenticationConfiguration 인스턴스
     * @return AuthenticationManager 인스턴스
     * @throws Exception 예외 발생 시
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * JwtAuthenticationFilter를 빈으로 생성합니다.
     * JWT 인증 필터를 구성합니다.
     *
     * @return JwtAuthenticationFilter 인스턴스
     * @throws Exception 예외 발생 시
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtProvider);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    /**
     * JwtAuthorizationFilter를 빈으로 생성합니다.
     * JWT 인가 필터를 구성합니다.
     *
     * @return JwtAuthorizationFilter 인스턴스
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtProvider, userDetailsService);
    }

    /**
     * CORS 구성을 설정합니다.
     *
     * @return CorsConfigurationSource 인스턴스
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT","OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.addExposedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * SecurityFilterChain을 설정합니다.
     * CSRF 설정, CORS 설정, 세션 생성 방식을 JWT 방식으로 변경합니다.
     * 요청 경로에 따른 인증 및 인가를 처리합니다.
     *
     * @param http HttpSecurity 인스턴스
     * @return SecurityFilterChain 인스턴스
     * @throws Exception 예외 발생 시
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CSRF 설정, CORS 설정, 기존 세션 방식 -> JWT 방식
        http
                .csrf((csrf) -> csrf.disable())
                .cors(withDefaults())
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers(GET, "/post/**").permitAll()
                                .requestMatchers(GET, "/aa/ranking").permitAll() // "/aa/ranking" 경로를 모든 사용자에게 허용
                                .requestMatchers(GET,"/api/**").permitAll() // "/api/"로 시작하는 모든 요청을 승인
                                .anyRequest().authenticated()) // 그 외 모든 요청 인증처리
                .addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter(), JwtAuthenticationFilter.class);

        return http.build();
    }
}

