package ex.han.hanbackend.domain.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 인증 실패 시 401 반환
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증실패 401: " + authException.getMessage());
    }
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        // 권한 거부 시 403 반환
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "권한 거부 403: " + accessDeniedException.getMessage());
    }


}