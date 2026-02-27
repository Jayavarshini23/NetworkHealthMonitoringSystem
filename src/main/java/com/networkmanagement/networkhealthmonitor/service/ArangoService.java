package com.networkmanagement.networkhealthmonitor.service;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDatabase;
import com.arangodb.model.AqlQueryOptions;
import com.networkmanagement.networkhealthmonitor.dto.HealthMetric;
import com.networkmanagement.networkhealthmonitor.model.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ArangoService {
    private final ArangoDatabase arangoDatabase;

    private static final String METRICS_COLLECTION = "health_metrics";
    private static final String DEVICE_COLLECTION = "devices";

    // Save a Single Metric
    public void saveMetric(HealthMetric metric){
        ArangoCollection collection = arangoDatabase.collection(METRICS_COLLECTION);
        collection.insertDocument(metric);
    }

    // Get latest metric for a single device
    public HealthMetric getLatestMetric(String deviceUid){
        String query = """
                FOR m IN @@collection
                FILTER m.deviceUid == @uid
                SORT m.lastSeenTs DESC
                LIMIT 1
                RETURN {
                    deviceUid: m.deviceUid,
                    cpuUsage: m.cpuUsage,
                    memoryUsage: m.memoryUsage,
                    lastSeenTs: m.lastSeenTs
                    }
                """;
        Map<String, Object> params = Map.of(
                "uid", deviceUid,
                "@collection", METRICS_COLLECTION
        );
        AqlQueryOptions options = new AqlQueryOptions().batchSize(1);

        ArangoCursor<HealthMetric> cursor =
                arangoDatabase.query(query, HealthMetric.class, params, options);

        return cursor.hasNext() ? cursor.next() : null;
    }

    //Get all Devices along with their latest metric

    public List<Device> getAllDevicesWithLatestMetrics(){
        String query = """
                FOR d IN @@deviceCollection
                LET latestMetric = (
                FOR m IN @@metricCollection
                FILTER m.deviceUid == d.deviceUid
                SORT m.lastSeenTs DESC 
                LIMIT 1
                RETURN m
                )
                RETURN MERGE(d, {latestMetric: latestMetric[0] })
                """;

        Map<String, Object> params = Map.of(
                "@deviceCollection", DEVICE_COLLECTION,
                "@metricCollection", METRICS_COLLECTION
        );

        ArangoCursor<Device> cursor =
                arangoDatabase.query(query, Device.class, params, new AqlQueryOptions());

        List<Device> devices = new ArrayList<>();
        cursor.forEachRemaining(devices::add);
        return devices;

    }

    // Update device status in ArangoDB
    public void updateDeviceStatus(Device device) {
        String query = """
            FOR d IN @@deviceCollection
                FILTER d.deviceUid == @uid
                UPDATE d WITH { currentStatus: @status, lastSeenAt: @lastSeenAt } IN @@deviceCollection
        """;

        Map<String, Object> params = Map.of(
                "uid", device.getDeviceUid(),
                "status", device.getCurrentStatus().name(),
                "lastSeenAt", Instant.now().toEpochMilli(),
                "@deviceCollection", DEVICE_COLLECTION
        );

        arangoDatabase.query(query, Void.class, params, new AqlQueryOptions());
    }
}
