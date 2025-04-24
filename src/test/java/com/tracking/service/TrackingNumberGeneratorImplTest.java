package com.tracking.service;

import com.tracking.model.TrackingNumber;
import com.tracking.model.TrackingNumberRequest;
import com.tracking.model.TrackingNumberResponse;
import com.tracking.util.SnowflakeIdGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TrackingNumberGeneratorImplTest {

    @Mock
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @InjectMocks
    private TrackingNumberGeneratorImpl trackingNumberGenerator;

    @Before
    public void setUp() {
        when(snowflakeIdGenerator.nextId("TL")).thenReturn(TrackingNumber.builder()
                .id("TL123456789")
                .generatedAt(java.time.Instant.parse("2024-04-24T12:00:00Z"))
                .build());
    }

    @Test
    public void generateTrackingNumber_WithValidRequest_ReturnsValidResponse() {
        // Arrange
        TrackingNumberRequest request = TrackingNumberRequest.builder()
                .originCountryId("US")
                .destinationCountryId("GB")
                .weight(1.5)
                .createdAt("2024-04-24T12:00:00Z")
                .customerId("123e4567-e89b-12d3-a456-426614174000")
                .customerName("Test Customer")
                .customerSlug("test-customer")
                .build();

        // Act
        TrackingNumberResponse response = trackingNumberGenerator.generateTrackingNumber(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getTrackingNumber());
        assertTrue(response.getTrackingNumber().startsWith("TL"));
        assertEquals("2024-04-24T12:00:00Z", response.getGeneratedAt());
    }

    @Test(expected = NullPointerException.class)
    public void generateTrackingNumber_WithNullRequest_ThrowsException() {
        // Act
        trackingNumberGenerator.generateTrackingNumber(null);
    }
} 