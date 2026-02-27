package com.networkmanagement.networkhealthmonitor.service;

import com.networkmanagement.networkhealthmonitor.dto.DomainCountDto;
import com.networkmanagement.networkhealthmonitor.model.Domain;
import com.networkmanagement.networkhealthmonitor.repository.DeviceRepository;
import com.networkmanagement.networkhealthmonitor.repository.DomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DomainService {
   private final DomainRepository domainRepository;
   private final DeviceRepository deviceRepository;

   // 1 Create Domain
    public Domain createDomain(Domain domain) {

        // 1️ Validation
        if (domain.getName() == null || domain.getName().isBlank()) {
            throw new IllegalArgumentException("Domain name cannot be empty");
        }

        // 2️ Duplicate Check
        if (domainRepository.existsByName(domain.getName())) {
            throw new RuntimeException("Domain already exists");
        }

        // 3️ Save using Repository
        return domainRepository.save(domain);
    }

    // 2 Get All Distinct Domains
    public List<String> getAllDomains() {
        return deviceRepository.findDistinctDomains();
    }

    // 3 Get Domain-Wise Device Count
    public List<DomainCountDto> getDomainCounts() {
        return deviceRepository.countDevicesByDomain();
    }

    public List<Domain>  getEachDomain(){
        return (List<Domain>) domainRepository.findAll();
    }


}
