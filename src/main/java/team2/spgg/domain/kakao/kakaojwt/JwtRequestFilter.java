package team2.spgg.domain.kakao.kakaojwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

//(1)
@RequiredArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired //(2)
    UserRepository userRepository;

    @Override //(3)
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //(4)
        String jwtHeader = ((HttpServletRequest)request).getHeader(JwtProperties.HEADER_STRING);

        //(5)
        if(jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        //(6)
        String token = jwtHeader.replace(JwtProperties.TOKEN_PREFIX, "");

        Long userCode = null;

        //(7)
        try {
            userCode = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                    .getClaim("id").asLong();

        } catch (TokenExpiredException e) {
            e.printStackTrace();
            request.setAttribute(JwtProperties.HEADER_STRING, "토큰이 만료되었습니다.");
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            request.setAttribute(JwtProperties.HEADER_STRING, "유효하지 않은 토큰입니다.");
        }

        //(8)
        request.setAttribute("userCode", userCode);

        //(9)
        filterChain.doFilter(request, response);
    }
}
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        //(1)
        String exception = (String) request.getAttribute(JwtProperties.HEADER_STRING);
        String errorCode;

        if(exception.equals("토큰이 만료되었습니다.")) {
            errorCode = "토큰이 만료되었습니다.";
            setResponse (response, errorCode);
        }

        if(exception.equals("유효하지 않은 토큰입니다.")) {
            errorCode = "유효하지 않은 토큰입니다.";
            setResponse(response, errorCode);
        }
    }

    //(2)
    private void setResponse(HttpServletResponse response, String errorCode) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(JwtProperties.HEADER_STRING + " : " + errorCode);
    }
}