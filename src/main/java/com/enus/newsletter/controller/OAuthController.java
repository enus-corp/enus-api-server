package com.enus.newsletter.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enus.newsletter.service.AuthService;
import com.enus.newsletter.service.JwtService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Tag(name="OAuth", description = "OAuth2 API")
@Slf4j(topic = "OAuthController")
@RestController
@RequestMapping("/api/oauth")
public class OAuthController {

    private final JwtService jwtService;
    private final AuthService authService;

    public OAuthController(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
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

    @GetMapping("/success")
    public void success(HttpServletResponse response, @RequestParam String state) throws IOException {
        log.info("OAuth2 login success");
        String email = jwtService.extractEmail(state);
        log.info("\t\t Email is : {}", email);   
        authService.createUserByEmail(email);
    }
}
