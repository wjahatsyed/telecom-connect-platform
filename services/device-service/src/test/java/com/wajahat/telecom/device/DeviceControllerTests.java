package com.wajahat.telecom.device;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wajahat.telecom.device.domain.Device;
import com.wajahat.telecom.device.repository.DeviceRepository;
import com.wajahat.telecom.device.service.DeviceService;
import com.wajahat.telecom.device.web.DeviceController;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DeviceController.class)
@Import({DeviceService.class, DeviceControllerTests.TestConfig.class})
class DeviceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsAndListsDevicesByCustomer() throws Exception {
        UUID customerId = UUID.randomUUID();

        String response = mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": "%s",
                                  "type": "IOT_SENSOR",
                                  "name": "Cold Chain Sensor",
                                  "imei": "123456789012345"
                                }
                                """.formatted(customerId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.deviceId").exists())
                .andExpect(jsonPath("$.customerId").value(customerId.toString()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String deviceId = com.jayway.jsonpath.JsonPath.read(response, "$.deviceId");

        mockMvc.perform(get("/devices/{deviceId}", deviceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deviceId").value(deviceId));

        mockMvc.perform(get("/devices/customer/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void rejectsInvalidImei() throws Exception {
        mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": "%s",
                                  "type": "PHONE",
                                  "name": "Phone",
                                  "imei": "abc"
                                }
                                """.formatted(UUID.randomUUID())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").isArray());
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        DeviceRepository deviceRepository() {
            return new DeviceRepository() {
                private final java.util.Map<UUID, Device> devices = new LinkedHashMap<>();

                @Override
                public Device save(Device device) {
                    devices.put(device.deviceId(), device);
                    return device;
                }

                @Override
                public Optional<Device> findById(UUID deviceId) {
                    return Optional.ofNullable(devices.get(deviceId));
                }

                @Override
                public List<Device> findByCustomerId(UUID customerId) {
                    return devices.values().stream()
                            .filter(device -> device.customerId().equals(customerId))
                            .toList();
                }
            };
        }
    }
}
