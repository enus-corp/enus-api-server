package com.enus.newsletter.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.enus.newsletter.interfaces.ICustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${app.auth.jwt.secret-key}")
    private String secretKey;

    @Value("${app.auth.jwt.access-token.expiration}")
    private Long accessTokenExpiration;

    @Value("${app.auth.jwt.refresh-token.expiration}")
    private Long refreshTokenExpiration;

    @Value("${app.auth.jwt.temporary-token.expiration}")
    private Long temporaryTokenExpiration;

    /*
    * ==============
    * Public
    * ==============
    * */

    public String genereateTempToken(String token) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "ACCESS");
        return Jwts
                .builder()
                .claims(claims)
                .subject(token)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + temporaryTokenExpiration))
                .signWith(getSignInKey())
                .compact();
    }
    
    public String generateAccessToken(ICustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "ACCESS");
        claims.put("role", userDetails.getAuthorities());
        return buildToken(claims, userDetails, accessTokenExpiration);
    }

    public String generateRefreshToken(ICustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "REFRESH");
        claims.put("jti", UUID.randomUUID().toString());
        return buildToken(claims, userDetails, refreshTokenExpiration);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public long getExpirationTime() {
        return accessTokenExpiration;
    }

    public boolean isTokenValid(String token, ICustomUserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getEmail()) && !isTokenExpired(token));
    }

    public String generateTemporaryToken(String uuid) {
        long expiration = 1000 * 60 * 5; // 5 minutes
        return Jwts
                .builder()
                .subject(uuid)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }
    /*
    * ==============
    * Private
    * ==============
    * */


    private String buildToken(
            Map<String, Object> extraClaims,
            ICustomUserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
