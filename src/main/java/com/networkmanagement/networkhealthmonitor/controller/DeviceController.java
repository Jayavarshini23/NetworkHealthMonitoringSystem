package com.networkmanagement.networkhealthmonitor.controller;

import com.networkmanagement.networkhealthmonitor.dto.DeviceResponseDto;
import com.networkmanagement.networkhealthmonitor.model.Device;
import com.networkmanagement.networkhealthmonitor.model.DeviceStatusHistory;
import com.networkmanagement.networkhealthmonitor.repository.DeviceRepository;
import com.networkmanagement.networkhealthmonitor.repository.DeviceStatusHistoryRepository;
import com.networkmanagement.networkhealthmonitor.service.DeviceService;
import com.networkmanagement.networkhealthmonitor.service.DeviceStatusHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;
    private final DeviceRepository deviceRepository;
    private final DeviceStatusHistoryService deviceStatusHistoryService;

    //10
    @GetMapping("/getalldevices")
    public List<Device> getAllDevices(){
        return deviceService.getAllDevices();
    }

    //9
    @GetMapping
    public List<DeviceResponseDto> list(
            @RequestParam(required = false) String domain,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {

        return deviceService.getDevices(domain, limit, offset);
    }

    //8
    @GetMapping("/id/{id}")
    public DeviceResponseDto get(@PathVariable String id){
        return deviceService.getDevice(id);
    }

    //7
    @GetMapping("/devices/recent")
    public List<Device> getRecentDevices(@RequestParam int hours){
        return deviceService.getDevicesByAge(hours);
    }

    //6
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalDeviceCount() {
        return ResponseEntity.ok(deviceRepository.count());
    }

    //5
    @GetMapping("/new-devices")
    public ResponseEntity<Map<String, Object>> getNewDevices(
            @RequestParam(defaultValue = "24") int hours) {

        Long count = deviceService.getNewDevicesCount(hours);

        Map<String, Object> response = new HashMap<>();
        response.put("hours", hours);
        response.put("newDevicesCount", count);

        return ResponseEntity.ok(response);
    }

    //4
    @GetMapping("/devices/new/count")
    public ResponseEntity<Long> getNewDevicesCount(@RequestParam String type) {
        Long count = deviceService.getNewDevicesCount(type);
        return ResponseEntity.ok(count);
    }

    //3
    @GetMapping("/getnewdevices")
    public ResponseEntity<List<Device>> getListNewDevices(
            @RequestParam(defaultValue = "24") int hours) {

        LocalDateTime time = LocalDateTime.now().minusHours(hours);
        return ResponseEntity.ok(deviceRepository.findDevicesCreatedAfter(time));
    }

    //2
    @GetMapping("/active")
    public ResponseEntity<List<Device>> getActiveDevices() {
        return ResponseEntity.ok(deviceService.getActiveDevices());
    }

    //1
    @GetMapping("/inactive")
    public ResponseEntity<List<Device>> getInactiveDevices() {
        return ResponseEntity.ok(deviceService.getInactiveDevices());
    }

    @GetMapping("/getalldeviceHistory")
    public List<DeviceStatusHistory> getAllDeviceHistory(){
            return deviceStatusHistoryService.getAllDevicesHistory();
    }
}
