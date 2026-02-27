package com.networkmanagement.networkhealthmonitor.model;

import com.arangodb.springframework.annotation.Document;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Document("devices")
@Data
public class Device {
    @Id
    private String id;
    private String deviceUid;
    private String name;
    private String domainName;
    private String ipAddress;
    private String vendor;
    private String model;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Status currentStatus;
    private LocalDateTime lastSeenAt;
}
