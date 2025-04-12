package com.enus.newsletter.handler;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.enus.newsletter.db.repository.UserRepository;
import com.enus.newsletter.enums.Gender;
import com.enus.newsletter.model.dto.UserDTO;
import com.enus.newsletter.service.JwtService;

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

    public OAuth2SuccessHandler(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("---------- OAuth2SuccessHandler -----------");
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
        String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

        log.info("Client ID -> {}", registrationId);

        // Extract user details based on the provider
        UserDTO userDTO = extractUserDetails(registrationId, oAuth2User);

        // Generate a temporary token and pass to controller
        // Controller will check if the user exists and create if the user does not exist
        String tempToken = jwtService.generateTemporaryToken(userDTO.getEmail());

        String redirectionUrl = "/api/oauth/success?state=" + tempToken;
        response.sendRedirect(redirectionUrl);
    }

    private UserDTO extractUserDetails(String registrationId, OAuth2User oAuth2User) {
        switch (registrationId) {
            case "google" -> {
                return extractGoogleUser(oAuth2User);
            }
            case "kakao" -> {
                return extractKakaoUser(oAuth2User);
            }
            case "naver" -> {
                return extractNaverUser(oAuth2User);
            }
            default -> throw new IllegalArgumentException("Invalid OAuth2 provider: " + registrationId);
        }
    }

    private UserDTO extractGoogleUser(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String username = oAuth2User.getAttribute("sub");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        return UserDTO.builder()
                .email(email)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .isOauthUser(true)
                .build();
    }

    private UserDTO extractKakaoUser(OAuth2User oAuth2User) {
        Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
        if (kakaoAccount == null) {
            throw new IllegalArgumentException("Kakao account details are missing");
        }

        String email = (String) kakaoAccount.get("email");
        String username = oAuth2User.getAttribute("id").toString();

        return UserDTO.builder()
                .email(email)
                .username(username)
                .isOauthUser(true)
                .build();
    }

    private UserDTO extractNaverUser(OAuth2User oAuth2User) {
        Map<String, Object> response = oAuth2User.getAttribute("response");
        if (response == null) {
            throw new IllegalArgumentException("Naver response details are missing");
        }

        String email = (String) response.get("email");
        String username = (String) response.get("id");
        String genderString = (String) response.get("gender");
        Gender gender = Gender.fromString(genderString);

        return UserDTO.builder()
                .email(email)
                .username(username)
                .isOauthUser(true)
                .gender(gender)
                .build();
    }

}
