package com.tracking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TrackingNumberResponse {
    @JsonProperty("tracking_number")
    private final String trackingNumber;
    
    @JsonProperty("generated_at")
    private final String generatedAt;
} 