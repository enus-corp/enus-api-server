package com.enus.newsletter.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.enus.newsletter.system.GeneralServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "OAuth2FailHandler")
@Transactional
@Component
public class OAuth2FailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("---------- OAuth2FailHandler -----------");
        log.error("Authentication failed: {}", exception.getMessage());
        // Custom logic for handling authentication failure
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        GeneralServerResponse<Void> responseBody = new GeneralServerResponse<>(
                false,
                "Failed to authenticate oauth user",
                301,
                null
        );

        // Clear the security context
        SecurityContextHolder.clearContext(); 

        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
    }
    
}
