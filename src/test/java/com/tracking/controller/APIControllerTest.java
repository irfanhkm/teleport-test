package com.tracking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracking.model.TrackingNumberRequest;
import com.tracking.model.TrackingNumberResponse;
import com.tracking.service.TrackingNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.validation.Validator;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class APIControllerTest {

    @Mock
    private TrackingNumberGenerator trackingNumberGenerator;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private APIController apiController;

    private Map<String, String> validParams;

    @BeforeEach
    void setUp() {
        validParams = new HashMap<>();
        validParams.put("originCountryId", "ID");
        validParams.put("destinationCountryId", "ID");
        validParams.put("weight", "1.5");
        validParams.put("createdAt", "2024-04-24T12:00:00Z");
        validParams.put("customerId", "123e4567-e89b-12d3-a456-426614174000");
        validParams.put("customerName", "Test Customer");
        validParams.put("customerSlug", "test-customer");
    }

    @Test
    void getNextTrackingNumber_WithValidParams_ReturnsSuccessResponse() throws Exception {
        // Arrange
        TrackingNumberRequest request = TrackingNumberRequest.builder()
                .originCountryId("ID")
                .destinationCountryId("ID")
                .weight(1.5)
                .createdAt("2024-04-24T12:00:00Z")
                .customerId("123e4567-e89b-12d3-a456-426614174000")
                .customerName("Test Customer")
                .customerSlug("test-customer")
                .build();

        TrackingNumberResponse response = TrackingNumberResponse.builder()
                .trackingNumber("TL123456789")
                .generatedAt("2024-04-24T12:00:00Z")
                .build();

        when(objectMapper.convertValue(validParams, TrackingNumberRequest.class)).thenReturn(request);
        when(validator.validate(request)).thenReturn(java.util.Collections.emptySet());
        when(trackingNumberGenerator.generateTrackingNumber(request)).thenReturn(response);

        // Act
        ResponseEntity<?> result = apiController.getNextTrackingNumber(validParams);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertTrue(result.getBody() instanceof TrackingNumberResponse);
        TrackingNumberResponse body = (TrackingNumberResponse) result.getBody();
        assertEquals("TL123456789", body.getTrackingNumber());
        assertEquals("2024-04-24T12:00:00Z", body.getGeneratedAt());
    }

    @Test
    void ping_ReturnsPong() {
        // Act
        String result = apiController.test();

        // Assert
        assertEquals("pong", result);
    }
}
 