package com.enus.newsletter.handler;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.repository.UserRepository;
import com.enus.newsletter.model.dto.UserDTO;
import com.enus.newsletter.model.response.Token;
import com.enus.newsletter.service.JwtService;
import com.enus.newsletter.system.GeneralServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

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
        Boolean existsByEmail = false;

        if ("google".equals(registrationId)) {
            email = oAuth2User.getAttribute("email");
            boolean emailVerified = oAuth2User.getAttribute("email_verified");
            String lastName = oAuth2User.getAttribute("family_name");
            String firstName = oAuth2User.getAttribute("given_name");
            String username = oAuth2User.getAttribute("sub");

            existsByEmail = userRepository.existsByEmail(email);
    
            if (!existsByEmail) {
                log.info("User not found, creating new user");
                UserEntity userEntity = UserEntity.builder()
                        .email(email)
                        .username(username)
                        .firstName(firstName)
                        .lastName(lastName)
                        .isOauthUser(true)
                        .build();
    
                userRepository.save(userEntity);
            }
        } else if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            if (kakaoAccount != null) {
                email = kakaoAccount.get("email").toString();
            }
            String username = oAuth2User.getAttribute("id").toString();
    
            existsByEmail = userRepository.existsByEmail(email);
    
            if (!existsByEmail) {
                log.info("User not found, creating new user");
                UserEntity userEntity = UserEntity.builder()
                        .email(email)
                        .username(username)
                        .isOauthUser(true)
                        .build();
    
                userRepository.save(userEntity);
            }
        }

        // Generate JWT token
        UserDTO user = UserDTO.builder()
            .email(email)
            .isOauthUser(true)
            .build(); 

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // super.onAuthenticationSuccess(request, response, authentication);
        GeneralServerResponse<Token> responseBody = new GeneralServerResponse<Token>(
            false,
            "Successfully authenticated user",
            200,
            Token.builder().accessToken(accessToken).refreshToken(refreshToken).build()
        );

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
    }

}
