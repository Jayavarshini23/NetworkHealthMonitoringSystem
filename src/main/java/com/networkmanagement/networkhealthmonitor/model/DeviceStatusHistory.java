package com.networkmanagement.networkhealthmonitor.model;

import com.arangodb.springframework.annotation.Document;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Document("device_status_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceStatusHistory {
    @Id
    private String id;   // Arango _key (String)

    @JsonIgnore
    private String deviceId;   // Store Device _key reference

    private Status status;     // Stored as STRING automatically

    private LocalDateTime at;
}
