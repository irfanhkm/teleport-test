package com.tracking.service;

import com.tracking.model.TrackingNumber;
import com.tracking.model.TrackingNumberRequest;
import com.tracking.model.TrackingNumberResponse;
import com.tracking.util.SnowflakeIdGenerator;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TrackingNumberGeneratorImpl implements TrackingNumberGenerator {

    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private final RedisService redisService;
    private static final String COMPANY_CODE = "TL";
    private static final String TRACKING_NUMBER_KEY_PREFIX = "tracking:";
    private static final int TRACKING_NUMBER_TTL_DAYS = 1; 

    @Autowired
    public TrackingNumberGeneratorImpl(SnowflakeIdGenerator snowflakeIdGenerator,
                                     RedisService redisService) {
        this.snowflakeIdGenerator = snowflakeIdGenerator;
        this.redisService = redisService;
    }

    @Override
    public TrackingNumberResponse generateTrackingNumber(@NonNull TrackingNumberRequest request) {
        TrackingNumber trackingNumber;
        String trackingNumberId;
        int maxAttempts = 3; // Maximum number of attempts to generate a unique tracking number

        log.info("Generating tracking number for request: {}", request);

        // Try to generate a unique tracking number
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            trackingNumber = snowflakeIdGenerator.nextId(COMPANY_CODE);
            trackingNumberId = trackingNumber.getId();
            
            // Use Redis SET NX command directly
            String key = TRACKING_NUMBER_KEY_PREFIX + trackingNumberId;
            if (redisService.setIfNotExists(key, "1", TRACKING_NUMBER_TTL_DAYS * 24 * 60 * 60)) {
                log.info("Generated tracking number: {}", trackingNumberId);
                return TrackingNumberResponse.builder()
                        .trackingNumber(trackingNumberId)
                        .generatedAt(trackingNumber.getGeneratedAt().toString())
                        .build();
            }
        }

        log.error("Failed to generate a unique tracking number after {} attempts", maxAttempts);
        throw new IllegalStateException("Failed to generate a unique tracking number after " + maxAttempts + " attempts");
    }
} 