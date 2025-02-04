package com.enus.newsletter.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.enus.newsletter.filter.JwtAuthenticationFilter;
import com.enus.newsletter.handler.OAuth2FailHandler;
import com.enus.newsletter.handler.OAuth2SuccessHandler;
import com.enus.newsletter.service.CustomOAuth2UserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailHandler oAuth2FailHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter, OAuth2SuccessHandler oAuth2SuccessHandler, OAuth2FailHandler oAuth2FailHandler, CustomOAuth2UserService customOAuth2UserService) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.oAuth2FailHandler = oAuth2FailHandler;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("---------- Security Filter Chain -----------");

        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(config ->
                        config
                                .successHandler(oAuth2SuccessHandler)
                                .failureHandler(oAuth2FailHandler)
                                .userInfoEndpoint(userEndpointConfig -> userEndpointConfig.userService(customOAuth2UserService))
                        )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                // Swagger
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                // Authentication
                                "/api/auth/signup",
                                "/api/auth/signin",
                                "/api/auth/refreshToken",
                                "/api/auth/verifyEmail",
                                "/api/auth/resetPassword",
                                // Error
                                "/error"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // check for JWT token in the request
                // If Token does not exits, pass to UsernamePasswordAuthenticationFilter to authenticate user principal and credential
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // Authentication Manager delegates the authentication to the AuthenticationProvider
                .authenticationProvider(authenticationProvider)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST" , "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
