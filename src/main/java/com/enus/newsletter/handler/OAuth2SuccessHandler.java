 package com.enus.newsletter.handler;

 import jakarta.servlet.ServletException;
 import jakarta.servlet.http.HttpServletRequest;
 import jakarta.servlet.http.HttpServletResponse;
 import lombok.RequiredArgsConstructor;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
 import org.springframework.stereotype.Component;

 import java.io.IOException;

 @Slf4j(topic = "OAUTH2_SUCCESS_HANDLER")
 @RequiredArgsConstructor
 @Component
 public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

     @Override
     public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
         String targetUrl;

         log.info("---------- OAuth2SuccessHandler -----------");
         log.info("---------- Authentication: {}", authentication);

         super.onAuthenticationSuccess(request, response, authentication);
//         response.sendRedirect("/websocket-client.html");
     }

 }
