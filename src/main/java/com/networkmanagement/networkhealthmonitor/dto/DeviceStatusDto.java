package com.networkmanagement.networkhealthmonitor.dto;

import com.networkmanagement.networkhealthmonitor.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DeviceStatusDto {
    // Arango document _key
    private String id;

    private String deviceUid;
    private String name;

    // Domain stored as String, not foreign key
    private String domain;

    private String ipAddress;
    private String vendor;
    private String model;

    private Status currentStatus;
    private LocalDateTime lastSeenAt;
}
