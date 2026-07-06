package com.wajahat.telecom.device.dto;

import com.wajahat.telecom.device.domain.DeviceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CreateDeviceRequest(
        @NotNull UUID customerId,
        @NotNull DeviceType type,
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Pattern(regexp = "\\d{14,16}", message = "must contain 14 to 16 digits") String imei) {
}
