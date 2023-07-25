package team2.spgg.domain.kakao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import team2.spgg.domain.kakao.kakaojwt.CustomAuthenticationEntryPoint;
import team2.spgg.domain.kakao.kakaojwt.JwtRequestFilter;
import team2.spgg.domain.kakao.repository.KakaoRepository;

@Configuration
@EnableWebSecurity
public class CorsConfig {

    @Value("${front.url}")
    private String frontUrl;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern(frontUrl);
        config.addAllowedHeader("*");
        config.addExposedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public CustomAuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter(KakaoRepository kakaoRepository) {
        return new JwtRequestFilter(kakaoRepository);
    }
}


//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf((csrf) -> csrf.disable())
//                .cors(withDefaults())
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .httpBasic().disable()
//                .formLogin().disable()
//                .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
//                .authorizeRequests()
//                .antMatchers(frontUrl + "/main/**").authenticated()
//                .anyRequest().permitAll()
//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint(authenticationEntryPoint());
//    }
//}
