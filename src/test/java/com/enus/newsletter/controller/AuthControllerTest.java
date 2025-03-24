/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package com.enus.newsletter.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.enums.Gender;
import com.enus.newsletter.model.request.auth.SignupRequest;
import com.enus.newsletter.model.response.Token;
import com.enus.newsletter.model.response.VerifyViaEmail;
import com.enus.newsletter.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author idohyeon
 */
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    private UserEntity testUser;

    private Token testToken;

    private VerifyViaEmail testVerification;

    @BeforeEach
    void setup() {
        testUser = new UserEntity("John", "Doe", "adminUser123", "P@ineappleMan123!", "pineapple@test.com", Gender.male, 0);
    }

    @Test
    void testSignup() throws Exception {
        SignupRequest request = SignupRequest.builder()
            .username("testuser")
            .email("test@example.com")
            .password("Password1!")
            .firstName("Test")
            .lastName("User")
            .gender("male")
            .age(30)
            .build();

        when(authService.signup(any(SignupRequest.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/auth/signup")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.error").value(false))
            .andExpect(jsonPath("$.message").value("Successfully created user"))
            .andExpect(jsonPath("$.data.username").value("testuser"))
            .andExpect(jsonPath("$.data.email").value("test@example.com"))
            ;

    }

}