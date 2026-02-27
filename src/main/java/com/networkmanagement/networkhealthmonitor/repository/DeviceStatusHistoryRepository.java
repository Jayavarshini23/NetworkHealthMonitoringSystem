package com.networkmanagement.networkhealthmonitor.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.networkmanagement.networkhealthmonitor.model.DeviceStatusHistory;

public interface DeviceStatusHistoryRepository extends ArangoRepository<DeviceStatusHistory, String> {
}
