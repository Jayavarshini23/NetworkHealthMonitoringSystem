package com.networkmanagement.networkhealthmonitor.service;

import com.networkmanagement.networkhealthmonitor.dto.DeviceResponseDto;
import com.networkmanagement.networkhealthmonitor.dto.DomainCountDto;
import com.networkmanagement.networkhealthmonitor.model.Device;
import com.networkmanagement.networkhealthmonitor.model.Status;
import com.networkmanagement.networkhealthmonitor.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    //API 1 - Devices Grouped By Domain
    public List<DomainCountDto> devicesByDomain(){
        return deviceRepository.countDevicesByDomain();
    }

    //API 2 - Active Vs Inactive
    public Map<String, Long> activeVsInactive(){
        return Map.of(
                "ACTIVE", deviceRepository.countByStatus(Status.ACTIVE),
                "INACTIVE", deviceRepository.countByStatus(Status.INACTIVE)
        );
    }

    //API 3 - Newly added devices
    public long newlyAddedDevices(int hours){
        LocalDateTime from = LocalDateTime.now().minusHours(hours);
        return deviceRepository.countDevicesAddedAfter(from);
    }

    // API 4 - Pagination using AQL LIMIT
    public List<DeviceResponseDto> getDevices(String domain, int limit, int offset) {
        return deviceRepository.findDevices(domain, limit, offset)
                .stream()
                .map(d -> new DeviceResponseDto(
                        d.getId(),
                        d.getDeviceUid(),
                        d.getName(),
                        d.getDomainName(),
                        d.getIpAddress(),
                        d.getVendor(),
                        d.getModel(),
                        d.getCurrentStatus(),
                        d.getLastSeenAt()
                ))
                .toList();
    }

    // API 5 - Get device by key
    public DeviceResponseDto getDevice(String id) {
        Device d = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        return new DeviceResponseDto(
                d.getId(),
                d.getDeviceUid(),
                d.getName(),
                d.getDomainName(),
                d.getIpAddress(),
                d.getVendor(),
                d.getModel(),
                d.getCurrentStatus(),
                d.getLastSeenAt()
        );
    }


    public long getNewDevices(int hours) {
        LocalDateTime timeThreshold = LocalDateTime.now().minusHours(hours);
        return deviceRepository.countDevicesAddedAfter(timeThreshold);
    }

    public List<Device> getDevicesByAge(int hours) {
        return deviceRepository.findDevicesCreatedAfter(
                LocalDateTime.now().minusHours(hours)
        );
    }

    public List<DeviceResponseDto> getDevicesByDomain(String domain) {
        return deviceRepository.findByDomainName(domain).stream()
                .map(d -> new DeviceResponseDto(
                        d.getId(),
                        d.getDeviceUid(),
                        d.getName(),
                        d.getDomainName(),
                        d.getIpAddress(),
                        d.getVendor(),
                        d.getModel(),
                        d.getCurrentStatus(),
                        d.getLastSeenAt()
                ))
                .toList();
    }

    public Long getNewDevicesCount(int hours) {
        LocalDateTime time = LocalDateTime.now().minusHours(hours);
        return deviceRepository.countDevicesAddedAfter(time);
    }

    public Long getNewDevicesCount(String type) {

        LocalDateTime fromTime;

        switch (type.toLowerCase()) {

            case "today":
                fromTime = LocalDate.now().atStartOfDay();
                break;

            case "24hours":
                fromTime = LocalDateTime.now().minusHours(24);
                break;

            case "7days":
                fromTime = LocalDateTime.now().minusDays(7);
                break;

            default:
                throw new IllegalArgumentException("Invalid type. Use today, 24hours or 7days");
        }

        return deviceRepository.countDevicesAddedAfter(fromTime);
    }

    public List<Device> getAllDevices() {
        Iterable<Device> iterable = deviceRepository.findAll();
        List<Device> devices = new ArrayList<>();
        iterable.forEach(devices::add);
        return devices;
    }

    public List<Device> getActiveDevices() {
        return deviceRepository.findByStatus(Status.ACTIVE);
    }

    public List<Device> getInactiveDevices() {
        return deviceRepository.findByStatus(Status.INACTIVE);
    }
}
