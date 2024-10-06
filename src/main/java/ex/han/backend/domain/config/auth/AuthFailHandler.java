package ex.han.backend.domain.config.auth;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
public class AuthFailHandler implements AuthenticationFailureHandler {

    private final String redirectUrlFailed;

    public AuthFailHandler(String redirectUrlFailed) {
        this.redirectUrlFailed = redirectUrlFailed;
    }
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.warn("OAuth2 authentication failed.");
        exception.printStackTrace();

        response.sendRedirect(redirectUrlFailed);
    }
}
