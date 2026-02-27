package com.networkmanagement.networkhealthmonitor.scheduler;

import com.networkmanagement.networkhealthmonitor.config.DeviceHealthConfig;
import com.networkmanagement.networkhealthmonitor.dto.HealthMetric;
import com.networkmanagement.networkhealthmonitor.model.Device;
import com.networkmanagement.networkhealthmonitor.model.DeviceStatusHistory;
import com.networkmanagement.networkhealthmonitor.model.Status;
import com.networkmanagement.networkhealthmonitor.repository.DeviceRepository;
import com.networkmanagement.networkhealthmonitor.repository.DeviceStatusHistoryRepository;
import com.networkmanagement.networkhealthmonitor.service.ArangoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceHealthScheduler {
    private final DeviceRepository deviceRepository;
    private final DeviceStatusHistoryRepository deviceStatusHistoryRepository;
    private final ArangoService arangoService;
    private final DeviceHealthConfig config;

    @Scheduled(fixedRate = 60000)
    public void evaluateHealth(){

        try {
            //List<Device> devices = deviceRepository.findAll();
            Iterable<Device> iterable = deviceRepository.findAll();
            List<Device> devices = StreamSupport
                    .stream(iterable.spliterator(), false)
                    .collect(Collectors.toList());
            Instant now = Instant.now();

            for (Device device : devices) {
                try {
                    HealthMetric metric = arangoService.getLatestMetric(device.getDeviceUid());
                    if (metric == null || metric.getLastSeenTs() == null) {
                        continue;
                    }

                    Instant lastSeen = metric.getLastSeenTs();
                    long minutes = ChronoUnit.MINUTES.between(lastSeen, now);

                    Status newStatus = minutes > config.getThresholdMinutes()
                            ? Status.INACTIVE
                            : Status.ACTIVE;

                    if (!newStatus.equals(device.getCurrentStatus())) {
                        device.setCurrentStatus(newStatus);
                        device.setLastSeenAt(LocalDateTime.now());
                        deviceRepository.save(device);

                        // Save history in ArangoDB
                        DeviceStatusHistory history = new DeviceStatusHistory();
                        history.setDeviceId(device.getDeviceUid()); // Store device UID as reference
                        history.setStatus(newStatus);
                        history.setAt(LocalDateTime.now());

                        deviceStatusHistoryRepository.save(history);

                        log.info("Device {} status changed to {} (inactive {} mins)",
                                device.getDeviceUid(),
                                newStatus,
                                minutes);
                    }

                } catch (Exception e) {
                    log.warn("Failed to evaluate health for device {}: {}", device.getDeviceUid(), e.getMessage());
                }
            }

            log.info("Health evaluation completed");

        } catch (Exception e) {
            log.error("Error in health evaluation: {}", e.getMessage());
        }

    }
}
