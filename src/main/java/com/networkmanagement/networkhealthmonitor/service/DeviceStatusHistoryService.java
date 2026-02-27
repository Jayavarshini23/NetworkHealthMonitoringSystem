package com.networkmanagement.networkhealthmonitor.service;

import com.networkmanagement.networkhealthmonitor.model.DeviceStatusHistory;
import com.networkmanagement.networkhealthmonitor.model.Domain;
import com.networkmanagement.networkhealthmonitor.repository.DeviceStatusHistoryRepository;
import com.networkmanagement.networkhealthmonitor.repository.DomainRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceStatusHistoryService {

        private DeviceStatusHistoryRepository deviceStatusHistoryRepository;

    public DeviceStatusHistoryService(DeviceStatusHistoryRepository deviceStatusHistoryRepository) {
        this.deviceStatusHistoryRepository = deviceStatusHistoryRepository;
    }

    public List<DeviceStatusHistory> getAllDevicesHistory(){
        return (List<DeviceStatusHistory>) deviceStatusHistoryRepository.findAll();
    }


}
