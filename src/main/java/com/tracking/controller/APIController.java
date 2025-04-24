package com.tracking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracking.model.TrackingNumberRequest;
import com.tracking.model.TrackingNumberResponse;
import com.tracking.service.TrackingNumberGenerator;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class APIController {

    private final TrackingNumberGenerator trackingNumberGenerator;
    private final Validator validator;
    private final ObjectMapper objectMapper;

    @Autowired
    public APIController(TrackingNumberGenerator trackingNumberGenerator, 
                        ObjectMapper objectMapper,
                        Validator validator) {
        this.trackingNumberGenerator = trackingNumberGenerator;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @GetMapping("/tracking-number")
    public ResponseEntity<?> getNextTrackingNumber(@RequestParam Map<String, String> params) {
        Map<String, String> errors = new HashMap<>();
        
        try {
            // Convert params to TrackingNumberRequest using ObjectMapper
            TrackingNumberRequest request = objectMapper.convertValue(params, TrackingNumberRequest.class);

            // Manual validation
            Set<javax.validation.ConstraintViolation<TrackingNumberRequest>> violations = validator.validate(request);
            
            if (!violations.isEmpty()) {
                violations.forEach(violation -> {
                    String fieldName = violation.getPropertyPath().toString();
                    String errorMessage = violation.getMessage();
                    errors.put(fieldName, errorMessage);
                });
                return ResponseEntity.badRequest().body(errors);
            }
                    
            return ResponseEntity.ok(trackingNumberGenerator.generateTrackingNumber(request));
        } catch (IllegalArgumentException e) {
            errors.put("error", "Invalid parameter format: " + e.getMessage());
            return ResponseEntity.badRequest().body(errors);
        }
    }

    @GetMapping("/ping")
    public String test() {
        return "pong";
    }
} 