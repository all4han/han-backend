package ex.han.backend.domain.config.auth;

import ex.han.backend.domain.config.jwt.JwtProvider;
import ex.han.backend.domain.user.User;
import ex.han.backend.domain.user.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;
@Slf4j
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    //    @Value("${jwt.redirect-url}")
    private final String redirectUrl;
    @Value("${jwt.cookie-secure}")
    private boolean cookieSecure;

    public AuthSuccessHandler(UserRepository userRepository, JwtProvider jwtProvider, String redirectUrl) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.redirectUrl = redirectUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        if(email == null) {
            email = (String) ((Map<String, Object>) oauth2User.getAttribute("kakao_account")).get("email");
        }
        User user = userRepository.findUserByEmailAndDeactivationDateIsNull(email).orElseThrow();
        String jwt = jwtProvider.generateToken(user, 60 * 60 * 30* 1000L);

        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        //TODO 배포 시 true로 변경해야함 (TLS 적용 시)
        cookie.setSecure(cookieSecure);
        response.addCookie(cookie);

        response.sendRedirect(redirectUrl);
    }
}