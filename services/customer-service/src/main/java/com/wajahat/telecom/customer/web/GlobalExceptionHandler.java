package com.wajahat.telecom.customer.web;

import com.wajahat.telecom.customer.exception.CustomerNotFoundException;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    ResponseEntity<ApiErrorResponse> handleNotFound(CustomerNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiErrorResponse.of(404, "Not Found", exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        var violations = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new ApiErrorResponse.FieldViolation(error.getField(), error.getDefaultMessage()))
                .toList();

        return ResponseEntity.badRequest()
                .body(new ApiErrorResponse(Instant.now(), 400, "Bad Request", "Validation failed", violations));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        return ResponseEntity.badRequest()
                .body(ApiErrorResponse.of(400, "Bad Request", "Invalid request parameter: " + exception.getName()));
    }
}
