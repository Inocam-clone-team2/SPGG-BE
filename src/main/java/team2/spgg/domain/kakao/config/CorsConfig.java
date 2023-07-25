package team2.spgg.domain.kakao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import team2.spgg.domain.kakao.kakaojwt.CustomAuthenticationEntryPoint;
import team2.spgg.domain.kakao.kakaojwt.JwtRequestFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*"); //(1)
        config.addExposedHeader("*"); //(2)
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception { //SecurityConfig
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .addFilter(corsFilter);

        http.authorizeRequests()
                .antMatchers(FRONT_URL+"/main/**")
                .authenticated()
                .anyRequest().permitAll()

                .and()
                //(1)
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        //(2)
        http.addFilterBefore(new JwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}