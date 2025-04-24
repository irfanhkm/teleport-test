package com.tracking.model;

import lombok.Builder;
import lombok.Value;
import java.time.Instant;

@Value
@Builder
public class TrackingNumber {
    private String id;
    private Instant generatedAt;
    
    public static TrackingNumber of(String id, Instant generatedAt) {
        return TrackingNumber.builder()
                .id(id)
                .generatedAt(generatedAt)
                .build();
    }
} 