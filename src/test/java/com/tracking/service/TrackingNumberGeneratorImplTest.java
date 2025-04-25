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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TrackingNumberGeneratorImplTest {

    @Mock
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private TrackingNumberGeneratorImpl trackingNumberGenerator;

    private TrackingNumberRequest validRequest;

    @Before
    public void setUp() {
        validRequest = TrackingNumberRequest.builder()
                .originCountryId("US")
                .destinationCountryId("GB")
                .weight(1.5)
                .createdAt("2024-04-24T12:00:00Z")
                .customerId("123e4567-e89b-12d3-a456-426614174000")
                .customerName("Test Customer")
                .customerSlug("test-customer")
                .build();

        when(snowflakeIdGenerator.nextId("TL")).thenReturn(TrackingNumber.builder()
                .id("TL123456789")
                .generatedAt(java.time.Instant.parse("2024-04-24T12:00:00Z"))
                .build());
    }

    @Test
    public void generateTrackingNumber_WithValidRequest_ReturnsValidResponse() {
        // Arrange
        when(redisService.setIfNotExists(anyString(), anyString(), anyInt())).thenReturn(true);

        // Act
        TrackingNumberResponse response = trackingNumberGenerator.generateTrackingNumber(validRequest);

        // Assert
        assertNotNull(response);
        assertEquals("TL123456789", response.getTrackingNumber());
        assertEquals("2024-04-24T12:00:00Z", response.getGeneratedAt());
        
        // Verify Redis interaction
        verify(redisService).setIfNotExists("tracking:TL123456789", "1", 1);
    }

    @Test(expected = NullPointerException.class)
    public void generateTrackingNumber_WithNullRequest_ThrowsException() {
        // Act
        trackingNumberGenerator.generateTrackingNumber(null);
    }

    @Test
    public void generateTrackingNumber_WhenRedisSetFails_RetriesWithNewTrackingNumber() {
        // Arrange
        when(redisService.setIfNotExists(anyString(), anyString(), anyInt()))
                .thenReturn(false)  // First attempt fails
                .thenReturn(true);  // Second attempt succeeds

        when(snowflakeIdGenerator.nextId("TL"))
                .thenReturn(TrackingNumber.builder()
                        .id("TL123456789")
                        .generatedAt(java.time.Instant.parse("2024-04-24T12:00:00Z"))
                        .build())
                .thenReturn(TrackingNumber.builder()
                        .id("TL987654321")
                        .generatedAt(java.time.Instant.parse("2024-04-24T12:00:01Z"))
                        .build());

        // Act
        TrackingNumberResponse response = trackingNumberGenerator.generateTrackingNumber(validRequest);

        // Assert
        assertNotNull(response);
        assertEquals("TL987654321", response.getTrackingNumber());
        
        // Verify Redis was called twice
        verify(redisService, times(2)).setIfNotExists(anyString(), anyString(), anyInt());
    }

    @Test(expected = IllegalStateException.class)
    public void generateTrackingNumber_WhenAllRetriesFail_ThrowsException() {
        // Arrange
        when(redisService.setIfNotExists(anyString(), anyString(), anyInt())).thenReturn(false);
        when(snowflakeIdGenerator.nextId("TL"))
                .thenReturn(TrackingNumber.builder()
                        .id("TL123456789")
                        .generatedAt(java.time.Instant.parse("2024-04-24T12:00:00Z"))
                        .build());

        // Act
        trackingNumberGenerator.generateTrackingNumber(validRequest);
    }
} 