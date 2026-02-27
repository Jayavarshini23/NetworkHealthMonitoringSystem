package com.networkmanagement.networkhealthmonitor.repository;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import com.networkmanagement.networkhealthmonitor.dto.DomainCountDto;
import com.networkmanagement.networkhealthmonitor.model.Device;
import com.networkmanagement.networkhealthmonitor.model.Status;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeviceRepository extends ArangoRepository<Device, String> {

    //1
    @Query("""
FOR d IN devices
FILTER @domain == null OR d.domainName == @domain
LIMIT @offset, @limit
RETURN d
""")
    List<Device> findDevices(
            @Param("domain") String domain,
            @Param("limit") int limit,
            @Param("offset") int offset);

    //2
    @Query("""
FOR d IN devices
FILTER d.currentStatus == @status
COLLECT WITH COUNT INTO length
RETURN length
""")
    Long countByStatus(
            @Param("status") Status status);

    //3
    @Query("""
FOR d IN devices
COLLECT domain = d.domainName WITH COUNT INTO count
RETURN { domain: domain, count: count }
""")
    List<DomainCountDto> countDevicesByDomain();

    //4
    @Query("""
    FOR d IN devices
        FILTER d.createdAt >= @from
        COLLECT WITH COUNT INTO length
        RETURN length
    """)
    Long countDevicesAddedAfter(@Param("from") LocalDateTime from);

    //5
    @Query("""
    FOR d IN devices
        FILTER d.createdAt >= @from
        RETURN d
    """)
    List<Device> findDevicesCreatedAfter(@Param("from") LocalDateTime from);

    //6
    @Query("""
    FOR d IN devices
        FILTER d.domainName == @domain
        RETURN d
    """)
    List<Device> findByDomainName(@Param("domain") String domain);

    //7
    @Query("""
    FOR d IN devices
        FILTER d.currentStatus == @status
        RETURN d
    """)
    List<Device> findByStatus(@Param("status") Status status);

    // 8 Get distinct domain names from devices collection
    @Query("""
        FOR d IN devices
            COLLECT domain = d.domainName
            RETURN domain
    """)
    List<String> findDistinctDomains();


}
