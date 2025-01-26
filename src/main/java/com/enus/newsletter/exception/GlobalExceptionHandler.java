package com.enus.newsletter.exception;

import com.enus.newsletter.system.GeneralServerResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
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

// Handle exceptions thrown by methods annotated with @RequestMapping
@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Validation handler
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, List<String>> body = new HashMap<>();

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        body.put("errors", errors);

        log.error("---------------- Validation error: {} ----------------", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomBaseException.class)
    public ResponseEntity<GeneralServerResponse<Void>> handleException(CustomBaseException exception) {
        log.info("---------------- Exception Handler: {} ----------------", exception.getMessage());
        return ResponseEntity.ok(
                new GeneralServerResponse<Void>(
                        false,
                        exception.getMessage(),
                        exception.getErrorCode(),
                        null
                )
        );
    }

}
