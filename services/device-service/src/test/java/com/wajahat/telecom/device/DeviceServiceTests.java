package com.wajahat.telecom.device;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wajahat.telecom.device.domain.Device;
import com.wajahat.telecom.device.domain.DeviceType;
import com.wajahat.telecom.device.dto.CreateDeviceRequest;
import com.wajahat.telecom.device.exception.DeviceNotFoundException;
import com.wajahat.telecom.device.repository.DeviceRepository;
import com.wajahat.telecom.device.service.DeviceService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DeviceServiceTests {

    private final DeviceRepository repository = new TestDeviceRepository();
    private final DeviceService deviceService = new DeviceService(repository);

    @Test
    void createsDeviceForCustomer() {
        UUID customerId = UUID.randomUUID();

        Device device = deviceService.create(new CreateDeviceRequest(
                customerId,
                DeviceType.PHONE,
                "Field Engineer Phone",
                "123456789012345"));

        assertThat(device.deviceId()).isNotNull();
        assertThat(device.customerId()).isEqualTo(customerId);
        assertThat(device.type()).isEqualTo(DeviceType.PHONE);
    }

    @Test
    void listsDevicesForCustomer() {
        UUID customerId = UUID.randomUUID();
        deviceService.create(new CreateDeviceRequest(customerId, DeviceType.TABLET, "Tablet", "123456789012345"));

        assertThat(deviceService.getByCustomerId(customerId)).hasSize(1);
    }

    @Test
    void throwsWhenDeviceDoesNotExist() {
        UUID missingDeviceId = UUID.randomUUID();

        assertThatThrownBy(() -> deviceService.getById(missingDeviceId))
                .isInstanceOf(DeviceNotFoundException.class)
                .hasMessageContaining(missingDeviceId.toString());
    }

    private static class TestDeviceRepository implements DeviceRepository {

        private final java.util.Map<UUID, Device> devices = new java.util.LinkedHashMap<>();

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
    }
}
