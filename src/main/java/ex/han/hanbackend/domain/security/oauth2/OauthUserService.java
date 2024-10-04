package ex.han.hanbackend.domain.security.oauth2;


import ex.han.hanbackend.domain.user.User;
import ex.han.hanbackend.domain.user.UserRepository;
import ex.han.hanbackend.domain.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static ex.han.hanbackend.domain.user.UserRole.ROLE_USER;

@Service
@RequiredArgsConstructor
public class OauthUserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String email = getEmail(oAuth2User, provider);

        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!user.getProvider().equals(provider)) {
                throw new OAuth2AuthenticationException("Looks like you're signed up with " + user.getProvider() + " oAuth2User. Please use your " + user.getProvider() + " oAuth2User to login.");
            }
            user = updateExistingUser(user, oAuth2User, provider);
        } else {
            user = registerNewUser(oAuth2User, provider);
        }

        return OauthPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2User oAuth2User, String provider) {
        User user = User.builder()
                .email(getEmail(oAuth2User, provider))
                .name(getName(oAuth2User, provider))
                .provider(provider)
                .build()
                .addRole(UserRole.ROLE_USER);
        return userRepository.save(user);
    }

    private User updateExistingUser(User user, OAuth2User oAuth2User, String provider) {
        user.setName(getName(oAuth2User, provider));
        return userRepository.save(user);
    }

    private String getEmail(OAuth2User oAuth2User, String provider) {
        switch (provider) {
            case "google":
                return oAuth2User.getAttribute("email");
            // Add cases for other providers as needed
            default:
                throw new OAuth2AuthenticationException("Sorry! Login with " + provider + " is not supported yet.");
        }
    }
    private String getName(OAuth2User oAuth2User, String provider) {
        switch (provider) {
            case "google":
                return oAuth2User.getAttribute("name");
            // Add cases for other providers as needed
            default:
                throw new OAuth2AuthenticationException("Sorry! Login with " + provider + " is not supported yet.");
        }
    }
}