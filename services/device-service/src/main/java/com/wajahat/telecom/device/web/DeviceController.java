package com.wajahat.telecom.device.web;

import com.wajahat.telecom.device.dto.CreateDeviceRequest;
import com.wajahat.telecom.device.dto.DeviceResponse;
import com.wajahat.telecom.device.service.DeviceService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceService deviceService;

    DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    ResponseEntity<DeviceResponse> create(@Valid @RequestBody CreateDeviceRequest request) {
        DeviceResponse response = DeviceResponse.from(deviceService.create(request));
        return ResponseEntity.created(URI.create("/devices/" + response.deviceId())).body(response);
    }

    @GetMapping("/{deviceId}")
    DeviceResponse getById(@PathVariable UUID deviceId) {
        return DeviceResponse.from(deviceService.getById(deviceId));
    }

    @GetMapping("/customer/{customerId}")
    List<DeviceResponse> getByCustomerId(@PathVariable UUID customerId) {
        return deviceService.getByCustomerId(customerId).stream()
                .map(DeviceResponse::from)
                .toList();
    }
}
