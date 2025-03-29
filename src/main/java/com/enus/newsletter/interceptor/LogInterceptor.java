package com.enus.newsletter.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "REQUEST_LOG_INTERCEPTOR")
@Component
public class LogInterceptor implements HandlerInterceptor {

    private String getHeadersAsString(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> 
            headers.append(headerName).append(": ").append(request.getHeader(headerName)).append("\n")
        );
        return headers.toString();
    }
    
    private String getResponseHeadersAsString(HttpServletResponse response) {
        StringBuilder headers = new StringBuilder();
        response.getHeaderNames().forEach(headerName -> 
            headers.append(headerName).append(": ").append(response.getHeader(headerName)).append("\n")
        );
        return headers.toString();
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("""
                \n\n
                ==================== Request Log ====================
                Request URL: {}
                Request Method: {}
                Request Headers: {}
                Handler: {}
                =====================================================
                \n\n
                """,
                request.getRequestURL(), request.getMethod(), getHeadersAsString(request), handler);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("""
                \n\n
                ==================== Response Log ====================
                Response Status: {}
                Response Headers: {}
                ModelAndView: {}
                =====================================================
                \n\n
                """,
                response.getStatus(), getResponseHeadersAsString(response), modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("""
                \n\n
                ==================== After Completion Log ====================
                Request URL: {}
                Request Method: {}
                Response Status: {}
                Exception: {}
                ==============================================================
                \n\n
                """,
                request.getRequestURL(), request.getMethod(), response.getStatus(), ex);
    }
}