package com.wajahat.telecom.customer.web;

import java.time.Instant;
import java.util.List;

record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        List<FieldViolation> violations) {

    static ApiErrorResponse of(int status, String error, String message) {
        return new ApiErrorResponse(Instant.now(), status, error, message, List.of());
    }

    record FieldViolation(String field, String message) {
    }
}
