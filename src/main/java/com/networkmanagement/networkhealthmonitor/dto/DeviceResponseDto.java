package com.networkmanagement.networkhealthmonitor.dto;

import com.arangodb.serde.jackson.Id;
import com.networkmanagement.networkhealthmonitor.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class DeviceResponseDto {
    @Id
    private String id;
    private String deviceUid;
    private String name;
    private String domain;
    private String ipAddress;
    private String vendor;
    private String model;
    private Status currentStatus;
    private LocalDateTime lastSeenAt;
}
