package com.enus.newsletter;

import com.enus.newsletter.model.request.SignupRequest;
import com.enus.newsletter.service.AuthService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
public class AuthControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private AuthService authService;

    private MockMvc mockMvc;

    @BeforeEach
    public void before() {
        System.out.println("Test Before");
    }

    @Test
    @DisplayName("Test Signup")
    void testSignup() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .firstName("robert")
                .lastName("lee")
                .username("admin1234")
                .password("P@ssw0rd123")
                .email("admin1234@test.com")
                .build();

    }

}
