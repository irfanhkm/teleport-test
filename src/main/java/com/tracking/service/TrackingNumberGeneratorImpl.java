package com.tracking.service;

import com.tracking.model.TrackingNumber;
import com.tracking.model.TrackingNumberRequest;
import com.tracking.model.TrackingNumberResponse;
import com.tracking.util.SnowflakeIdGenerator;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackingNumberGeneratorImpl implements TrackingNumberGenerator {

    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private static final String COMPANY_CODE = "TL";

    @Autowired
    public TrackingNumberGeneratorImpl(SnowflakeIdGenerator snowflakeIdGenerator) {
        this.snowflakeIdGenerator = snowflakeIdGenerator;
    }

    @Override
    public TrackingNumberResponse generateTrackingNumber(@NonNull TrackingNumberRequest request) {
        // can added another logic here
        // example: checking customer id exist or not in db

        // generate tracking number
        TrackingNumber trackingNumber = snowflakeIdGenerator.nextId(COMPANY_CODE);

        // return response
        return TrackingNumberResponse.builder()
                .trackingNumber(trackingNumber.getId())
                .generatedAt(trackingNumber.getGeneratedAt().toString())
                .build();
    }
} 