package com.tracking.util;

import com.tracking.model.TrackingNumber;
import org.springframework.stereotype.Component;
import java.net.InetAddress;
import java.time.Instant;
import lombok.NonNull;


@Component
public class SnowflakeIdGenerator {
    // Fixed epoch: 2020-01-01T00:00:00Z (custom)
    private static final long CUSTOM_EPOCH = 1577836800000L;

    private final long epoch;
    private final int machineId;
    private int sequence;

    public SnowflakeIdGenerator() {
        this.epoch = CUSTOM_EPOCH;
        this.machineId = generateMachineIdFromHostname();
        this.sequence = 0;
    }

    public TrackingNumber nextId(@NonNull String companyCode) {
        long timestamp = System.currentTimeMillis() - epoch;

        // Bit shift: 41 bits timestamp, 10 bits machine ID, 12 bits sequence
        long id = (timestamp << 22) | ((machineId & 0x3FF) << 12) | (sequence++ & 0xFFF);

        String encodedId = encodeBase36(id);
        String fullTrackingNumber = (companyCode.toUpperCase() + encodedId).substring(0, Math.min(16, companyCode.length() + encodedId.length()));

        Instant generatedAt = Instant.ofEpochMilli(epoch + timestamp);

        return TrackingNumber.of(fullTrackingNumber, generatedAt);
    }

    private String encodeBase36(long id) {
        return Long.toString(id, 36).toUpperCase(); // Base36 for alphanumeric compactness
    }

    private int generateMachineIdFromHostname() {
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            int hash = hostname.hashCode(); // Optional: replace with more stable machine ID logic
            return Math.abs(hash % 1024); // 10-bit machine ID
        } catch (Exception e) {
            throw new IllegalStateException("Failed to determine machine ID", e);
        }
    }
} 