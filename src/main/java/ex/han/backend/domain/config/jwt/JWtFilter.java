package ex.han.backend.domain.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import ex.han.backend.domain.config.auth.CustomUserDetails;
import ex.han.backend.domain.user.User;
import ex.han.backend.domain.user.UserRepository;
import ex.han.backend.global.exception.AuthenticationException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class JWtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;


    /*TEST*/
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/api/test");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String path = request.getRequestURI();
        if (path.startsWith("/api/test/")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 쿠키에서 JWT 토큰 가져오기
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    token = cookie.getValue();
                }
            }
        }
        try {
            /* --------- JWT 토큰 검증 --------*/
            if (token == null || jwtProvider.isExpired(token)) {
                throw new AuthenticationException("Invalid or expired JWT token");
            }
            String email = jwtProvider.getEmail(token);
            //탈퇴한 유저인지 확인
            boolean isEligible = userRepository.existsUserByEmailAndDeactivationDateIsNull(email);
            if(!isEligible) throw new AuthenticationException("Authenticate with deactivated user authentication");
            String nickname = jwtProvider.getNickname(token);
            String userRole = jwtProvider.getUserRole(token);
            String oauthType = jwtProvider.getUserOauthType(token);

            User user = new User();
            user.setEmail(email);
            user.setNickname(nickname);
            user.setUserRole(userRole);
            user.setOauthType(oauthType);

            CustomUserDetails userDetails = new CustomUserDetails(user);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException("JWT expired");
        } catch (AuthenticationException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("code", "AF");
            errorResponse.put("message", "Authentication Failed");

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);

            response.getWriter().write(jsonResponse);
        }
    }
}
