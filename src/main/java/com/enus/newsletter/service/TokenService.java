package com.enus.newsletter.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.enus.newsletter.exception.auth.TokenErrorCode;
import com.enus.newsletter.exception.auth.TokenException;
import com.enus.newsletter.model.response.Token;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "TOKEN_SERVICE")
@Service
public class TokenService {
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtService jwtService;

    public TokenService(RedisTemplate<String, String> redisTemplate, JwtService jwtService) {
        this.redisTemplate = redisTemplate;
        this.jwtService = jwtService;
    }

    public String storeToken(String tempToken, String accessString, String refreshToken) {
        Token token = Token.builder()
            .accessToken(accessString)
            .refreshToken(refreshToken)
            .build();

        redisTemplate.opsForValue().set(
            "temp:" + tempToken,
            token.toJson(),
            5,
            TimeUnit.MINUTES
        );

        return tempToken;
    }

    public Token exchangeToken(String tempToken) {
        String key = "temp:" + tempToken;
        String tokenJson = redisTemplate.opsForValue().get(key);

        if (tokenJson == null) {
            throw new TokenException(TokenErrorCode.TOKEN_EXPIRED, TokenErrorCode.TOKEN_EXPIRED.getMessage());
        }

        redisTemplate.delete(key);

        return Token.fromJson(tokenJson);
    }
}
