package com.tracking.service;

import com.tracking.model.TrackingNumberRequest;
import com.tracking.model.TrackingNumberResponse;
import lombok.NonNull;

public interface TrackingNumberGenerator {
    TrackingNumberResponse generateTrackingNumber(@NonNull TrackingNumberRequest request);
} 