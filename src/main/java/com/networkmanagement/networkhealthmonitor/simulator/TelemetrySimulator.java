package com.networkmanagement.networkhealthmonitor.simulator;

import com.networkmanagement.networkhealthmonitor.dto.HealthMetric;
import com.networkmanagement.networkhealthmonitor.model.Device;
import com.networkmanagement.networkhealthmonitor.model.Status;
import com.networkmanagement.networkhealthmonitor.repository.DeviceRepository;
import com.networkmanagement.networkhealthmonitor.service.ArangoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelemetrySimulator {

    private final DeviceRepository deviceRepository;
    private final ArangoService arangoService;
    private final Random random = new Random();

    @Scheduled(fixedRate = 30000)
    public void simulateTelemetry() {

        try {
            // 1 Fetch all Devices from ArangoDB
            Iterable<Device> deviceIterable = deviceRepository.findAll();
            List<Device> devices = StreamSupport.stream(deviceIterable.spliterator(), false)
                    .collect(Collectors.toList());

            if (devices.isEmpty()) {
                log.warn("No devices found in ArangoDB to generate telemetry.");
                return;
            }

            // 2 Generate telemetry for each device
            for (Device device : devices) {

                HealthMetric metric = new HealthMetric();
                metric.setDeviceUid(device.getDeviceUid());
                metric.setCpuUsage(random.nextDouble() * 100);     // CPU %
                metric.setMemoryUsage(random.nextDouble() * 100);  // Memory %

                // 20% chance device is inactive
                boolean isInactive = random.nextInt(10) < 2;
                Instant lastSeen;

                if (isInactive) {
                    lastSeen = Instant.now().minus(Duration.ofMinutes(10));
                    device.setCurrentStatus(Status.INACTIVE);
                } else {
                    lastSeen = Instant.now();
                    device.setCurrentStatus(Status.ACTIVE);
                }

                metric.setLastSeenTs(lastSeen);

                // 3 Update device status in ArangoDB
                deviceRepository.save(device);

                // 4 Save metric in ArangoDB
                try {
                    arangoService.saveMetric(metric);
                    log.info("Telemetry metric saved for device: {}", device.getDeviceUid());
                } catch (Exception e) {
                    log.warn("Failed to save metric for device {}: {}", device.getDeviceUid(), e.getMessage());
                }
            }
            log.info("Telemetry simulation completed for {} devices", devices.size());

        } catch (Exception e) {
            log.error("Error during telemetry simulation: {}", e.getMessage(), e);
        }
    }
}






