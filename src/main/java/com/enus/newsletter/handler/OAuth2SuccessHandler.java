package com.enus.newsletter.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.repository.UserRepository;
import com.enus.newsletter.service.JwtService;

import java.io.IOException;

@Slf4j(topic = "OAUTH2_SUCCESS_HANDLER")
@Transactional
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public OAuth2SuccessHandler(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("---------- OAuth2SuccessHandler -----------");
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
        String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

        log.info("Client ID -> {}", registrationId);

        String email = null;
        String firstName = null;
        String lastName = null;
        String username = null;

        switch (registrationId) {
            case "google":
                email = oAuth2User.getAttribute("email");
                boolean emailVerified = oAuth2User.getAttribute("email_verified");
                lastName = oAuth2User.getAttribute("family_name");
                firstName = oAuth2User.getAttribute("given_name");
                username = oAuth2User.getAttribute("sub");

                UserEntity user = UserEntity.builder()
                    .email(email)
                    .username(username)
                    .firstName(firstName)
                    .lastName(lastName)
                    .isOauthUser(true)
                    .build(); 

                userRepository.save(user);
                
                break;
        
            default:
                break;
        }


        super.onAuthenticationSuccess(request, response, authentication);
//         response.sendRedirect("/websocket-client.html");
    }

}
