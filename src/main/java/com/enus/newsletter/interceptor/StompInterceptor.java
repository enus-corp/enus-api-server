package com.enus.newsletter.interceptor;

import java.util.Collections;

import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.enus.newsletter.exception.auth.TokenErrorCode;
import com.enus.newsletter.exception.auth.TokenException;
import com.enus.newsletter.interfaces.CustomUserDetailsImpl;
import com.enus.newsletter.service.CustomUserDetailsServiceImpl;
import com.enus.newsletter.service.JwtService;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "STOMP_INTERCEPTOR")
@Component
public class StompInterceptor implements ChannelInterceptor {
    private final JwtService jwtService;
    private final CustomUserDetailsServiceImpl userDetailsService;
    
    public StompInterceptor(JwtService jwtService, CustomUserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    @Nullable
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();

        if (accessor.getCommand() != null) {
            // check CONNECT command
            switch (accessor.getCommand()) {
                case CONNECT -> {
                    log.info("[{}][{}] CONNECT", accessor.getCommand(), sessionId);
                    String authHeader = accessor.getFirstNativeHeader("Authorization");
                    if (authHeader == null) {
                        log.error("Authorization header is missing");
                        throw new TokenException(TokenErrorCode.TOKEN_NOT_VALID, "Authorization header is missing");
                    }   String[] parts = authHeader.split(" ");
                    String accessToken = "";
                    if (parts.length > 1 && "Bearer".equals(parts[0])) {
                        // extract access token from header
                        accessToken = parts[1];
                        try {
                            String email = jwtService.extractEmail(accessToken);
                            if (email == null) {
                                log.error("Username could not be extracted from token");
                                throw new TokenException(TokenErrorCode.TOKEN_NOT_VALID, "Invalid token format");
                            }
                            
                            CustomUserDetailsImpl userDetails = userDetailsService.loadUserByUsername(email);
                            
                            if (jwtService.isTokenValid(accessToken, userDetails)){
                                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        Collections.singleton((GrantedAuthority) () -> "USER")
                                );
                                log.info("[CONNECT][{}] Authentication -> {}", sessionId, auth);
                                accessor.setUser(auth);
                                // SecurityContextHolder.getContext().setAuthentication(auth);
                                log.info("[CONNECT][{}] Successfully authenticated user: {}", sessionId, email);
                            } else {
                                log.error("[CONNECT][{}] Invalid token for user: {}", sessionId, email);
                                throw new TokenException(TokenErrorCode.TOKEN_NOT_VALID, TokenErrorCode.TOKEN_NOT_VALID.getMessage());
                            }
                        } catch (Exception e) {
                            log.error("[CONNECT][{}][Exception] Error while authenticating user: {}", sessionId, e.getMessage());
                            throw new TokenException(TokenErrorCode.TOKEN_NOT_VALID, TokenErrorCode.TOKEN_NOT_VALID.getMessage());
                        }
                    } else {
                        log.error("[CONNECT][{}] Invalid Authorization header format", sessionId);
                        throw new TokenException(TokenErrorCode.TOKEN_NOT_VALID, TokenErrorCode.TOKEN_NOT_VALID.getMessage());
                    }
                }
                case SUBSCRIBE, SEND -> {
                    if (accessor.getUser() != null) {
                        String username = accessor.getUser().getName();
                        log.info("[{}][{}] User: {}", accessor.getCommand(), sessionId, username);
                    } else {
                        log.warn("[{}][{}] No user found in session", accessor.getCommand(), sessionId);
                    }
                }
                default -> log.info("[{}][{}] {}", accessor.getCommand(), sessionId);
            }
        }

        return message;
    }

}
