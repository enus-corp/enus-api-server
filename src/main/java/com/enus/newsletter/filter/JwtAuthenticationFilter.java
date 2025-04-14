package com.enus.newsletter.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.enus.newsletter.auth.EmailPasswordAuthenticationToken;
import com.enus.newsletter.exception.auth.TokenErrorCode;
import com.enus.newsletter.exception.auth.TokenException;
import com.enus.newsletter.interfaces.CustomUserDetailsImpl;
import com.enus.newsletter.service.CustomUserDetailsServiceImpl;
import com.enus.newsletter.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j(topic = "JWT_AUTH_FILTER")
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsServiceImpl userDetailsService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException, TokenException {
        final String authHeader = request.getHeader("Authorization");
        
        log.info("[JwtAuthenticationFilter] Auth Header: {}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String email = jwtService.extractEmail(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (email != null && authentication == null) {
                CustomUserDetailsImpl userDetails = this.userDetailsService.loadUserByUsername(email);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    EmailPasswordAuthenticationToken authToken = new EmailPasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.warn("[JwtAuthenticationFilter] JWT Token has expired: {}", e.getMessage());
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, TokenErrorCode.TOKEN_EXPIRED, "Token has expired");
        } catch (MalformedJwtException| UnsupportedJwtException | IllegalArgumentException e) {
            log.warn("[JwtAuthenticationFilter] Invalid JWT Token: {}", e.getMessage());
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, TokenErrorCode.TOKEN_NOT_VALID, "Invalid token signature or structure");
        } catch (TokenException e) {
            log.warn("[JwtAuthenticationFilter] TokenException caught: Code={}, Message={}", e.getErrorCode(), e.getMessage());
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, e.getErrorCode(), e.getMessage());
        } catch (ServletException | IOException | UsernameNotFoundException e) {
            log.error("[JwtAuthenticationFilter] Unexpected error during JWT processing: {}", e.getMessage(), e);
            setErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, 9999, "Internal server error during authentication");
            throw new ServletException("Internal error during authentication filter processing", e);
        }
    }

    private void setErrorResponse(
        HttpServletResponse response, 
        HttpStatus status, 
        TokenErrorCode errorCode,
        String message) throws IOException {
        setErrorResponse(response, status, errorCode.getCode(), message);
    }

    private void setErrorResponse(HttpServletResponse response, HttpStatus status, int errorCode, String message)
            throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new HashMap<>();
        body.put("errorCode", errorCode);
        body.put("errorMessage", message);
        objectMapper.writeValue(response.getWriter(), body);
    }
}
