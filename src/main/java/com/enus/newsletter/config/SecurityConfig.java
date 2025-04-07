package com.enus.newsletter.config;

import java.util.List;

import com.enus.newsletter.handler.OAuth2SuccessHandler;
import com.enus.newsletter.service.CustomUserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.enus.newsletter.auth.CustomAuthenticationProvider;
import com.enus.newsletter.filter.JwtAuthenticationFilter;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomUserDetailsServiceImpl userDetailsService;

    public SecurityConfig(
            PasswordEncoder passwordEncoder,
            JwtAuthenticationFilter jwtAuthenticationFilter, 
            ClientRegistrationRepository clientRegistrationRepository, 
            OAuth2SuccessHandler oAuth2SuccessHandler,
            CustomUserDetailsServiceImpl userDetailsService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public CustomAuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/error");
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
                                .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig.baseUri("/oauth2/authorization"))
                                .redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig.baseUri("/login/oauth2/code/*"))
                ) // add OAuth2LoginAuthenticationFilter
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(
                                        // Internal testing
                                        "/websocket-client.html",
                                        // Swagger
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        // Authentication
                                        "/api/auth/signup",
                                        "/api/auth/signin",
                                        "/api/auth/refresh",
                                        "/api/auth/verifyEmail",
                                        "/api/auth/resetPassword",
                                        // Error
                                        "/error",
                                        "/ws/**"
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                // check for JWT token in the request
                // If Token does not exists, pass to UsernamePasswordAuthenticationFilter to authenticate user principal and credential
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // Authentication Manager delegates the authentication to the AuthenticationProvider
                .authenticationProvider(authenticationProvider())
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
