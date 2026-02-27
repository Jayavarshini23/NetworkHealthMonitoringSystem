package com.networkmanagement.networkhealthmonitor.simulator;

import com.networkmanagement.networkhealthmonitor.model.Device;
import com.networkmanagement.networkhealthmonitor.model.Domain;
import com.networkmanagement.networkhealthmonitor.model.Status;
import com.networkmanagement.networkhealthmonitor.repository.DeviceRepository;
import com.networkmanagement.networkhealthmonitor.repository.DomainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeviceSimulator {
    private final DeviceRepository deviceRepository;
    private final Random random = new Random();
    private final DomainRepository domainRepository;

    //Run Every 20 - 60 Seconds
    @Scheduled(fixedDelayString = "#{T(java.util.concurrent.ThreadLocalRandom).current().nextInt(20000,60000)}")
    public void addRandomDevice(){

        //Fetch all Domains
        Iterable<Domain> allDomains = domainRepository.findAll();
        List<String> domains = StreamSupport.stream(allDomains.spliterator(), false)
                .map(d-> d.getName())
                .distinct()
                .collect(Collectors.toList());

        if (domains.isEmpty()){
            log.warn("No Domains found. Skiping Device creation. ");
            return;
        }

        //Pick a Random Domain
        String domainName = domains.get(random.nextInt(domains.size()));

        //Create a New Random Device
        Device device = new Device();
        device.setDeviceUid("device-"+ UUID.randomUUID());
        device.setName("Device-" + random.nextInt(1000));
        device.setDomainName(domainName);
        device.setModel("Model-" + random.nextInt(10));
        device.setVendor("Cisco");
        device.setIpAddress("192.168.1." + random.nextInt(255));
        device.setCurrentStatus(Status.ACTIVE);
        device.setLastSeenAt(LocalDateTime.now());
        device.setCreatedAt(LocalDateTime.now());
        device.setUpdatedAt(LocalDateTime.now());

        //Save Device using Repository
        deviceRepository.save(device);

        log.info("Random device added to ArangoDB via repository: {}", device.getDeviceUid());

    }


}
