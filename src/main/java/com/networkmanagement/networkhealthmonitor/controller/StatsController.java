package com.networkmanagement.networkhealthmonitor.controller;

import com.networkmanagement.networkhealthmonitor.dto.DeviceResponseDto;
import com.networkmanagement.networkhealthmonitor.dto.DomainCountDto;
import com.networkmanagement.networkhealthmonitor.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final DeviceService deviceService;

    //1 Devices filtered by domain
    @GetMapping("/devicesbydomain")
    public List<DeviceResponseDto> getDevicesByDomain(
            @RequestParam String domain
    ) {
        return deviceService.getDevicesByDomain(domain);
    }

//    //2 Domain-wise device count
//    @GetMapping("/devices-by-domain/count")
//    public List<DomainCountDto> devicesByDomain() {
//        return deviceService.devicesByDomain();
//    }

    //3 Active vs Inactive count
    @GetMapping("/active-vs-inactive")
    public Map<String, Long> activeVsInactive() {
        return deviceService.activeVsInactive();
    }

    //4 Newly added devices
    @GetMapping("/newly-added")
    public Long newlyAdded(
            @RequestParam(defaultValue = "24") int hours) {
        return deviceService.newlyAddedDevices(hours);
    }
}
