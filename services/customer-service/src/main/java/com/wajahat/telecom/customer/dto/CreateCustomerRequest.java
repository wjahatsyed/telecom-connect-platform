package com.wajahat.telecom.customer.dto;

import com.wajahat.telecom.customer.domain.CustomerType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCustomerRequest(
        @NotNull CustomerType type,
        @NotBlank @Size(max = 160) String displayName,
        @NotBlank @Email @Size(max = 254) String email,
        @Size(max = 40) String phoneNumber) {
}
