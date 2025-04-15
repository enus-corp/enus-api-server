package com.enus.newsletter.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.enus.newsletter.exception.auth.TokenException;
import com.enus.newsletter.system.GeneralServerResponse;

import lombok.extern.slf4j.Slf4j;

// Handle exceptions thrown by methods annotated with @RequestMapping
@Slf4j(topic="GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("[handleExceptionInternal]", ex.getMessage());
        GeneralServerResponse<Void> response = new GeneralServerResponse<>(
            true,
            ex.getMessage(),
            0,
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("---------------- Validation error: {} ----------------", ex.getMessage());

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(
                    Collectors.toMap(
                        FieldError::getField, 
                        FieldError::getDefaultMessage
                        ));

        log.info(errors.toString());
        GeneralServerResponse<Map<String, String>> response = new GeneralServerResponse<>(
            true,  "Validation failed", 0, errors);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<Map<String, Object>> handleTokenException(TokenException ex) {
        log.error("---------------- TokenException Handler: {} ----------------", ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("errorCode", ex.getErrorCode());
        response.put("errorMessage", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(CustomBaseException.class)
    public ResponseEntity<GeneralServerResponse<Void>> handleException(CustomBaseException exception) {
        log.info("---------------- CustomBaseException Handler: {} ----------------", exception.getMessage());
        GeneralServerResponse<Void> response = new GeneralServerResponse<>(
                true,
                exception.getMessage(),
                exception.getErrorCode(),
                null
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralServerResponse<Void>> handleException(Exception exception) {
        log.error("---------------- Exception Handler: {} ----------------", exception.getMessage());
        GeneralServerResponse<Void> response = new GeneralServerResponse<>(
            true,
            exception.getMessage(),
            500,
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
