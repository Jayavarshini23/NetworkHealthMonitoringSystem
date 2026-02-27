package com.networkmanagement.networkhealthmonitor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainCountDto {
    private String domain;
    private Long count;
}
