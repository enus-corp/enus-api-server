package com.enus.newsletter.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.enus.newsletter.auth.CustomAuthenticationProvider;
import com.enus.newsletter.auth.oauth.CustomOauth2UserService;
import com.enus.newsletter.filter.JwtAuthenticationFilter;
import com.enus.newsletter.handler.OAuth2FailHandler;
import com.enus.newsletter.handler.OAuth2SuccessHandler;
import com.enus.newsletter.service.CustomUserDetailsServiceImpl;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOauth2UserService customOauth2UserService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2FailHandler oAuth2FailHandler;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomUserDetailsServiceImpl userDetailsService;

    public SecurityConfig(
            PasswordEncoder passwordEncoder,
            JwtAuthenticationFilter jwtAuthenticationFilter, 
            ClientRegistrationRepository clientRegistrationRepository, 
            OAuth2FailHandler oAuth2FailHandler,
            OAuth2SuccessHandler oAuth2SuccessHandler,
            CustomOauth2UserService customOauth2UserService,
            CustomUserDetailsServiceImpl userDetailsService
    ) {
        this.oAuth2FailHandler = oAuth2FailHandler;
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.userDetailsService = userDetailsService;
        this.customOauth2UserService = customOauth2UserService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CustomAuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Ignore security for certain request patterns
        return web -> web.ignoring()
                .requestMatchers("/error")
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                .requestMatchers("/ws/**")
                ;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("---------- Security Filter Chain -----------");

        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(config ->
                        config
                        // Base URI for OAuth2 Authorization
                        .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig.baseUri("/oauth2/authorization"))
                        // Base URI for OAuth2 Redirection which is used to redirect the user from "/oauth2/authorization/{registrationId}" (after successful authentication)
                        .redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig.baseUri("/login/oauth2/code/*"))
                        // Configures how to fetch user details from the provider
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOauth2UserService))
                        // Configures what happens after successful authentication
                        .successHandler(oAuth2SuccessHandler)
                        // Configures what happens after failed authentication
                        .failureHandler(oAuth2FailHandler)
                ) // add OAuth2LoginAuthenticationFilter
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(
                                        // Authentication
                                        "/api/auth/**",
                                        // OAuth
                                        "/api/oauth/**"
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

        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST" , "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));
        // client includes cookie, HTTP Authentication headers, client side SSL, TLS client certificate
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
