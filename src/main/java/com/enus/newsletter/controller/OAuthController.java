package com.enus.newsletter.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enus.newsletter.model.dto.UserDTO;
import com.enus.newsletter.model.request.auth.ExchangeTokenRequest;
import com.enus.newsletter.model.response.OauthCallbackResponse;
import com.enus.newsletter.model.response.Token;
import com.enus.newsletter.service.JwtService;
import com.enus.newsletter.service.TokenService;
import com.enus.newsletter.service.UserService;
import com.enus.newsletter.system.GeneralServerResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


@Tag(name="OAuth", description = "OAuth2 API")
@Slf4j(topic = "OAuthController")
@RestController
@RequestMapping("/api/oauth")
public class OAuthController {

    private final TokenService tokenService;
    private final JwtService jwtService;
    private final UserService userService;
    
    public OAuthController(TokenService tokenService, UserService userService, JwtService jwtService) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/google")
    public void googleAuth(HttpServletResponse response) throws IOException {
        log.info("Google OAuth2 login initiated");
        response.sendRedirect("/oauth2/authorization/google");
    }

    @GetMapping("/kakao")
    public void kakaoAuth(HttpServletResponse response) throws IOException {
        log.info("Kakao OAuth2 login initiated");
        response.sendRedirect("/oauth2/authorization/kakao");
    }

    @GetMapping("/naver")
    public void naverAuth(HttpServletResponse response) throws IOException {
        log.info("Naver OAuth2 login initiated");
        response.sendRedirect("/oauth2/authorization/naver");
    }

    @PostMapping("/exchange-token")
    public ResponseEntity<GeneralServerResponse<OauthCallbackResponse>> exchangeToken(@Valid @RequestBody ExchangeTokenRequest body) throws IOException {
        log.info("[exchangeToken()] Called");
        Token token = tokenService.exchangeToken(body.getTempToken());
        String email = jwtService.extractEmail(token.getAccessToken());
        UserDTO user = userService.getUser(email);
        OauthCallbackResponse callbackResponse = OauthCallbackResponse.builder()
            .token(token)
            .user(user)
            .build();
        GeneralServerResponse<OauthCallbackResponse> response = new GeneralServerResponse<>(
            false,
            "Successfully exchanged token",
            200,
            callbackResponse
        );
        return ResponseEntity.ok(response);
    }
}
