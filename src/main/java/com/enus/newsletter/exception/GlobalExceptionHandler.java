package com.enus.newsletter.exception;

import com.enus.newsletter.system.GeneralServerResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.validation.FieldError;

import lombok.extern.slf4j.Slf4j;

// Handle exceptions thrown by methods annotated with @RequestMapping
@Slf4j(topic="GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Validation handler
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

    @ExceptionHandler(CustomBaseException.class)
    public ResponseEntity<GeneralServerResponse<Void>> handleException(CustomBaseException exception) {
        log.info("---------------- Exception Handler: {} ----------------", exception.getMessage());
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
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
