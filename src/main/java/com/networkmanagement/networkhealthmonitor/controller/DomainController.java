package com.networkmanagement.networkhealthmonitor.controller;

import com.networkmanagement.networkhealthmonitor.dto.DomainCountDto;
import com.networkmanagement.networkhealthmonitor.model.Domain;
import com.networkmanagement.networkhealthmonitor.service.DomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/domains")
@RequiredArgsConstructor
public class DomainController {
    private final DomainService domainService;

    @GetMapping("/all")
    public ResponseEntity<List<Domain>> getEachDomain(){
        List<Domain> domains = domainService.getEachDomain();
        return ResponseEntity.ok(domains);
    }

    //1 Create Domain
    @PostMapping("/createdomain")
    public ResponseEntity<Domain> create(@RequestBody Domain domain) {
        return ResponseEntity.ok(domainService.createDomain(domain));
    }

    //2 Get all distinct domains
    @GetMapping
    public ResponseEntity<List<String>> getAllDomains() {
        return ResponseEntity.ok(domainService.getAllDomains());
    }

    //3 Get domain-wise device count
    @GetMapping("/device-count")
    public ResponseEntity<List<DomainCountDto>> getDomainCounts() {
        return ResponseEntity.ok(domainService.getDomainCounts());
    }

    // To Check the method Conflict

//    //2
//    @GetMapping
//    public ResponseEntity<List<String>> getAllDomains() {
//        List<String> domains = deviceRepository.findDistinctDomains();
//        return ResponseEntity.ok(domains);
//
//    }
//
//    //3
//    @GetMapping("/device-count")
//    public ResponseEntity<List<DomainCountDto>> getDomainCounts() {
//        List<DomainCountDto> stats = domainRepository.countDevicesByDomain();
//        return ResponseEntity.ok(stats);
//    }



}
