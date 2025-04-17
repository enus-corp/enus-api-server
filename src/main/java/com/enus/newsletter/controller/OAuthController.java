package com.enus.newsletter.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enus.newsletter.model.request.auth.ExchangeTokenRequest;
import com.enus.newsletter.model.response.Token;
import com.enus.newsletter.service.TokenService;
import com.enus.newsletter.system.GeneralServerResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

    public OAuthController(TokenService tokenService) {
        this.tokenService = tokenService;
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
    public ResponseEntity<GeneralServerResponse<Token>> exchangeToken(@Valid @RequestBody ExchangeTokenRequest body) throws IOException {
        log.info("[exchangeToken()] Called");
        Token token = tokenService.exchangeToken(body.getTempToken());
        GeneralServerResponse<Token> response = new GeneralServerResponse<Token>(
            false,
            "Successfully exchanged token",
            200,
            token
        );
        return ResponseEntity.ok(response);
    }
}
