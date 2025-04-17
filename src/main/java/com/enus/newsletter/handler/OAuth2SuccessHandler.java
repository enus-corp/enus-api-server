package com.enus.newsletter.handler;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.enus.newsletter.auth.oauth.AbsOauthUserInfo;
import com.enus.newsletter.auth.oauth.OAuthUserInfoFactory;
import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.db.repository.UserRepository;
import com.enus.newsletter.model.dto.UserDTO;
import com.enus.newsletter.service.JwtService;
import com.enus.newsletter.service.TokenService;
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
    @Value("${app.oauth2.redirect-url}")
    private String clientUrl;

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final TokenService tokenService;

    public OAuth2SuccessHandler(
        JwtService jwtService, 
        UserRepository userRepository,
        TokenService tokenService,
        ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("---------- OAuth2SuccessHandler -----------");
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
        String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

        log.info("Client ID -> {}", registrationId);

        // generate uuid
        String uuid = UUID.randomUUID().toString();

        // generate temporary token
        String tempAccessToken = jwtService.generateTemporaryToken(uuid);

        
        Map<String, Object> attributes = oAuth2User.getAttributes();
        AbsOauthUserInfo oauthUserInfo = OAuthUserInfoFactory.getOauthUserInfo(registrationId, attributes);
        UserEntity user = oauthUserInfo.toEntity();
        // convert user entity to user dto
        UserDTO userDTO = UserDTO.builder()
        .userId(user.getId())
        .email(user.getEmail())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .build();
        
        // user UserDTO which implements ICustomUserDetails to generate access token and refresh token
        String accessToken = jwtService.generateAccessToken(userDTO);
        String refreshToken = jwtService.generateRefreshToken(userDTO);

        tokenService.storeToken(tempAccessToken, accessToken, refreshToken);


        String redirectionUrl = String.format("%s/oauth-callback?tempToken=%s", clientUrl, tempAccessToken);
        getRedirectStrategy().sendRedirect(request, response, redirectionUrl);
    }
}
