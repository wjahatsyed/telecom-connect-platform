package com.wajahat.telecom.esimprovisioning.web;

import com.wajahat.telecom.esimprovisioning.exception.EsimProfileNotFoundException;
import com.wajahat.telecom.esimprovisioning.exception.InvalidEsimStateException;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(EsimProfileNotFoundException.class)
    ResponseEntity<ApiErrorResponse> handleNotFound(EsimProfileNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiErrorResponse.of(404, "Not Found", exception.getMessage()));
    }

    @ExceptionHandler(InvalidEsimStateException.class)
    ResponseEntity<ApiErrorResponse> handleInvalidState(InvalidEsimStateException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiErrorResponse.of(409, "Conflict", exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        var violations = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new ApiErrorResponse.FieldViolation(error.getField(), error.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest()
                .body(new ApiErrorResponse(Instant.now(), 400, "Bad Request", "Validation failed", violations));
    }
}
