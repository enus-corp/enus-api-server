package com.enus.newsletter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;


@Tag(name="OAuth", description = "OAuth2 API")
@Slf4j(topic = "OAuthController")
@RestController
@RequestMapping("/api/oauth")
public class OAuthController {

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
    public void success(HttpServletResponse response) throws IOException {
        log.info("OAuth2 login success");
        response.sendRedirect("/api/auth/oauth/success");
    }
}
