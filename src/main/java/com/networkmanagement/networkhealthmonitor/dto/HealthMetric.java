package com.networkmanagement.networkhealthmonitor.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class HealthMetric {

    private String deviceUid;
    private double cpuUsage;
    private double memoryUsage;

    // Store as Instant instead of raw Long
    private Instant lastSeenTs;
}
