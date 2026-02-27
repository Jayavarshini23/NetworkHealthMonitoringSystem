package com.networkmanagement.networkhealthmonitor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "device.inactive")
public class DeviceHealthConfig {
    private long thresholdMinutes;
}
