/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package com.enus.newsletter.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.enus.newsletter.exception.user.UserErrorCode;
import com.enus.newsletter.exception.user.UserException;
import com.enus.newsletter.model.request.auth.ResetPasswordRequest;
import com.enus.newsletter.model.request.auth.SigninRequest;
import com.enus.newsletter.model.request.auth.VerifyViaEmailRequest;
import com.enus.newsletter.model.request.keyword.RefreshTokenRequest;
import lombok.With;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.enums.Gender;
import com.enus.newsletter.filter.JwtAuthenticationFilter;
import com.enus.newsletter.model.request.auth.SignupRequest;
import com.enus.newsletter.model.response.Token;
import com.enus.newsletter.model.response.VerifyViaEmail;
import com.enus.newsletter.service.AuthService;
import com.enus.newsletter.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author idohyeon
 */
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = true)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private UserEntity testUser;

    private Token testToken;

    private VerifyViaEmail testVerification;

    @BeforeEach
    void setup() {
        testUser = new UserEntity("John", "Doe", "adminUser123", "P@ineappleMan123!", "pineapple@test.com", Gender.male, 0);
        testToken = Token.builder()
                .accessToken("at")
                .refreshToken("rt")
                .build();
    }

    @Test
    @WithMockUser
    void testSignup() throws Exception {
        SignupRequest request = SignupRequest.builder()
            .username("adminUser123")
            .email("pineapple@test.com")
            .password("P@ineappleMan123!")
            .firstName("John")
            .lastName("Doe")
            .gender("male")
            .age(30)
            .build();

        when(authService.signup(any(SignupRequest.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/auth/signup")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.error").value(false))
            .andExpect(jsonPath("$.data.username").value("adminUser123"))
            .andExpect(jsonPath("$.data.email").value("pineapple@test.com"))
            ;
    }

    @Test
    @WithMockUser
    void invalidInput() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .username("adminUser123")
                .email("pineapple@test.com")
                .password("invalidpassword")
                .firstName("John")
                .lastName("Doe")
                .gender("male")
                .age(30)
                .build();

        when(authService.signup(any(SignupRequest.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/auth/signup")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.data.password").value("Password must have at least 8 characters and contain at least one uppercase letter, one lowercase letter, one number, and one special character"));
    }

    @Test
    @WithMockUser
    void userExists() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .username("adminUser123")
                .email("pineapple@test.com")
                .password("P@ineappleMan123!")
                .firstName("John")
                .lastName("Doe")
                .gender("male")
                .age(30)
                .build();

        when(authService.signup(any(SignupRequest.class))).thenThrow(new UserException(UserErrorCode.USER_EXISTS, UserErrorCode.USER_EXISTS.getMessage()));

        mockMvc.perform(post("/api/auth/signup")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.code").value(UserErrorCode.USER_EXISTS.getCode()))
                .andExpect(jsonPath("$.message").value(UserErrorCode.USER_EXISTS.getMessage()));
    }

    @Test
    @WithMockUser
    void testSignin() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .username("adminUser123")
                .password("P@ineappleMan123!")
                .build();

        when(authService.authenticate(any(SigninRequest.class), any(String.class))).thenReturn(testToken);

        mockMvc.perform(post("/api/auth/signin")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.data.accessToken").value("at"))
                .andExpect(jsonPath("$.data.refreshToken").value("rt"))
        ;
    }

    @Test
    void testVerifyEmail() throws Exception {
        VerifyViaEmailRequest request = VerifyViaEmailRequest.builder()
                .email("pineapple@test.com")
                .build();

        VerifyViaEmail verify = VerifyViaEmail.builder().otp("123123").build();

        when(authService.verifyEmail(any(VerifyViaEmailRequest.class))).thenReturn(verify);

        mockMvc.perform(post("/api/auth/verifyEmail")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.data.otp").value("123123"));
    }

    @Test
    void testResetPassword() throws Exception {
        ResetPasswordRequest request = ResetPasswordRequest.builder()
                .emailCode("123123")
                .verificationCode("123123")
                .newPassword("P@ineappleMan123!")
                .build();

        doNothing().when(authService).resetPassword(any(ResetPasswordRequest.class));

        mockMvc.perform(post("/api/auth/resetPassword")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false));
    }

    @Test
    void testValidRefreshToken() throws Exception {
        RefreshTokenRequest request = RefreshTokenRequest.builder()
                .refreshToken(testToken.getRefreshToken())
                .build();

        Token newToken = Token.builder()
                .accessToken("newAccessToken")
                .refreshToken("newRefresh")
                .build();

        when(authService.refreshToken(any(RefreshTokenRequest.class))).thenReturn(newToken);

        mockMvc.perform(post("/api/auth/refresh")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.data.accessToken").value("newAccessToken"))
                .andExpect(jsonPath("$.data.refreshToken").value("newRefresh"));
    }
}